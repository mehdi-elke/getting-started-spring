package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BankTransferProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement par virement bancaire pour la commande: {}", paymentRequest.getOrderId());

        // Vérification du nom du titulaire
        if (paymentRequest.getAccountHolderName() == null || paymentRequest.getAccountHolderName().trim().isEmpty()) {
            log.warn("Nom du titulaire du compte manquant pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // Vérification de l'IBAN
        if (paymentRequest.getIban() == null || paymentRequest.getIban().trim().isEmpty()) {
            log.warn("IBAN manquant pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // Vérification basique du format IBAN (ex. FR, DE, etc.)
        if (!paymentRequest.getIban().matches("^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$")) {
            log.warn("Format d'IBAN invalide pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        log.info("Virement bancaire réussi pour la commande: {}", paymentRequest.getOrderId());
        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        if (paymentRequest.getAccountHolderName() == null || paymentRequest.getAccountHolderName().trim().isEmpty()) {
            return "Nom du titulaire du compte manquant";
        }

        if (paymentRequest.getIban() == null || paymentRequest.getIban().trim().isEmpty()) {
            return "IBAN manquant";
        }

        if (!paymentRequest.getIban().matches("^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$")) {
            return "Format d'IBAN invalide";
        }

        return "Échec de traitement du virement bancaire";
    }
}
