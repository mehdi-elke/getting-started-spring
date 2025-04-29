package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String orderId;
    private double amount;

    @Enumerated(EnumType.STRING) // Stocke l'enum sous forme de chaîne dans la base de données
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING) // Stocke l'enum sous forme de chaîne dans la base de données
    private PaymentStatus status;
}
