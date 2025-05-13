package com.example.demo.service;

import com.example.demo.model.PaymentMethod;
import com.example.demo.serviceImpl.*;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Slf4j
public class PaymentProcessorFactory {

    private final CreditCardProcessor creditCardProcessor;
    private final PaypalProcessor paypalProcessor;
    private final BankTransferProcessor bankTransferProcessor;
    private final ApplePayProcessor applePayProcessor;
    private final GooglePayProcessor googlePayProcessor;

    @Autowired
    public PaymentProcessorFactory(
            CreditCardProcessor creditCardProcessor,
            PaypalProcessor paypalProcessor,
            BankTransferProcessor bankTransferProcessor,
            ApplePayProcessor applePayProcessor,
            GooglePayProcessor googlePayProcessor) {
        this.creditCardProcessor = creditCardProcessor;
        this.paypalProcessor = paypalProcessor;
        this.bankTransferProcessor = bankTransferProcessor;
        this.applePayProcessor = applePayProcessor;
        this.googlePayProcessor = googlePayProcessor;

        log.info("PaymentProcessorFactory initialisé avec {} processeurs de paiement", 5);
    }

    public PaymentProcessor getProcessor(PaymentMethod paymentMethod) {
        log.debug("Récupération du processeur pour la méthode de paiement: {}", paymentMethod);

        // Vérification si le paymentMethod est null
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Unsupported payment method: null");
        }

        // Récupération du processeur selon la méthode de paiement
        return switch (paymentMethod) {
            case CREDIT_CARD -> creditCardProcessor;
            case PAYPAL -> paypalProcessor;
            case BANK_TRANSFER -> bankTransferProcessor;
            case APPLE_PAY -> applePayProcessor;
            case GOOGLE_PAY -> googlePayProcessor;
            default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        };
    }
}
