package com.example.demo.model.dto;

import com.example.demo.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "L'ID de commande est obligatoire")
    private UUID orderId;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal amount;

    @NotNull(message = "La méthode de paiement est obligatoire")
    private PaymentMethod paymentMethod;

    // Informations spécifiques à la carte bancaire
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    // Informations spécifiques à PayPal
    private String paypalEmail;

    // Informations spécifiques au virement bancaire
    private String iban;
    private String accountHolderName;
}
