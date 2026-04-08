package com.draculavenom.schedulingSystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.company.CompanyName;
import com.draculavenom.schedulingSystem.dto.AppointmentResponseDTO;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@Service
public class AppointmentService {
    
    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private UserRepository userRepository;

    public List<AppointmentResponseDTO> getAllWithCompany() {
        List<Appointment> appointments = appointmentRepository.findAll();

        Map<Integer, User> usersById = userRepository.findAll()
            .stream()
            .collect(Collectors.toMap(User::getId, u -> u));

        List<AppointmentResponseDTO> response = new ArrayList<>();

        for(Appointment appointment : appointments) {
            User user = usersById.get(appointment.getUserId());
            if(user == null) {
                continue;
            }

            User manager = appointment.getManager();
            CompanyName company = manager != null ? manager.getCompany() : null;

            if(user == null || manager == null){
                continue;
            }

            AppointmentResponseDTO dto = new AppointmentResponseDTO();
            dto.setAppointmentId(appointment.getId());
            dto.setDate(appointment.getDate());
            dto.setTime(appointment.getTime());
            dto.setStatus(appointment.getStatus());
            dto.setUserId(user.getId());
            dto.setManagerId(appointment.getManager().getId());

            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());

            dto.setCompanyName(company != null ? company.getNameCompany() : "");

            response.add(dto);
        }

        return response;

    }
}
