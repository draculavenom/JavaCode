package com.draculavenom.schedulingSystem.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.schedulingSystem.dto.DayShiftRequest;
import com.draculavenom.schedulingSystem.dto.WorkScheduleRequest;
import com.draculavenom.schedulingSystem.dto.WorkShiftResponse;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;
import com.draculavenom.schedulingSystem.model.DayOfWeekEnum;
import com.draculavenom.schedulingSystem.model.ScheduleConfig;
import com.draculavenom.schedulingSystem.model.ScheduleConfigRepository;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {
    @Autowired
    private ScheduleConfigRepository scheduleConfigRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional
    public void createOrReplaceSchedule(Integer managerId, WorkScheduleRequest request, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        if (!isAdmin && !currentUser.getId().equals(managerId)) {
            throw new RuntimeException("You cannont modify another manager's schedule");
        }

        User manager = userRepository.findById(managerId).orElseThrow(() -> new RuntimeException("Manager not found"));

        scheduleConfigRepository.deleteByManager(manager);

        for (DayShiftRequest shiftRequest : request.getShifts()) {

            ScheduleConfig shift = new ScheduleConfig();
            shift.setDayOfWeek(shiftRequest.getDay());
            shift.setStartingTime(shiftRequest.getStartingTime());
            shift.setEndingTime(shiftRequest.getEndingTime());
            shift.setAppointmentDuration(shiftRequest.getAppointmentDuration());
            shift.setManager(manager);

            scheduleConfigRepository.save(shift);
        }
    }

    public List<WorkShiftResponse> getSchedule(Integer managerId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        if (!isAdmin && !currentUser.getId().equals(managerId)) {
            throw new RuntimeException("You cannont view another manager's schedule");
        }

        User manager = userRepository.findById(managerId).orElseThrow(() -> new RuntimeException("Manager not found"));

        return scheduleConfigRepository.findByManager(manager)
                .stream()
                .map(shift -> WorkShiftResponse.builder()
                        .day(shift.getDayOfWeek())
                        .startingTime(shift.getStartingTime())
                        .endingTime(shift.getEndingTime())
                        .appointmentDuration(shift.getAppointmentDuration())
                        .build())
                .toList();
    }

    private List<LocalTime> generateSlots(LocalTime start, LocalTime end, Integer durationMinutes) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = start;

        while (!current.plusMinutes(durationMinutes).isAfter(end)) {
            slots.add(current);
            current = current.plusMinutes(durationMinutes);
        }

        return slots;
    }

    public List<LocalTime> getAvailableSlots(Integer managerId, LocalDate date) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        DayOfWeekEnum dayOfWeek = DayOfWeekEnum.valueOf(date.getDayOfWeek().name());
        ScheduleConfig config = scheduleConfigRepository.findByManagerAndDayOfWeek(manager, dayOfWeek).orElse(null);

        if (config == null)
            return List.of();
        List<Integer> clientIds = userRepository.findAllByManagedBy(managerId)
                .orElse(List.of())
                .stream()
                .map(User::getId)
                .toList();

        final List<LocalTime> takenTimes = (clientIds.isEmpty())
                ? Collections.emptyList()
                : appointmentRepository
                        .findAllByUserIdInAndDateAndStatusNot(clientIds, date, AppointmentStatus.CANCELLED)
                        .stream()
                        .map(app -> app.getTime().withSecond(0).withNano(0))
                        .toList();

        System.out.println("Busy times found: " + takenTimes);
        List<LocalTime> allSlots = generateSlots(
                config.getStartingTime(),
                config.getEndingTime(),
                config.getAppointmentDuration());

        System.out.println("Manager's clients: " + clientIds);

        return allSlots.stream()
                .filter(slot -> !takenTimes.contains(slot.withSecond(0).withNano(0)))
                .toList();
    }

}
