package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class PaypalProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement PayPal pour la commande: {}", paymentRequest.getOrderId());

        // Validation de l'email PayPal
        if (paymentRequest.getPaypalEmail() == null || paymentRequest.getPaypalEmail().isEmpty()) {
            log.warn("Email PayPal manquant pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // Vérification basique du format email
        if (!paymentRequest.getPaypalEmail().contains("@")) {
            log.warn("Format d'email PayPal invalide pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        log.info("Paiement PayPal réussi pour la commande: {}", paymentRequest.getOrderId());
        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        if (paymentRequest.getPaypalEmail() == null || paymentRequest.getPaypalEmail().isEmpty()) {
            return "Email PayPal manquant";
        }

        if (!paymentRequest.getPaypalEmail().contains("@")) {
            return "Format d'email PayPal invalide";
        }

        return "Échec de traitement PayPal";
    }
}
