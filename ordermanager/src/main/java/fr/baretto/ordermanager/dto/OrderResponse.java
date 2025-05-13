package fr.baretto.ordermanager.dto;

import fr.baretto.ordermanager.model.OrderStatus;
import java.util.Date;

public class OrderResponse {
    private String orderId;
    private OrderStatus status;
    private Date creationDate;
    private String message;

    public OrderResponse() {}

    public OrderResponse(String orderId, OrderStatus status, Date creationDate) {
        this.orderId = orderId;
        this.status = status;
        this.creationDate = creationDate;
    }

    public OrderResponse(String message) {
        this.message = message;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}