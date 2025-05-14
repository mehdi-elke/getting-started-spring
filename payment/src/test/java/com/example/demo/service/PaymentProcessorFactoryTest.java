package com.example.demo.service;

import com.example.demo.model.PaymentMethod;
import com.example.demo.serviceImpl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentProcessorFactoryTest {

    private PaymentProcessorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new PaymentProcessorFactory(
                new CreditCardProcessor(),
                new PaypalProcessor(),
                new BankTransferProcessor(),
                new ApplePayProcessor(),
                new GooglePayProcessor()
        );
    }

    @Test
    void testGetProcessor() {
        assertTrue(factory.getProcessor(PaymentMethod.CREDIT_CARD) instanceof CreditCardProcessor);
        assertTrue(factory.getProcessor(PaymentMethod.PAYPAL) instanceof PaypalProcessor);
        assertTrue(factory.getProcessor(PaymentMethod.BANK_TRANSFER) instanceof BankTransferProcessor);
        assertTrue(factory.getProcessor(PaymentMethod.APPLE_PAY) instanceof ApplePayProcessor);
        assertTrue(factory.getProcessor(PaymentMethod.GOOGLE_PAY) instanceof GooglePayProcessor);
    }

    @Test
    void testGetProcessor_InvalidMethod() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            factory.getProcessor(null);
        });
        assertEquals("Unsupported payment method: null", exception.getMessage());
    }
}
