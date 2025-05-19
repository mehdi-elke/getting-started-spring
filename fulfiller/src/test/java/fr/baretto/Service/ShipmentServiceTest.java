package fr.baretto.Service;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.ShipmentIndicatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentIndicatorRepository shipmentIndicatorRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    private UUID shipmentId;
    private Shipment shipment;
    private ShipmentIndicator indicator1;
    private ShipmentIndicator indicator2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        FulfillmentOrder fulfillmentOrder = new FulfillmentOrder();
        fulfillmentOrder.setId(UUID.randomUUID());
        fulfillmentOrder.setStatus(FulfillmentStatus.IN_DELIVERY);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(UUID.randomUUID());
        orderItem.setProductId("TEST-PRODUCT");
        orderItem.setQuantity(1);
        fulfillmentOrder.addOrderLine(orderItem);
        orderItem.setFulfillmentOrder(fulfillmentOrder);

        shipmentId = UUID.randomUUID();
        
        shipment = new Shipment();
        shipment.setId(shipmentId);
        shipment.setFulfillmentOrder(null);
        ShipmentIndicator shipmentIndicator = new ShipmentIndicator();
        shipmentIndicator.setId(UUID.randomUUID());
        shipmentIndicator.setEventType(ShipmentEventType.IN_DELIVERY);
        shipmentIndicator.setEventDescription("En cours de livraison");
        shipment.setFulfillmentOrder(fulfillmentOrder);
        shipment.addIndicator(shipmentIndicator);
        shipment.setOrderItem(orderItem);
    }

    @Test
    void getShipmentHistory_ShouldReturnIndicators() {
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));
        when(shipmentIndicatorRepository.findByShipmentId(shipmentId))
            .thenReturn(Arrays.asList(indicator1, indicator2));

        List<ShipmentIndicator> results = shipmentService.getShipmentHistory(shipmentId);

        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void getShipmentHistory_ShouldThrowException_WhenShipmentNotFound() {
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> shipmentService.getShipmentHistory(shipmentId));
    }

    @Test
    void getShipment_ShouldReturnShipment() {
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));

        Shipment result = shipmentService.getShipment(shipmentId);

        assertNotNull(result);
        assertEquals(shipmentId, result.getId());
        assertEquals(FulfillmentStatus.IN_DELIVERY, result.getStatus());
    }

    @Test
    void getShipment_ShouldThrowException_WhenShipmentNotFound() {
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> shipmentService.getShipment(shipmentId));
    }

    @Test
    void updateTrackingNumber_ShouldUpdateShipment() {
        String newTrackingNumber = "TRACK-456";
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));
        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(i -> i.getArgument(0));

        Shipment result = shipmentService.updateTrackingNumber(shipmentId, newTrackingNumber);

        assertNotNull(result);
        assertEquals(newTrackingNumber, result.getTrackingNumber());
        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    void updateTrackingNumber_ShouldThrowException_WhenShipmentNotFound() {
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            shipmentService.updateTrackingNumber(shipmentId, "TRACK-456"));
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }
} 