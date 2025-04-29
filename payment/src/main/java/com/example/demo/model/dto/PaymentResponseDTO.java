package com.example.demo.model.dto;

import com.example.demo.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private PaymentStatus status; // Utilisation de l'enum
    private String message;
}
