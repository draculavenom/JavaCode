package com.draculavenom.payments.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.payments.dto.PaymentDTO;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    @Autowired private PaymentService paymentService;

    @PostMapping
    //@PreAuthorize("hasAuthority('manager:create')")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO paymentDTO, @AuthenticationPrincipal User user) throws StripeException{
        System.out.println("RAW BODY = " + paymentDTO);
        System.out.println("User = " + user.getId());

        System.out.println("DTO recibido -> managerOptionsId = " + paymentDTO.getManagerOptionsId());

        if(paymentDTO == null) {
            System.out.println("PaymentDTO is NULL");
        }else{
            System.out.println("managerOptionsId = " + paymentDTO.getManagerOptionsId());
        }

        String clientSecret = paymentService.createPaymentIntent(paymentDTO.getManagerOptionsId(), user);

        return ResponseEntity.ok(Map.of("clientSecret", clientSecret)); 
    }

}
