package com.draculavenom.whatsapp.handler;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.draculavenom.notification.utilities.WhatsappService;
import com.draculavenom.schedulingSystem.manager.AppointmentManager;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;
import com.draculavenom.whatsapp.enums.BotState;
import com.draculavenom.whatsapp.model.WhatsappSession;
import com.draculavenom.whatsapp.service.SessionService;

@Component
public class BookingHandler {
    
    @Autowired private AppointmentManager appointmentManager;
    @Autowired private WhatsappService whatsappService;
    @Autowired private SessionService sessionService;

    public void handle(WhatsappSession session, String phone, String message, String buttonId){
        switch (session.getState()){
            case BOOKING_DATE:
                session.setTempDate(LocalDate.parse(message));
                session.setState(BotState.BOOKING_TIME);
                whatsappService.sendMessage(phone,"Enter the time (HH:mm)");
                break;
            
            case BOOKING_TIME:
                session.setTempTime(LocalTime.parse(message));
                session.setState(BotState.BOOKING_CONFIRM);
                whatsappService.sendMessage(phone, "Confirm appointment:\n" + session.getTempDate() + " " + session.getTempTime());
                break;

            case BOOKING_CONFIRM:
                Appointment ap = new Appointment();
                ap.setUserId(session.getUserId());
                ap.setDate(session.getTempDate());
                ap.setTime(session.getTempTime());
                ap.setStatus(AppointmentStatus.SCHEDULED);

                try{
                    appointmentManager.create(ap);
                    whatsappService.sendMessage(phone, "Appointment created successfully");
                }catch(Exception e){
                    whatsappService.sendMessage(phone, "Error: " + e.getMessage());
                }

                session.setState(BotState.MAIN_MENU);
                break;

            case CANCEL_SELECT:
                session.setTempAppointmentId(Integer.parseInt(buttonId));
                if(buttonId == null){
                    whatsappService.sendMessage(phone, "Please select an appointment from the list");
                }
                whatsappService.sendMessage(phone, "Please enter the reason for cancellation");
                session.setState(BotState.CANCEL_CONFIRM);
                break;

            case CANCEL_CONFIRM:
                Integer apId = session.getTempAppointmentId();
                try{
                    appointmentManager.getAppointmentAndCancelIt(apId, message);
                    whatsappService.sendMessage(phone, "Appointment cancelled successfully");
                }catch(Exception e){
                    whatsappService.sendMessage(phone, "Error: " + e.getMessage());
                }
                session.setState(BotState.MAIN_MENU);
                break;
        }
        sessionService.save(session);
    }
}
