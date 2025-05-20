package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class CreditCardProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement par carte de crédit pour la commande: {}", paymentRequest.getOrderId());

        // ✅ Vérifie que le numéro de carte est bien présent et a un format valide (13 à 19 chiffres)
        if (paymentRequest.getCardNumber() == null || !paymentRequest.getCardNumber().matches("^\\d{13,19}$")) {
            log.warn("Numéro de carte invalide pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // ✅ Vérifie que le CVV est composé de 3 ou 4 chiffres
        if (paymentRequest.getCvv() == null || !paymentRequest.getCvv().matches("^\\d{3,4}$")) {
            log.warn("CVV invalide pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // ✅ Vérifie que la date d'expiration est renseignée
        if (paymentRequest.getExpiryDate() == null || paymentRequest.getExpiryDate().isEmpty()) {
            log.warn("Date d'expiration manquante pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // ✅ Vérifie que la date d’expiration est valide (pas expirée)
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(paymentRequest.getExpiryDate(), formatter);
            if (expiry.isBefore(YearMonth.now())) {
                log.warn("Carte expirée pour la commande: {}", paymentRequest.getOrderId());
                return false;
            }
        } catch (Exception e) {
            log.warn("Format de date d'expiration invalide: {}", paymentRequest.getExpiryDate());
            return false;
        }

        // ✅ Vérifie que le montant est positif
        if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Montant invalide pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // ✅ Simulation d’un paiement réussi
        log.info("Paiement par carte réussi pour la commande: {}", paymentRequest.getOrderId());
        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        // ❌ Numéro de carte
        if (paymentRequest.getCardNumber() == null || !paymentRequest.getCardNumber().matches("^\\d{13,19}$")) {
            return "Numéro de carte invalide";
        }

        // ❌ CVV
        if (paymentRequest.getCvv() == null || !paymentRequest.getCvv().matches("^\\d{3,4}$")) {
            return "CVV invalide";
        }

        // ❌ Date d'expiration vide
        if (paymentRequest.getExpiryDate() == null || paymentRequest.getExpiryDate().isEmpty()) {
            return "Date d'expiration manquante";
        }

        // ❌ Carte expirée
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(paymentRequest.getExpiryDate(), formatter);
            if (expiry.isBefore(YearMonth.now())) {
                return "Carte expirée";
            }
        } catch (Exception e) {
            return "Format de date d'expiration invalide";
        }

        // ❌ Montant invalide
        if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Montant invalide";
        }

        // ✅ Si tout est valide, mais échec du traitement externe (ex. banque)
        return "Échec de traitement par la banque";
    }
}
