package fr.baretto.Entity;

import fr.baretto.Enumeration.ShipmentEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentIndicatorTest {

    private UUID indicatorId;
    private UUID shipmentId;
    private ShipmentIndicator indicator;
    private Shipment shipment;

    @BeforeEach
    void setUp() {
        indicatorId = UUID.randomUUID();
        shipmentId = UUID.randomUUID();
        
        shipment = new Shipment();
        shipment.setId(shipmentId);
        shipment.setTrackingNumber("TRACK-123");
        
        indicator = new ShipmentIndicator();
        indicator.setId(indicatorId);
        indicator.setShipment(shipment);
        indicator.setEventType(ShipmentEventType.CREATED);
        indicator.setEventDescription("Test event");
    }

    @Test
    void constructor_ShouldInitializeWithDefaultValues() {
        ShipmentIndicator newIndicator = new ShipmentIndicator();
        
        assertNotNull(newIndicator.getCreatedAt());
        assertNull(newIndicator.getId());
        assertNull(newIndicator.getShipment());
        assertNull(newIndicator.getEventType());
        assertNull(newIndicator.getEventDescription());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        UUID newId = UUID.randomUUID();
        String newEventDescription = "FULFILLED";
        LocalDateTime newCreatedAt = LocalDateTime.now();
        
        indicator.setId(newId);
        indicator.setEventDescription(newEventDescription);
        indicator.setEventType(ShipmentEventType.FULFILLED);
        indicator.setCreatedAt(newCreatedAt);

        assertEquals(newId, indicator.getId());
        assertEquals(ShipmentEventType.FULFILLED, indicator.getEventType());
        assertEquals(newEventDescription, indicator.getEventDescription());
        assertEquals(newCreatedAt, indicator.getCreatedAt());
    }

    @Test
    void shipment_ShouldBeUpdatedCorrectly() {
        Shipment newShipment = new Shipment();
        newShipment.setId(UUID.randomUUID());
        newShipment.setTrackingNumber("TRACK-456");
        
        indicator.setShipment(newShipment);
        
        assertEquals(newShipment, indicator.getShipment());
        assertEquals("TRACK-456", indicator.getShipment().getTrackingNumber());
    }

    @Test
    void createdAt_ShouldBeSetAutomatically() {
        ShipmentIndicator newIndicator = new ShipmentIndicator();
        
        assertNotNull(newIndicator.getCreatedAt());
        assertTrue(newIndicator.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(newIndicator.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void eventType_ShouldBeUpdatedCorrectly() {
        // Test de l'événement de livraison
        indicator.setEventType(ShipmentEventType.FULFILLED);
        assertEquals(ShipmentEventType.FULFILLED, indicator.getEventType());
    }

    @Test
    public void testDeliveryEvent() {
        ShipmentIndicator indicator = new ShipmentIndicator();
        indicator.setEventType(ShipmentEventType.FULFILLED);
        indicator.setEventDescription("Colis livré");
        assertEquals(ShipmentEventType.FULFILLED, indicator.getEventType());
        assertEquals("Colis livré", indicator.getEventDescription());
    }
} 