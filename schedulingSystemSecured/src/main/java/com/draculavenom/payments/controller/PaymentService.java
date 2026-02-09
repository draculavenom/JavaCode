package com.draculavenom.payments.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.draculavenom.payments.model.Payment;
import com.draculavenom.payments.model.PaymentRepository;
import com.draculavenom.payments.model.PaymentStatus;
import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;
import com.draculavenom.security.user.User;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final ManagerOptionsRepository managerOptionsRepository;
    private final PaymentRepository paymentRepository;
    
    public String createPaymentIntent(Integer managerOptionsId, User manager) throws StripeException {
        
        ManagerOptions options = managerOptionsRepository.findById(managerOptionsId)
            .orElseThrow(() -> new RuntimeException("Subscription not fund"));

        if (options.getManagerId() != manager.getId()){
            throw new RuntimeException("Unauthorized");
        }

        long aumountInCents = Math.round(options.getAmmountPaid() * 100);
        Map<String, Object> params = new HashMap<>();
        params.put("amount", aumountInCents);
        params.put("currency", "mxn");
        params.put("payment_method_types", List.of("card"));

        Map<String, String> metadata = new HashMap<>();
        metadata.put("managerId", String.valueOf(manager.getId()));
        metadata.put("managerOptionsId", String.valueOf(managerOptionsId));
        params.put("metadata", metadata);

        PaymentIntent intent = PaymentIntent.create(params);
        
        Payment payment = new Payment();
        payment.setManagerOptionsId(managerOptionsId);
        payment.setAmount(options.getAmmountPaid());
        payment.setPaymentIntentId(intent.getId());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
            
        
        paymentRepository.save(payment);

        return intent.getClientSecret();            
     
    }

    public static void savePayment(PaymentIntent intent) {
        Payment payment = new Payment();
        payment.setManagerId(Integer.parseInt(intent.getMetadata().get("managerId")));
        payment.setManagerOptionsId(Integer.parseInt(intent.getMetadata().get("managerOptionsId")));
        payment.setAmount(intent.getAmount() / 100.0);
        payment.setPaymentIntentId(intent.getId());
        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment.setCreatedAt(LocalDate.now());

        paymentRepository.save(payment);
    }

}
