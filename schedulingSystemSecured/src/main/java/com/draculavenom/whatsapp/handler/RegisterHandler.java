package com.draculavenom.whatsapp.handler;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.draculavenom.notification.utilities.WhatsappService;
import com.draculavenom.usersHandler.controller.UsersManager;
import com.draculavenom.usersHandler.dto.UserInputDTO;
import com.draculavenom.whatsapp.enums.BotState;
import com.draculavenom.whatsapp.model.WhatsappSession;
import com.draculavenom.whatsapp.service.SessionService;
import com.draculavenom.whatsappConfig.model.WhatsappConfig;

@Component
public class RegisterHandler {
    
    @Autowired private UsersManager usersManager;
    @Autowired private WhatsappService whatsappService;
    @Autowired private SessionService sessionService;

    public void handle(WhatsappConfig config, WhatsappSession session, String phone, String message, String buttonId){
        switch(session.getState()){
            case REGISTER_FIRST_NAME:
                session.setTempFirstName(message);
                session.setState(BotState.REGISTER_LAST_NAME);
                whatsappService.sendMessage(config, phone, "Enter your last name:");
                break;
            
            case REGISTER_LAST_NAME:
                session.setTempLastName(message);
                session.setState(BotState.REGISTER_DAY_BIRTH);
                whatsappService.sendMessage(config, phone, "Enter your day of birth (YYYY-MM-DD)\n Example: 1998-05-12");
                break;

            case REGISTER_DAY_BIRTH:
                session.setTempDayOfBirth(LocalDate.parse(message));
                session.setState(BotState.REGISTER_EMAIL);
                whatsappService.sendMessage(config, phone, "Enter your email");
                break;

            case REGISTER_EMAIL:
                session.setTempEmail(message);
                session.setState(BotState.REGISTER_CONFIRM);
                whatsappService.sendMessage(config, phone, "Confirm registration:\n" +
                    session.getTempFirstName() + " " +
                    session.getTempLastName() + " " +
                    session.getTempEmail()
                );
                break;

            case REGISTER_CONFIRM:
                UserInputDTO dto = new UserInputDTO();
                dto.setFirstName(session.getTempFirstName());
                dto.setLastName(session.getTempLastName());
                dto.setDateOfBirth(session.getTempDayOfBirth());
                dto.setEmail(session.getTempEmail());
                dto.setPhoneNumber(phone);
                dto.setManagedBy(session.getManagerId());
                dto.setPassword("password");
                dto.setRole("USER");
                usersManager.create(dto);

                whatsappService.sendMessage(config, phone, "Account created successfully");
                session.setState(BotState.MAIN_MENU);
                break;
        }

        sessionService.save(session);
    }

}
