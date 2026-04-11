package com.draculavenom.whatsapp.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.whatsapp.service.WhatsappBotService;
import com.draculavenom.whatsappConfig.model.WhatsappConfig;
import com.draculavenom.whatsappConfig.model.WhatsappConfigRepository;

@RestController
@RequestMapping("/webhook")
public class WhatsappWebhookController {
    
    @Autowired private WhatsappBotService botService;
    @Autowired private WhatsappConfigRepository configRepository;

    @PostMapping("/whatsapp")
    public ResponseEntity<String> receive(@RequestBody Map<String, Object> payload){
        botService.process(payload);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/whatsapp")
    public ResponseEntity<String> verify(
        @RequestParam("hub.mode") String mode,
        @RequestParam("hub.verify_token") String token,
        @RequestParam("hub.challenge") String challenge
    ){        
        Optional<WhatsappConfig> configOpt = configRepository.findByVerifyToken(token);
        if(configOpt.isPresent() && "subscribe".equals(mode)){
            return ResponseEntity.ok(challenge);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
