package com.draculavenom.whatsapp.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.company.controller.CompanyNumberService;
import com.draculavenom.managementModules.controller.ManagementModulesService;
import com.draculavenom.managementModules.model.ManagementModules;
import com.draculavenom.notification.utilities.WhatsappService;
import com.draculavenom.security.user.User;
import com.draculavenom.whatsapp.enums.BotState;
import com.draculavenom.whatsapp.handler.BookingHandler;
import com.draculavenom.whatsapp.handler.MenuHandler;
import com.draculavenom.whatsapp.handler.RegisterHandler;
import com.draculavenom.whatsapp.model.WhatsappSession;
import com.draculavenom.whatsappConfig.model.WhatsappConfig;
import com.draculavenom.whatsappConfig.service.WhatsappConfigService;

@Service
public class WhatsappBotService {
    
    @Autowired private SessionService sessionService;
    @Autowired private MenuHandler menuhandler;
    @Autowired private BookingHandler bookingHandler;
    @Autowired private RegisterHandler registerHandler;
    @Autowired private CompanyNumberService companyNumberService;
    @Autowired private WhatsappService whatsappService;
    @Autowired private WhatsappConfigService whatsappConfigService;
    @Autowired private ManagementModulesService managementModulesService;

    public void process(Map<String, Object> payload){
        
        try{
            Map entry = ((List<Map>)payload.get("entry")).get(0);
            Map changes = ((List<Map>)entry.get("changes")).get(0);
            Map value = (Map) changes.get("value");

            String phoneNumberId = extractPhoneNumberId(payload);
            if(phoneNumberId == null){
                return;
            }

            WhatsappConfig config = whatsappConfigService.getByPhoneNumberId(phoneNumberId);
            if(config == null || !config.isActive()){
                return;
            }

            User manager = config.getCompany().getUser();
            ManagementModules modules = managementModulesService.getByManager(manager.getId());
            if(modules == null || !modules.isWhatsappNotification()){
                return;
            }

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
                whatsappService.sendMessage(config, phone, "Welcome! Let's create your account.\nEnter your first name:");
                session.setState(BotState.REGISTER_FIRST_NAME);
                sessionService.save(session);
                return;
            }

            Integer managerId = manager.getId();
            if(session.getManagerId() == null){
                session.setManagerId(managerId);
            }

            switch (session.getState()){
                case START:
                case MAIN_MENU:
                    menuhandler.handle(config, session, phone, buttonId);
                    break;
                
                case BOOKING_SUGGEST:
                case BOOKING_DATE:
                case BOOKING_TIME:
                case BOOKING_CONFIRM:
                    bookingHandler.handle(config, session, phone, message, buttonId);
                    break;
                case CANCEL_SELECT:
                case CANCEL_REASON:
                case CANCEL_CONFIRM:
                    bookingHandler.handle(config, session, phone, message, buttonId);
                    break;
                case REGISTER_FIRST_NAME:
                case REGISTER_LAST_NAME:
                case REGISTER_DAY_BIRTH:
                case REGISTER_EMAIL:
                case REGISTER_CONFIRM:
                        registerHandler.handle(config, session, phone, message, buttonId);
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

    public String extractPhoneNumberId(Map<String, Object> payload){
        try{
            Map entry = ((List<Map>)payload.get("entry")).get(0);
            Map changes = ((List<Map>)entry.get("changes")).get(0);
            Map value = (Map) changes.get("value");
            Map metadata = (Map) value.get("metadata");
            return (String) metadata.get("phone_number_id");
        }catch(Exception e){
            return null;
        }
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
