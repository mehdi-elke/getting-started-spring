package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
@Slf4j
public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement par carte de crédit pour la commande: {}", paymentRequest.getOrderId());

        // Validation des données de la carte
        if (paymentRequest.getCardNumber() == null || paymentRequest.getCardNumber().isEmpty()) {
            log.warn("Numéro de carte manquant pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        if (paymentRequest.getCvv() == null || paymentRequest.getCvv().isEmpty()) {
            log.warn("CVV manquant pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        if (paymentRequest.getExpiryDate() == null || paymentRequest.getExpiryDate().isEmpty()) {
            log.warn("Date d'expiration manquante pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }


        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        if (paymentRequest.getCardNumber() == null || paymentRequest.getCardNumber().isEmpty()) {
            return "Numéro de carte manquant";
        }

        if (paymentRequest.getCvv() == null || paymentRequest.getCvv().isEmpty()) {
            return "CVV manquant";
        }

        if (paymentRequest.getExpiryDate() == null || paymentRequest.getExpiryDate().isEmpty()) {
            return "Date d'expiration manquante";
        }

        if (paymentRequest.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            return "Montant trop élevé (limite: 10000)";
        }

        return "Échec de traitement par la banque";
    }
}
