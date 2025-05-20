package com.example.demo.serviceImpl;
import com.example.demo.exception.PaymentException;
import com.example.demo.model.Payment;
import com.example.demo.model.PaymentMethod;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.model.dto.PaymentResponse;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentProcessor;
import com.example.demo.service.PaymentProcessorFactory;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentProcessorFactory paymentProcessorFactory;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        try {
        log.info("Traitement du paiement pour la commande: {}", paymentRequest.getOrderId());

        // Créer une nouvelle entité de paiement avec le builder
        Payment payment = Payment.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .status(PaymentStatus.IN_PROGRESS)
                .build();

        payment = paymentRepository.save(payment);

        // Traiter le paiement avec le processeur approprié
        PaymentProcessor processor = paymentProcessorFactory.getProcessor(paymentRequest.getPaymentMethod());
        boolean isSuccessful = processor.processPayment(paymentRequest);

        // Mettre à jour le statut du paiement
        payment.setProcessedAt(LocalDateTime.now());
        String message;

        if (isSuccessful) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setTransactionReference(generateTransactionReference());
            message = "Paiement réussi";
            log.info("Paiement réussi pour la commande: {}", payment.getOrderId());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            String failureReason = processor.getFailureReason(paymentRequest);
            message = "Paiement échoué: " + failureReason;
            log.warn("Paiement échoué pour la commande: {} - Raison: {}",
                    payment.getOrderId(), failureReason);
        }

        payment = paymentRepository.save(payment);

        // Convertir en réponse
        return convertToResponse(payment, message);
        } catch (Exception e) {
            throw new PaymentException("Erreur lors du traitement du paiement: " + e.getMessage(), e);
        }
    }

    // Le reste du code reste inchangé
    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        log.debug("Récupération du paiement avec ID: {}", paymentId);
        return paymentRepository.findById(paymentId)
                .map(payment -> convertToResponse(payment, null))
                .orElseThrow(() -> new PaymentException("Paiement non trouvé avec l'ID: " + paymentId));
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrderId(UUID orderId) {
        log.debug("Récupération des paiements pour la commande: {}", orderId);
        return paymentRepository.findByOrderId(orderId).stream()
                .map(payment -> convertToResponse(payment, null))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getLatestPaymentForOrder(UUID orderId) {
        log.debug("Récupération du dernier paiement pour la commande: {}", orderId);
        return paymentRepository.findTopByOrderIdOrderByCreatedAtDesc(orderId)
                .map(payment -> convertToResponse(payment, null))
                .orElseThrow(() -> new PaymentException("Aucun paiement trouvé pour la commande: " + orderId));
    }

    @Override
    public List<PaymentMethod> getAvailablePaymentMethods() {
        log.debug("Récupération des méthodes de paiement disponibles");
        return Arrays.asList(PaymentMethod.values());
    }

    private PaymentResponse convertToResponse(Payment payment, String message) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .transactionReference(payment.getTransactionReference())
                .processedAt(payment.getProcessedAt())
                .message(message)
                .build();
    }

    private String generateTransactionReference() {
        // Génère une référence de transaction unique
        return "TRX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
