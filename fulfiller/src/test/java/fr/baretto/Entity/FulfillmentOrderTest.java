package fr.baretto.Entity;

import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
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

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = new FulfillmentOrder();
        order.setId(orderId);

        OrderItem item = new OrderItem();
        item.setId(UUID.randomUUID());
        item.setProductId("TEST-PRODUCT");
        item.setQuantity(1);
        order.addOrderLine(item);

        Shipment shipment = new Shipment();
        shipment.setId(UUID.randomUUID());
        shipment.setTrackingNumber("TEST-TRACKING");
        shipment.setOrderItem(item);
        shipment.setFulfillmentOrder(order);
        shipment.setTrackingUrl("http://example.com/track");
        item.addShipment(shipment);

        ShipmentIndicator shipmentIndicator = new ShipmentIndicator();
        shipmentIndicator.setId(UUID.randomUUID());
        shipmentIndicator.setEventType(ShipmentEventType.CREATED);

        shipment.addIndicator(shipmentIndicator);

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
        assertEquals(1, order.getOrderLines().size());
        OrderItem newItem = new OrderItem();
        newItem.setId(UUID.randomUUID());
        newItem.setProductId("NEW-PRODUCT");
        newItem.setQuantity(2);
        order.addOrderLine(newItem);
        assertEquals(2, order.getOrderLines().size());
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

        assertEquals(1, order.getOrderLines().size());
        OrderItem newItem = new OrderItem("NEW-PRODUCT", 2);
        order.getOrderLines().add(newItem);
        assertEquals(2, order.getOrderLines().size());
        assertEquals("NEW-PRODUCT", order.getOrderLines().get(1).getProductId());
    }

    @Test
    void removeItem_ShouldRemoveItemFromOrder() {
        assertEquals(1, order.getOrderLines().size());
        
        order.getOrderLines().remove(0);
        assertTrue(order.getOrderLines().isEmpty());
    }

    @Test
    void statusTransitions_ShouldWorkCorrectly() {
        // Test transition from IN_PREPARATION to VALIDATED
        ShipmentIndicator shipmentIndicator = new ShipmentIndicator();
        shipmentIndicator.setEventType(ShipmentEventType.VALIDATED);
        shipmentIndicator.setEventDescription("Validation de la commande");
        shipmentIndicator.setCreatedAt(LocalDateTime.now());
        shipmentIndicator.setUpdatedAt(LocalDateTime.now());

        for (OrderItem orderItem : order.getOrderLines()) {
            for (Shipment shipment : orderItem.getShipment()) {
                shipment.addIndicator(shipmentIndicator);
            }
        }
        assertEquals(FulfillmentStatus.VALIDATED, order.getStatus());

        // Test transition from VALIDATED to IN_DELIVERY
        ShipmentIndicator shipmentIndicator2 = new ShipmentIndicator();
        shipmentIndicator2.setEventType(ShipmentEventType.IN_DELIVERY);
        shipmentIndicator2.setEventDescription("Validation de la commande");
        shipmentIndicator2.setCreatedAt(LocalDateTime.now());
        shipmentIndicator2.setUpdatedAt(LocalDateTime.now());

        for (OrderItem orderItem : order.getOrderLines()) {
            for (Shipment shipment : orderItem.getShipment()) {
                shipment.addIndicator(shipmentIndicator2);
            }
        }

        assertEquals(FulfillmentStatus.IN_DELIVERY, order.getStatus());

        // Test transition from IN_DELIVERY to FULFILLED
        order.setStatus(FulfillmentStatus.FULFILLED);
        assertEquals(FulfillmentStatus.FULFILLED, order.getStatus());
    }
} 