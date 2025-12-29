package com.draculavenom.schedulingSystem.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.draculavenom.schedulingSystem.manager.AppointmentManager;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;

@Component
public class AppointmentScheduler {
    private final AppointmentManager appointmentManager;
    private final AppointmentRepository appointmentRepository;

    public AppointmentScheduler(AppointmentManager appointmentManager, AppointmentRepository appointmentRepository) {
        this.appointmentManager = appointmentManager;
        this.appointmentRepository = appointmentRepository;
    }

    // Ejecución de la función en cada minuto para verificar el tiempo y realizar el cambio de estado
    @Scheduled(fixedRate = 60000)
    public void cancellPastAppointment() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Appointment> app = appointmentRepository.findAllByStatusIn(
            List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED)
        );

        for (Appointment appoint : app) {
            if(appoint.getDate().isBefore(today) || (appoint.getDate().isEqual(today) && appoint.getTime().isBefore(now))) {
                appointmentManager.cancelAppointment(appoint);
            }
        }
    }
}
