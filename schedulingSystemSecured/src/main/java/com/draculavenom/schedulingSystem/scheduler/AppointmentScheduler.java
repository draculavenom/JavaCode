package com.draculavenom.schedulingSystem.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.draculavenom.notification.service.NotificationService;
import com.draculavenom.schedulingSystem.manager.AppointmentManager;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentRepository;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;

@Component
public class AppointmentScheduler {
    private final AppointmentManager appointmentManager;
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public AppointmentScheduler(AppointmentManager appointmentManager, AppointmentRepository appointmentRepository, NotificationService notificationService) {
        this.appointmentManager = appointmentManager;
        this.appointmentRepository = appointmentRepository;
        this.notificationService = notificationService;
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
                appointmentManager.CancelBySystem(appoint);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void notifyAppointmentStartingSoon() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        List<Appointment> appointments = appointmentRepository.findAllByStatusInAndDate(
            List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED), today);

        for(Appointment appoint : appointments) {
            LocalDateTime appointmenDateTime = LocalDateTime.of(appoint.getDate(), appoint.getTime());

            long minutesLeft = ChronoUnit.MINUTES.between(now, appointmenDateTime);

            if(minutesLeft >= 4 && minutesLeft <= 5) {
                notificationService.notifyAppointmentTimeManager(appoint);
                notificationService.notifyAppointmentTimeUser(appoint); 
            }
        }
    }
}
