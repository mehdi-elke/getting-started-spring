package com.example.demo.service;


import com.example.demo.model.PaymentMethod;
import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.model.dto.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    /**
     * Traite une demande de paiement
     * @param paymentRequest les détails du paiement
     * @return la réponse du paiement
     */

    PaymentResponse processPayment(PaymentRequest paymentRequest);

    /**
     * Récupère un paiement par son ID
     * @param paymentId l'ID du paiement
     * @return les détails du paiement
     */
    PaymentResponse getPaymentById(UUID paymentId);

    /**
     * Récupère tous les paiements pour une commande
     * @param orderId l'ID de la commande
     * @return la liste des paiements
     */
    List<PaymentResponse> getPaymentsByOrderId(UUID orderId);

    /**
     * Récupère le dernier paiement pour une commande
     * @param orderId l'ID de la commande
     * @return le dernier paiement
     */
    PaymentResponse getLatestPaymentForOrder(UUID orderId);

    /**
     * Récupère les méthodes de paiement disponibles
     * @return la liste des méthodes de paiement
     */
    List<PaymentMethod> getAvailablePaymentMethods();
}

