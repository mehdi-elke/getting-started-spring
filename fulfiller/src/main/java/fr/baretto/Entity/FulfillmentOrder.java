package fr.baretto.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.baretto.Enumeration.FulfillmentStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "fulfillment_orders")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FulfillmentOrder extends TimestableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_reference", nullable = false, unique = true)
    private String orderReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FulfillmentStatus status = FulfillmentStatus.IN_PREPARATION;

    @OneToMany(mappedBy = "fulfillmentOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
        updateTimestamp();
    }

    public FulfillmentStatus getStatus() {
        return status;
    }

    public void setStatus(FulfillmentStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        updateTimestamp();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setFulfillmentOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setFulfillmentOrder(null);
    }
} 