package fr.baretto.ordermanager.dto;


import fr.baretto.ordermanager.controller.PaymentMethod;

public class PaymentRequest {
    private String orderId;
    private Double amount;
    private PaymentMethod paymentMethod;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}