package com.draculavenom.whatsapp.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.draculavenom.notification.utilities.WhatsappService;
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

    public void handle(WhatsappSession session, String phone, String buttonId){
        if(buttonId == null){
            whatsappService.sendMainMenu(phone);
            session.setState(BotState.MAIN_MENU);
            sessionService.save(session);
            return;
        }

        switch (buttonId){
            case "BOOK":
                whatsappService.sendMessage(phone, "Enter the date (YYYY-MM-DD)");
                session.setState(BotState.BOOKING_DATE);
                break;
            
            case "CANCEL":
                List<Appointment> appointments = appointmentManager.getCancellableAppointments(session.getUserId());
                if(appointments.isEmpty()){
                    whatsappService.sendMessage(phone, "No appointments to cancel");
                    return;
                }
                whatsappService.sendAppointmentList(phone, appointments);
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
