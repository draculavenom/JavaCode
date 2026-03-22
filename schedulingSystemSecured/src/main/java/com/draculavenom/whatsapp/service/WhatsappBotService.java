package com.draculavenom.whatsapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.company.controller.CompanyNumberService;
import com.draculavenom.notification.utilities.WhatsappService;
import com.draculavenom.whatsapp.enums.BotState;
import com.draculavenom.whatsapp.handler.BookingHandler;
import com.draculavenom.whatsapp.handler.MenuHandler;
import com.draculavenom.whatsapp.handler.RegisterHandler;
import com.draculavenom.whatsapp.model.WhatsappSession;

@Service
public class WhatsappBotService {
    
    @Autowired private SessionService sessionService;
    @Autowired private MenuHandler menuhandler;
    @Autowired private BookingHandler bookingHandler;
    @Autowired private RegisterHandler registerHandler;
    @Autowired private CompanyNumberService companyNumberService;
    @Autowired private WhatsappService whatsappService;

    public void process(Map<String, Object> payload){
        
        try{
            Map entry = ((List<Map>)payload.get("entry")).get(0);
            Map changes = ((List<Map>)entry.get("changes")).get(0);
            Map value = (Map) changes.get("value");
            if(value.get("messages") == null){
                return;
            }

            List<Map> messages = (List<Map>) value.get("messages");
            Map messageObj = messages.get(0);
            String phone = (String) messageObj.get("from");
            String message = null;
            if(messageObj.get("text") != null){
                message = (String)((Map) messageObj.get("text")).get("body");
            }
            String buttonId = null;
            if(messageObj.get("interactive") != null){
                Map interactive = (Map) messageObj.get("interactive");

                if(interactive.get("button_reply") != null){
                    buttonId = (String) ((Map) interactive.get("button_reply")).get("id");
                }

                if(interactive.get("list_reply") != null){
                    buttonId = (String) ((Map) interactive.get("list_reply")).get("id");
                }
            }

            if(messageObj.get("button") != null){
                buttonId = (String) ((Map) messageObj.get("button")).get("payload");
            }
            
            WhatsappSession session = sessionService.getOrCreate(phone);

            if(session.getUserId() == null && session.getState() == BotState.START){
                whatsappService.sendMessage(phone, "Welcome! Let's create your account.\nEnter your first name:");
                session.setState(BotState.REGISTER_FIRST_NAME);
                sessionService.save(session);
                return;
            }

            String businessNumber = extractBusinessPhone(payload);
            Integer managerId = companyNumberService.getManagerIdByCompanyNumber(businessNumber);

            if(session.getManagerId() == null){
                session.setManagerId(managerId);
            }

            switch (session.getState()){
                case START:
                case MAIN_MENU:
                    menuhandler.handle(session, phone, buttonId);
                    break;
                case BOOKING_DATE:
                case BOOKING_TIME:
                case BOOKING_CONFIRM:
                    bookingHandler.handle(session, phone, message, buttonId);
                    break;
                case CANCEL_SELECT:
                case CANCEL_CONFIRM:
                    bookingHandler.handle(session, phone, message, buttonId);
                    break;
                case REGISTER_FIRST_NAME:
                case REGISTER_LAST_NAME:
                case REGISTER_DAY_BIRTH:
                case REGISTER_EMAIL:
                case REGISTER_CONFIRM:
                        registerHandler.handle(session, phone, message, buttonId);
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String extractPhone(Map<String, Object> payload){
        try{
            Map entry = ((List<Map>)payload.get("entry")).get(0);
            Map changes = ((List<Map>)entry.get("changes")).get(0);
            Map value = (Map) changes.get("value");
            List<Map> messages = (List<Map>) value.get("messages");
            if(messages == null){
                return null;
            }
            Map message = messages.get(0);
            return (String) message.get("from");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String extractBusinessPhone(Map<String, Object> payload){
        Map entry = ((List<Map>)payload.get("entry")).get(0);
        Map changes = ((List<Map>)entry.get("changes")).get(0);
        Map value = (Map) changes.get("value");
        Map metadata = (Map) value.get("metadata");
        return (String) metadata.get("display_phone_number");
    }

    public String extractMessage(Map<String, Object> payload){
        try{
            Map entry = ((List<Map>)payload.get("entry")).get(0);
            Map changes = ((List<Map>)entry.get("changes")).get(0);
            Map value = (Map) changes.get("value");
            List<Map> messages = (List<Map>) value.get("messages");
            if(messages == null){
                return null;
            }
            Map message = messages.get(0);
            if(message.get("text") == null) return null;
            return (String) ((Map) message.get("text")).get("body");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

        public String extractButtonId(Map<String, Object> payload){
        Map entry = ((List<Map>)payload.get("entry")).get(0);
        Map changes = ((List<Map>)entry.get("changes")).get(0);
        Map value = (Map) changes.get("value");
        Map message = ((List<Map>) value.get("messages")).get(0);
        if(message.containsKey("button")){
            return (String) ((Map) message.get("button")).get("payload");
        }
        return null;
    }
}
