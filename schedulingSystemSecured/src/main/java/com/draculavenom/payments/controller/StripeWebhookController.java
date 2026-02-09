package com.draculavenom.payments.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.draculavenom.schedulingSystem.controller.ManagerOptionsService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.EventDataDeserializer;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class StripeWebhookController {
    
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;
    
    private final StripeWebhookService webhookService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try{
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if(!dataObjectDeserializer.getObject().isPresent()) {
            log.warn("Event Stripe sin objeto expirado : {}", event.getId());
            return ResponseEntity.ok("Ignored");
        }

        Object stripeObject = dataObjectDeserializer.getObject().get();
        
        
        if("payment_intent.succeeded".equals(event.getType())){
            PaymentIntent intent = (PaymentIntent) stripeObject;
            webhookService.handlePaymentSucceeded(intent);
        }

        if("payment_intent.payment_failed".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event
                .getDataObjectDeserializer()
                .getObject()
                .orElse(null);
            
            if(intent != null) {
                webhookService.handlePaymentFailed(intent);
            }
        }

        return ResponseEntity.ok("OK");
    }
}
