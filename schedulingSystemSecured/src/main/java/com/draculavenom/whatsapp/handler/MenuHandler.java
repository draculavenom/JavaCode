package com.draculavenom.whatsapp.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.draculavenom.notification.utilities.WhatsappService;
import com.draculavenom.schedulingSystem.controller.WorkScheduleService;
import com.draculavenom.schedulingSystem.manager.AppointmentManager;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.whatsapp.enums.BotState;
import com.draculavenom.whatsapp.model.WhatsappSession;
import com.draculavenom.whatsapp.service.SessionService;

@Component
public class MenuHandler {
    
    @Autowired private WhatsappService whatsappService;
    @Autowired private SessionService sessionService;
    @Autowired private AppointmentManager appointmentManager;
    @Autowired private WorkScheduleService workScheduleService;

    public void handle(WhatsappSession session, String phone, String buttonId){
        if(buttonId == null){
            whatsappService.sendMainMenu(phone);
            session.setState(BotState.MAIN_MENU);
            sessionService.save(session);
            return;
        }

        switch (buttonId){
            case "BOOK":
                Optional<LocalDateTime> nextSlot = workScheduleService.getNextAvailableSlot(session.getManagerId());
                if(nextSlot.isEmpty()){
                    whatsappService.sendMessage(phone, "No available slots");
                    return;
                }
                LocalDateTime slot = nextSlot.get();
                session.setTempDate(slot.toLocalDate());
                session.setTempTime(slot.toLocalTime());
                whatsappService.sendSuggestedSlot(phone, slot);
                sessionService.save(session);
                session.setState(BotState.BOOKING_SUGGEST);
                break;
            
            case "CANCEL":
                List<Appointment> appointments = appointmentManager.getCancellableAppointments(session.getUserId());
                if(appointments.isEmpty()){
                    whatsappService.sendMessage(phone, "No appointments to cancel");
                    return;
                }
                session.setCurrentPage(0);
                whatsappService.sendAppointmentList(phone, appointments, 0);
                session.setState(BotState.CANCEL_SELECT);
                break;
            
            case "REGISTER":
                whatsappService.sendMessage(phone, "Enter your first name:");   
                session.setState(BotState.REGISTER_FIRST_NAME);
                break;
        }
        sessionService.save(session);
    }
}
