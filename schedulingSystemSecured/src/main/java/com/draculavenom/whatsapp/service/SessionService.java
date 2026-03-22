package com.draculavenom.whatsapp.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.whatsapp.enums.BotState;
import com.draculavenom.whatsapp.model.WhatsappSession;
import com.draculavenom.whatsapp.model.WhatsappSessionRepository;

@Service
public class SessionService {
    
    @Autowired private WhatsappSessionRepository repository;
    @Autowired private UserRepository userRepository;

    private static final int SESSION_TIMEOUT_MINUTES = 30;

    public WhatsappSession getOrCreate(String phone){
        Optional<WhatsappSession> optional = repository.findById(phone);
        if(optional.isPresent()){
            WhatsappSession session = optional.get();

            Optional<User> userOpt = userRepository.findByPhoneNumber(phone);
            if(userOpt.isPresent()){
                session.setUserId(userOpt.get().getId());
            }

            if(isExpired(session)){
                return resetSession(session);
            }
            return repository.save(session);
        }

        WhatsappSession session = new WhatsappSession();
        session.setPhone(phone);
        session.setState(BotState.START);
        session.setLastUpdated(LocalDateTime.now());

        return repository.save(session);
    }

    public WhatsappSession save(WhatsappSession session){
        session.setLastUpdated(LocalDateTime.now());
        return repository.save(session);
    }

    public void reset(String phone){
        repository.findById(phone).ifPresent(this::resetSession);
    }

    private WhatsappSession resetSession(WhatsappSession session){
        session.setState(BotState.START);
        session.setTempDate(null);
        session.setTempTime(null);
        session.setLastUpdated(LocalDateTime.now());
        return repository.save(session);
    }

    private boolean isExpired(WhatsappSession session){
        return session.getLastUpdated().plusMinutes(SESSION_TIMEOUT_MINUTES).isBefore(LocalDateTime.now());
    }

}
