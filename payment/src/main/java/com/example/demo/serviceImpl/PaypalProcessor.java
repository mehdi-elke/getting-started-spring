package com.example.demo.serviceImpl;

import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaypalProcessor implements PaymentProcessor {

    @Override
    public boolean processPayment(PaymentRequest paymentRequest) {
        log.debug("Traitement du paiement PayPal pour la commande: {}", paymentRequest.getOrderId());

        String email = paymentRequest.getPaypalEmail();

        // Vérification si l'email est null ou vide
        if (email == null || email.trim().isEmpty()) {
            log.warn("Email PayPal manquant pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }

        // Vérification du format d'email avec regex
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            log.warn("Format d'email PayPal invalide pour la commande: {}", paymentRequest.getOrderId());
            return false;
        }



        log.info("Paiement PayPal réussi pour la commande: {}", paymentRequest.getOrderId());
        return true;
    }

    @Override
    public String getFailureReason(PaymentRequest paymentRequest) {
        String email = paymentRequest.getPaypalEmail();

        if (email == null || email.trim().isEmpty()) {
            return "Email PayPal manquant";
        }

        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return "Format d'email PayPal invalide";
        }


        return "Échec de traitement PayPal";
    }
}
