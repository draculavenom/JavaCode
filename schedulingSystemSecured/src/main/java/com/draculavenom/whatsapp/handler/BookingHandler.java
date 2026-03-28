package com.draculavenom.whatsapp.handler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.draculavenom.notification.utilities.WhatsappService;
import com.draculavenom.schedulingSystem.controller.WorkScheduleService;
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
    @Autowired private WorkScheduleService workScheduleService;

    public void handle(WhatsappSession session, String phone, String message, String buttonId){
        switch (session.getState()){

            case BOOKING_SUGGEST:
                if(buttonId == null){
                    whatsappService.sendMessage(phone, "Please use the buttons");
                    return;
                }
                if("ACCEPT_SLOT".equals(buttonId)){
                    session.setState(BotState.BOOKING_CONFIRM);
                    whatsappService.sendMessage(phone, "Confirm appointment:\n" + session.getTempDate() + " " + session.getTempTime());
                } else if ("CHANGE_SLOT".equals(buttonId)){
                    session.setState(BotState.BOOKING_DATE);
                    whatsappService.sendMessage(phone, "Enter the date (YYYY-MM-DD)");
                }
                break;
            
            case BOOKING_DATE:
                LocalDate date = LocalDate.parse(message);
                List<LocalTime> slots = workScheduleService.getAvailableSlots(session.getManagerId(), date);
                if(slots.isEmpty()){
                    whatsappService.sendMessage(phone, "No available times for that date.");
                    return;
                }
                session.setTempDate(date);
                session.setCurrentPage(0);
                int pageSizes = 8;
                int ends = Math.min(pageSizes, slots.size());
                session.setCurrentSlots(new ArrayList<>(slots.subList(0, ends)));
                whatsappService.sendAvailableSlots(phone, slots, 0);
                session.setState(BotState.BOOKING_TIME);
                break;
            
            case BOOKING_TIME:
                if(buttonId == null){
                    whatsappService.sendMessage(phone, "Please select a time from the list");
                    return;
                }

                if(buttonId.startsWith("NEXT_PAGE_") || buttonId.startsWith("PREV_PAGE_")) {
                    int page = Integer.parseInt(buttonId.split("_")[2]);
                    session.setCurrentPage(page);
                    List<LocalTime> slotsTime = workScheduleService.getAvailableSlots(session.getManagerId(), session.getTempDate());
                    int pageSize = 8;
                    int start = page * pageSize;
                    int end = Math.min(start + pageSize, slotsTime.size());
                    if(start >= slotsTime.size()) return;
                    session.setCurrentSlots(new ArrayList<>(slotsTime.subList(start, end)));
                    whatsappService.sendAvailableSlots(phone, slotsTime, page);
                    return;
                }
                
                LocalTime selectedTime = LocalTime.parse(buttonId);
                
                List<LocalTime> availableSlots = workScheduleService.getAvailableSlots(session.getManagerId(), session.getTempDate())
                    .stream()
                    .map(t -> t.withSecond(0).withNano(0))
                    .toList();
                
                if(!availableSlots.contains(selectedTime)){
                    whatsappService.sendMessage(phone, "Invalid time. Please choose an available slot");
                    return;
                }

                session.setTempTime(selectedTime);
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
                if(buttonId == null){
                    whatsappService.sendMessage(phone, "Please select and appointment from the list");
                    return;
                }
                if(buttonId.startsWith("NEXT_PAGE_") || buttonId.startsWith("PREV_PAGE_")){
                    int page = Integer.parseInt(buttonId.split("_")[2]);
                    session.setCurrentPage(page);
                    List<Appointment> appointments = appointmentManager.getCancellableAppointments(session.getUserId());
                    whatsappService.sendAppointmentList(phone, appointments, page);
                    return;
                }
                
                session.setTempAppointmentId(Integer.parseInt(buttonId));
                whatsappService.sendMessage(phone, "Please enter the reason for cancellation");
                session.setState(BotState.CANCEL_REASON);
                break;
            
            case CANCEL_REASON:
                if(buttonId != null){
                    return;
                }
                if(session.getCancelReason() != null){
                    return;
                }
                if(message == null || message.isBlank()){
                    whatsappService.sendMessage(phone, "Please enter the reason");
                    return;
                }
                session.setCancelReason(message);
                whatsappService.sendConfirmationButtons(phone);
                session.setState(BotState.CANCEL_CONFIRM);
                break;

            case CANCEL_CONFIRM:
                if(buttonId == null){
                    return;
                }
                Integer apId = session.getTempAppointmentId();
                String reason = session.getCancelReason();

                if(reason == null || reason.isBlank()){
                    whatsappService.sendMessage(phone, "Error: Comments is required when cancelling");
                    session.setState(BotState.CANCEL_REASON);
                    return;
                }

                if("CONFIRM_CANCEL".equals(buttonId)){                    
                    try{
                        appointmentManager.getAppointmentAndCancelIt(apId, reason);
                        whatsappService.sendMessage(phone, "Appointment cancelled successfully");
                    }catch(Exception e){
                        whatsappService.sendMessage(phone, "Error: " + e.getMessage());
                    }
                } else if("KEEP_APPOINTMENT".equals(buttonId)){
                    whatsappService.sendMessage(phone, "Your appointment was not cancelled");
                }
                session.setCancelReason(null);
                session.setTempAppointmentId(null);
                session.setState(BotState.MAIN_MENU);
                break;
        }
        sessionService.save(session);
    }
}
