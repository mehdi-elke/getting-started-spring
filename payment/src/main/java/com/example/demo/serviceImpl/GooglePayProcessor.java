package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class GooglePayProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement Google Pay pour la commande: {}", paymentRequest.getOrderId());
        log.info("Paiement Google Pay réussi pour la commande: {}", paymentRequest.getOrderId());
        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        return "Échec de traitement Google Pay";
    }
}
