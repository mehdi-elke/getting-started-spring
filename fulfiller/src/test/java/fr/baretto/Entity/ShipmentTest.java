package fr.baretto.Entity;

import fr.baretto.Enumeration.FulfillmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {

    private Shipment shipment;
    private UUID shipmentId;
    private String trackingNumber;
    private Carrier carrier;
    private FulfillmentOrder order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @BeforeEach
    void setUp() {
        shipmentId = UUID.randomUUID();
        trackingNumber = "TRACK-123";
        carrier = new Carrier("TEST-CARRIER", "Test Carrier");
        order = new FulfillmentOrder();
        order.setId(UUID.randomUUID());
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        shipment = new Shipment();
        shipment.setId(shipmentId);
        shipment.setTrackingNumber(trackingNumber);
        shipment.setCarrier(carrier);
        shipment.setFulfillmentOrder(order);
        shipment.setCreatedAt(createdAt);
        shipment.setUpdatedAt(updatedAt);
    }

    @Test
    void constructor_ShouldInitializeWithDefaultValues() {
        Shipment newShipment = new Shipment();
        
        assertNotNull(newShipment.getCreatedAt());
        assertNull(newShipment.getTrackingNumber());
        assertNull(newShipment.getCarrier());
        assertNull(newShipment.getFulfillmentOrder());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Test ID
        assertEquals(shipmentId, shipment.getId());
        UUID newId = UUID.randomUUID();
        shipment.setId(newId);
        assertEquals(newId, shipment.getId());

        // Test TrackingNumber
        assertEquals(trackingNumber, shipment.getTrackingNumber());
        String newTrackingNumber = "NEW-TRACK-456";
        shipment.setTrackingNumber(newTrackingNumber);
        assertEquals(newTrackingNumber, shipment.getTrackingNumber());

        // Test Carrier
        assertEquals(carrier, shipment.getCarrier());
        Carrier newCarrier = new Carrier("NEW-CARRIER", "New Carrier");
        shipment.setCarrier(newCarrier);
        assertEquals(newCarrier, shipment.getCarrier());

        // Test FulfillmentOrder
        assertEquals(order, shipment.getFulfillmentOrder());
        FulfillmentOrder newOrder = new FulfillmentOrder();
        newOrder.setId(UUID.randomUUID());
        shipment.setFulfillmentOrder(newOrder);
        assertEquals(newOrder, shipment.getFulfillmentOrder());

        // Test CreatedAt
        assertEquals(createdAt.truncatedTo(java.time.temporal.ChronoUnit.MILLIS), 
                    shipment.getCreatedAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS));
        LocalDateTime newCreatedAt = LocalDateTime.now();
        shipment.setCreatedAt(newCreatedAt);
        assertEquals(newCreatedAt.truncatedTo(java.time.temporal.ChronoUnit.MILLIS), 
                    shipment.getCreatedAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS));

        // Test UpdatedAt
        assertEquals(updatedAt.truncatedTo(java.time.temporal.ChronoUnit.MILLIS), 
                    shipment.getUpdatedAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS));
        LocalDateTime newUpdatedAt = LocalDateTime.now();
        shipment.setUpdatedAt(newUpdatedAt);
        assertEquals(newUpdatedAt.truncatedTo(java.time.temporal.ChronoUnit.MILLIS), 
                    shipment.getUpdatedAt().truncatedTo(java.time.temporal.ChronoUnit.MILLIS));
    }

    @Test
    void updateTrackingNumber_ShouldUpdateTrackingNumberAndUpdatedAt() {
        LocalDateTime beforeUpdate = LocalDateTime.now();
        String newTrackingNumber = "UPDATED-TRACK-789";
        shipment.setTrackingNumber(newTrackingNumber);
        LocalDateTime afterUpdate = LocalDateTime.now();

        assertEquals(newTrackingNumber, shipment.getTrackingNumber());
        assertNotNull(shipment.getUpdatedAt());
        assertTrue(shipment.getUpdatedAt().isAfter(beforeUpdate) || shipment.getUpdatedAt().equals(beforeUpdate));
        assertTrue(shipment.getUpdatedAt().isBefore(afterUpdate) || shipment.getUpdatedAt().equals(afterUpdate));
    }
} 