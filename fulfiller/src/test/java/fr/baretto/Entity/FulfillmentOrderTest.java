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
        assertEquals(FulfillmentStatus.CREATED, newOrder.getStatus());
        assertNotNull(newOrder.getOrderLines());
        assertTrue(newOrder.getOrderLines().isEmpty());
    }

    @Test
    void setStatus_ShouldUpdateStatusAndUpdatedAt() {
        LocalDateTime beforeUpdate = LocalDateTime.now();
        order.setStatus(FulfillmentStatus.VALIDATED);
        LocalDateTime afterUpdate = LocalDateTime.now();

        assertEquals(FulfillmentStatus.VALIDATED, order.getStatus());
        assertNotNull(order.getUpdatedAt());
        assertTrue(order.getUpdatedAt().isAfter(beforeUpdate) || order.getUpdatedAt().equals(beforeUpdate));
        assertTrue(order.getUpdatedAt().isBefore(afterUpdate) || order.getUpdatedAt().equals(afterUpdate));
    }

    @Test
    void setOrderLines_ShouldUpdateOrderLinesList() {
        order.setOrderLines(items);

        assertEquals(items, order.getOrderLines());
        assertEquals(1, order.getOrderLines().size());
        assertEquals("TEST-PRODUCT", order.getOrderLines().get(0).getProductId());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Test ID
        assertEquals(orderId, order.getId());
        UUID newId = UUID.randomUUID();
        order.setId(newId);
        assertEquals(newId, order.getId());

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
        order.getOrderLines().add(newItem);

        assertEquals(1, order.getOrderLines().size());
        assertEquals("NEW-PRODUCT", order.getOrderLines().get(0).getProductId());
        assertEquals(2, order.getOrderLines().get(0).getQuantity());
    }

    @Test
    void removeItem_ShouldRemoveItemFromOrder() {
        order.setOrderLines(items);
        assertEquals(1, order.getOrderLines().size());
        
        order.getOrderLines().remove(0);
        assertTrue(order.getOrderLines().isEmpty());
    }

    @Test
    void statusTransitions_ShouldWorkCorrectly() {
        // Test transition from IN_PREPARATION to VALIDATED
        order.setStatus(FulfillmentStatus.VALIDATED);
        assertEquals(FulfillmentStatus.VALIDATED, order.getStatus());

        // Test transition from VALIDATED to IN_DELIVERY
        order.setStatus(FulfillmentStatus.IN_DELIVERY);
        assertEquals(FulfillmentStatus.IN_DELIVERY, order.getStatus());

        // Test transition from IN_DELIVERY to FULFILLED
        order.setStatus(FulfillmentStatus.FULFILLED);
        assertEquals(FulfillmentStatus.FULFILLED, order.getStatus());
    }
} 