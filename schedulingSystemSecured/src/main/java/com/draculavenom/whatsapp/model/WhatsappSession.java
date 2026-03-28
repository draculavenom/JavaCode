package com.draculavenom.whatsapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.whatsapp.enums.BotState;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class WhatsappSession {
    @Id 
    private String phone;
    @Enumerated(EnumType.STRING)
    private BotState state;
    private LocalDate tempDate;
    private LocalTime tempTime;
    private Integer userId;
    private LocalDateTime lastUpdated;
    private Integer tempAppointmentId;
    private String cancelReason;
    private Integer managerId;
    private String tempFirstName;
    private String tempLastName;
    private LocalDate tempDayOfBirth;
    private String tempEmail;
    private int currentPage;
    private List<LocalTime> currentSlots;

    public WhatsappSession(){        
    }

    public WhatsappSession(String phone, BotState state){
        this.phone = phone;
        this.state = state;
    }
}
