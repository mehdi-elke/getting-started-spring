package fr.baretto.Controller;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateShipmentRequest {
    private String trackingNumber;
    private String trackingUrl;
    private BigDecimal estimatedPrice;
    private BigDecimal shippingPrice;
    private String currency;
    private String labelUrl;
    private String carrierCode;
    private UUID carrier;
    private UUID fulfillmentOrder;
    
    public UUID getFulfillmentOrderId() {
        return fulfillmentOrder;
    }

    public void setFulfillmentOrderId(UUID fulfillmentOrderId) {
        this.fulfillmentOrder = fulfillmentOrderId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public BigDecimal getShippingPrice() {
        return shippingPrice;
    }

    public void setShippingPrice(BigDecimal shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLabelUrl() {
        return labelUrl;
    }

    public void setLabelUrl(String labelUrl) {
        this.labelUrl = labelUrl;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public UUID getCarrier() {
        return carrier;
    }

    public void setCarrier(UUID carrierId) {
        this.carrier = carrierId;
    }

}
