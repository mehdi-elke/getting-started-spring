package fr.baretto.ordermanager.dto;

import java.util.List;

public class OrderLineDTO {
    private String productId;
    private int quantity;
    private double price;
    private String productReference;
    private List<ShipmentDTO> shipment;

    // Getters
    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getProductReference() {
        return productReference;
    }

    public List<ShipmentDTO> getShipment() {
        return shipment;
    }

    // Setters
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductReference(String productReference) {
        this.productReference = productReference;
    }

    public void setShipment(List<ShipmentDTO> shipment) {
        this.shipment = shipment;
    }
}