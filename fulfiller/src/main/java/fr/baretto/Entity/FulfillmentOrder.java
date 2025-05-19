package fr.baretto.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.baretto.Enumeration.FulfillmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "fulfillment_orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FulfillmentOrder extends TimestableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Embedded
    @NotNull
    private DeliveryAddress deliveryAddress;

    @Embedded
    @NotNull
    private Contact contact;

    @OneToMany(mappedBy = "fulfillmentOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderLines = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FulfillmentStatus status = FulfillmentStatus.CREATED;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
        updateTimestamp();
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        updateTimestamp();
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        updateTimestamp();
    }

    public List<OrderItem> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderItem> orderLines) {
        this.orderLines = orderLines;
        updateTimestamp();
    }

    public FulfillmentStatus getStatus() {
        return status;
    }

    public void setStatus(FulfillmentStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public void addOrderLine(OrderItem item) {
        orderLines.add(item);
        item.setFulfillmentOrder(this);
    }

    public void removeOrderLine(OrderItem item) {
        orderLines.remove(item);
        item.setFulfillmentOrder(null);
    }
} 