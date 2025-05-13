package com.example.demo.model;

public enum PaymentStatus {
    PENDING,       // Paiement en attente (pas encore commencé)
    IN_PROGRESS,   // Paiement en cours de traitement
    COMPLETED,     // Paiement réussi et terminé
    FAILED,        // Paiement échoué (ex : fonds insuffisants, erreur technique)
    REFUNDED       // Paiement remboursé à l'utilisateur
}

