package com.example.demo.model.dto;

import com.example.demo.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PaymentRequestDTO {
    private String orderId;
    private double amount;
    private PaymentMethod paymentMethod;

}
