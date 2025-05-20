package com.example.demo.service;

import com.example.demo.model.Payment;
import com.example.demo.model.PaymentMethod;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.dto.PaymentRequest;
import com.example.demo.model.dto.PaymentResponse;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.serviceImpl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentProcessorFactory paymentProcessorFactory;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment_Success() {
        PaymentRequest request = PaymentRequest.builder()
                .orderId(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        PaymentProcessor processor = mock(PaymentProcessor.class);
        when(paymentProcessorFactory.getProcessor(PaymentMethod.CREDIT_CARD)).thenReturn(processor);
        when(processor.processPayment(request)).thenReturn(true);

        Payment savedPayment = Payment.builder()
                .id(UUID.randomUUID())
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.IN_PROGRESS)
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse response = paymentService.processPayment(request);

        assertEquals(PaymentStatus.COMPLETED, response.getStatus());
        assertNotNull(response.getTransactionReference());
    }

    @Test
    void testProcessPayment_Failure() {
        PaymentRequest request = PaymentRequest.builder()
                .orderId(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        PaymentProcessor processor = mock(PaymentProcessor.class);
        when(paymentProcessorFactory.getProcessor(PaymentMethod.CREDIT_CARD)).thenReturn(processor);
        when(processor.processPayment(request)).thenReturn(false);
        when(processor.getFailureReason(request)).thenReturn("Numéro de carte invalide");

        Payment savedPayment = Payment.builder()
                .id(UUID.randomUUID())
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.IN_PROGRESS)
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse response = paymentService.processPayment(request);

        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertEquals("Paiement échoué: Numéro de carte invalide", response.getMessage());
    }
}