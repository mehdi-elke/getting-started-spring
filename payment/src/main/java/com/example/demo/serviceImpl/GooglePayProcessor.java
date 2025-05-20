package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class GooglePayProcessor implements PaymentProcessor {

    private final Random random = new Random();

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement Google Pay pour la commande: {}", paymentRequest.getOrderId());

        // Vérification basique des champs nécessaires
        if (paymentRequest.getOrderId() == null || paymentRequest.getOrderId().equals(new UUID(0L, 0L))) {
            log.warn("Identifiant de commande manquant");
            return false;
        }

        if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Montant invalide pour Google Pay: {}", paymentRequest.getAmount());
            return false;
        }



        log.info("Paiement Google Pay réussi pour la commande: {}", paymentRequest.getOrderId());
        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        // Tu peux affiner ici selon les erreurs potentielles
        if (paymentRequest.getOrderId() == null || paymentRequest.getOrderId().equals(new UUID(0L, 0L))) {
            return "ID de commande invalide";
        }


        if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Montant invalide pour Google Pay";
        }

        return "Échec de traitement Google Pay (simulation ou erreur interne)";
    }
}
