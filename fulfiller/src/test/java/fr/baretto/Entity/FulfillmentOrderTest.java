package fr.baretto.Entity;

import fr.baretto.Enumeration.FulfillmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FulfillmentOrderTest {

    private FulfillmentOrder order;
    private UUID orderId;
    private String orderReference;
    private List<OrderItem> items;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        orderReference = "TEST-ORDER-REF";
        order = new FulfillmentOrder();
        order.setId(orderId);
        order.setOrderReference(orderReference);
        
        items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(UUID.randomUUID());
        item.setProductId("TEST-PRODUCT");
        item.setQuantity(1);
        items.add(item);
    }

    @Test
    void constructor_ShouldInitializeWithDefaultValues() {
        FulfillmentOrder newOrder = new FulfillmentOrder();
        
        assertNotNull(newOrder.getCreatedAt());
        assertEquals(FulfillmentStatus.IN_PREPARATION, newOrder.getStatus());
        assertNotNull(newOrder.getItems());
        assertTrue(newOrder.getItems().isEmpty());
    }

    @Test
    void setStatus_ShouldUpdateStatusAndUpdatedAt() {
        LocalDateTime beforeUpdate = LocalDateTime.now();
        order.setStatus(FulfillmentStatus.ACCEPTED);
        LocalDateTime afterUpdate = LocalDateTime.now();

        assertEquals(FulfillmentStatus.ACCEPTED, order.getStatus());
        assertNotNull(order.getUpdatedAt());
        assertTrue(order.getUpdatedAt().isAfter(beforeUpdate) || order.getUpdatedAt().equals(beforeUpdate));
        assertTrue(order.getUpdatedAt().isBefore(afterUpdate) || order.getUpdatedAt().equals(afterUpdate));
    }

    @Test
    void setItems_ShouldUpdateItemsList() {
        order.setItems(items);

        assertEquals(items, order.getItems());
        assertEquals(1, order.getItems().size());
        assertEquals("TEST-PRODUCT", order.getItems().get(0).getProductId());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Test ID
        assertEquals(orderId, order.getId());
        UUID newId = UUID.randomUUID();
        order.setId(newId);
        assertEquals(newId, order.getId());

        // Test OrderReference
        assertEquals(orderReference, order.getOrderReference());
        String newReference = "NEW-REF";
        order.setOrderReference(newReference);
        assertEquals(newReference, order.getOrderReference());

        // Test CreatedAt
        LocalDateTime createdAt = LocalDateTime.now();
        order.setCreatedAt(createdAt);
        assertEquals(createdAt, order.getCreatedAt());

        // Test UpdatedAt
        LocalDateTime updatedAt = LocalDateTime.now();
        order.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, order.getUpdatedAt());
    }

    @Test
    void addItem_ShouldAddItemToOrder() {
        OrderItem newItem = new OrderItem("NEW-PRODUCT", 2);
        order.getItems().add(newItem);

        assertEquals(1, order.getItems().size());
        assertEquals("NEW-PRODUCT", order.getItems().get(0).getProductId());
        assertEquals(2, order.getItems().get(0).getQuantity());
    }

    @Test
    void removeItem_ShouldRemoveItemFromOrder() {
        order.setItems(items);
        assertEquals(1, order.getItems().size());
        
        order.getItems().remove(0);
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void statusTransitions_ShouldWorkCorrectly() {
        // Test transition from IN_PREPARATION to ACCEPTED
        order.setStatus(FulfillmentStatus.IN_PREPARATION);
        order.setStatus(FulfillmentStatus.ACCEPTED);
        assertEquals(FulfillmentStatus.ACCEPTED, order.getStatus());

        // Test transition from ACCEPTED to IN_DELIVERY
        order.setStatus(FulfillmentStatus.IN_DELIVERY);
        assertEquals(FulfillmentStatus.IN_DELIVERY, order.getStatus());

        // Test transition from IN_DELIVERY to IN_DELIVERY
        order.setStatus(FulfillmentStatus.IN_DELIVERY);
        assertEquals(FulfillmentStatus.IN_DELIVERY, order.getStatus());

        // Test transition from IN_DELIVERY to DELIVERED
        order.setStatus(FulfillmentStatus.DELIVERED);
        assertEquals(FulfillmentStatus.DELIVERED, order.getStatus());
    }
} 