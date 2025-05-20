package com.example.demo.service;

import com.example.demo.model.dto.PaymentRequest;

public interface PaymentProcessor {

    /**
     * Traite un paiement
     * @param paymentRequest les détails du paiement
     * @return true si le paiement est réussi, false sinon
     */
    boolean processPayment(PaymentRequest paymentRequest);

    /**
     * Obtient la raison de l'échec du paiement
     * @param paymentRequest les détails du paiement
     * @return la raison de l'échec
     */
    default String getFailureReason(PaymentRequest paymentRequest) {
        return "Échec de traitement (raison inconnue)";
    }

}
