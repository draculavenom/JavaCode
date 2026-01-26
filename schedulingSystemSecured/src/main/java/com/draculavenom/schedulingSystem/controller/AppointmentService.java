package com.draculavenom.schedulingSystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

            User manager = null;
            CompanyName company = null;

            if(user.getManagedBy() != null) {
                manager = usersById.get(user.getManagedBy());
                if(manager != null) {
                    company = manager.getCompanyName();
                }
            }

            AppointmentResponseDTO dto = new AppointmentResponseDTO();
            dto.setAppointmentId(appointment.getId());
            dto.setDate(appointment.getDate());
            dto.setTime(appointment.getTime());
            dto.setStatus(appointment.getStatus());
            dto.setUserId(user.getId());

            if(manager != null) {
                dto.setManagerId(manager.getId());
                dto.setCompanyName(company.getNameCompany());
            }

            response.add(dto);
        }

        return response;

    }
}
