package com.draculavenom.schedulingSystem.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.draculavenom.schedulingSystem.model.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {
    private Integer appointmentId;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private String comment;

    private Integer userId;

    private Integer managerId;
    private String companyName;
    private String firstName;
    private String lastName;
}
