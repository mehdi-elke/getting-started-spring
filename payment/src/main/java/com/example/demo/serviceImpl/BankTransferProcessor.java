package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class BankTransferProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement par virement bancaire pour la commande: {}", paymentRequest.getOrderId());
        log.info("Virement bancaire réussi pour la commande: {}", paymentRequest.getOrderId());
        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        return "Échec de traitement du virement bancaire";
    }
}
