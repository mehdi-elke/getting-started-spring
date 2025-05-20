package fr.baretto.ordermanager.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class OrderRequest {

    private UUID orderId;

    private String customerId;
    private DeliveryAddress deliveryAddress;
    private String email;
    private String phoneNumber;
    private Date creationDate;
    private List<OrderLineDTO> orderDetails;
    private String orderTracking;
    private String paymentMethod;

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    // Getters et Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public List<OrderLineDTO> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(
            List<OrderLineDTO> orderDetails) { this.orderDetails = orderDetails; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getOrderTracking() {
        return orderTracking;
    }

    public void setOrderTracking(String orderTracking) {
        this.orderTracking = orderTracking;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddressString() {
        return deliveryAddress.getStreet() + ", " +
                deliveryAddress.getCity() + ", " +
                deliveryAddress.getPostalCode() + ", " +
                deliveryAddress.getCountry();
    }

    public String getOrderDetailToString() {
        StringBuilder details = new StringBuilder();
        for (OrderLineDTO orderLine : orderDetails) {
            details.append(orderLine.getProductReference())
                    .append(" (")
                    .append(orderLine.getQuantity())
                    .append("), ");
        }
        return details.toString();
    }

    public String getZone() {
        return deliveryAddress.getZone();
    }

}