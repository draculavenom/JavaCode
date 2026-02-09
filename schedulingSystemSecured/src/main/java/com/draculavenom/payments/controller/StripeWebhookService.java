package com.draculavenom.payments.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.draculavenom.payments.model.Payment;
import com.draculavenom.payments.model.PaymentRepository;
import com.draculavenom.payments.model.PaymentStatus;
import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;
import com.stripe.model.PaymentIntent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookService {
    
    private final PaymentRepository paymentRepository;
    private final ManagerOptionsRepository managerOptionsRepository;

    public void handlePaymentSucceeded(PaymentIntent intent) {
        System.out.println("Webhook PI: = " + intent.getId() +  "metadata = " + intent.getMetadata());
        Payment paym = paymentRepository.findByPaymentIntentId(intent.getId()).orElseThrow();
        Integer managerOptionsIdStr = paym.getManagerOptionsId();

        if(managerOptionsIdStr == null) {
            log.error("PaymentIntent sin managerOptionsId: {}", intent.getId());
            return;
        }

        Integer managerOptionsId = Integer.parseInt(managerOptionsIdStr);

        paymentRepository.findByPaymentIntentId(intent.getId())
            .ifPresent(payment -> {
                if(payment.getStatus() == PaymentStatus.PAID) {
                    return;
                }

                payment.setStatus(PaymentStatus.PAID);
                paymentRepository.save(payment);

                managerOptionsRepository.findById(managerOptionsId)
                    .ifPresent(options -> {
                        LocalDate basDate = options.getActiveDate() != null ? options.getActiveDate() : LocalDate.now();

                    options.setActiveDate(basDate.plusMonths(1));
                    managerOptionsRepository.save(options);
                    });
            });
    }

    public void handlePaymentFailed(PaymentIntent intent) {
        paymentRepository.findByPaymentIntentId(intent.getId())
            .ifPresent(payment -> {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
            });
    }
}
