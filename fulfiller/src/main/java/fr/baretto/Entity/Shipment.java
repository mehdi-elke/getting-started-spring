package fr.baretto.Entity;

import fr.baretto.Enumeration.FulfillmentStatus;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
public class Shipment extends TimestableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier carrier;

    @ManyToOne
    @JoinColumn(name = "fulfillment_order_id", nullable = false)
    private FulfillmentOrder fulfillmentOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Column(name = "tracking_url")
    private String trackingUrl;

    @Column(name = "estimated_price", precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(name = "shipping_price", precision = 10, scale = 2)
    private BigDecimal shippingPrice;

    @Column(length = 3)
    private String currency;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "price_estimated")
    private Double priceEstimated;

    @Column(name = "price_final")
    private Double priceFinal;

    @Column(name = "label_url")
    private String labelUrl;

    @Column(name = "carrier_code")
    private String carrierCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FulfillmentStatus status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Shipment() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }

    public BigDecimal getShippingPrice() {
        return shippingPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Double getPriceEstimated() {
        return priceEstimated;
    }

    public Double getPriceFinal() {
        return priceFinal;
    }

    public String getLabelUrl() {
        return labelUrl;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public FulfillmentStatus getStatus() {
        return status;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public FulfillmentOrder getFulfillmentOrder() {
        return fulfillmentOrder;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
        updateTimestamp();
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public void setShippingPrice(BigDecimal shippingPrice) {
        this.shippingPrice = shippingPrice;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPriceEstimated(Double priceEstimated) {
        this.priceEstimated = priceEstimated;
    }

    public void setPriceFinal(Double priceFinal) {
        this.priceFinal = priceFinal;
    }

    public void setLabelUrl(String labelUrl) {
        this.labelUrl = labelUrl;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public void setStatus(FulfillmentStatus status) {
        this.status = status;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
        updateTimestamp();
    }

    public void setFulfillmentOrder(FulfillmentOrder fulfillmentOrder) {
        this.fulfillmentOrder = fulfillmentOrder;
        updateTimestamp();
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
