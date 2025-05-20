package fr.baretto.Service;

import fr.baretto.Entity.Carrier;
import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Repository.CarrierRepository;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.ShipmentIndicatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CarrierServiceTest {

    @Mock
    private CarrierRepository carrierRepository;

    @Mock
    private FulfillmentOrderRepository fulfillmentOrderRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ShipmentIndicatorRepository shipmentIndicatorRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private CarrierService carrierService;

    private UUID orderId;
    private UUID carrierId;
    private FulfillmentOrder order;
    private Carrier carrier;
    private Shipment shipment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        orderId = UUID.randomUUID();
        carrierId = UUID.randomUUID();
        
        order = new FulfillmentOrder();
        order.setId(orderId);
        order.setStatus(FulfillmentStatus.IN_PREPARATION);
        
        carrier = new Carrier();
        carrier.setId(carrierId);
        carrier.setCode("TEST-CARRIER");
        
        shipment = new Shipment();
        shipment.setId(UUID.randomUUID());
        shipment.setFulfillmentOrder(order);
        shipment.setCarrier(carrier);

        ShipmentIndicator shipmentIndicator = new ShipmentIndicator();
        shipmentIndicator.setId(UUID.randomUUID());
        shipmentIndicator.setEventType(ShipmentEventType.IN_DELIVERY);
        shipmentIndicator.setEventDescription("En cours de livraison");
        shipment.addIndicator(shipmentIndicator);
    }

    @Test
    void createShipment_ShouldCreateShipmentAndIndicator() {
        OrderItem item = new OrderItem();
        item.setProductId("PROD-TEST");
        item.setQuantity(1);
        item.setPrice(new java.math.BigDecimal("10.00"));
        order.getOrderLines().add(item);

        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(carrierRepository.findById(carrierId)).thenReturn(Optional.of(carrier));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);
        when(shipmentIndicatorRepository.save(any(ShipmentIndicator.class))).thenAnswer(i -> i.getArgument(0));

        Shipment result = carrierService.createShipment(orderId, carrierId);

        assertNotNull(result);
        assertEquals(FulfillmentStatus.IN_DELIVERY, result.getStatus());
        assertEquals(carrier, result.getCarrier());
        assertEquals(order, result.getFulfillmentOrder());
        
        verify(shipmentIndicatorRepository).save(any(ShipmentIndicator.class));
    }

    @Test
    void createShipment_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carrierService.createShipment(orderId, carrierId));
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }

    @Test
    void createShipment_ShouldThrowException_WhenCarrierNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(carrierRepository.findById(carrierId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carrierService.createShipment(orderId, carrierId));
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }

    @Test
    void createShipment_ShouldThrowException_WhenInvalidOrderStatus() {
        order.setStatus(FulfillmentStatus.VALIDATED);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> carrierService.createShipment(orderId, carrierId));
        verify(shipmentRepository, never()).save(any(Shipment.class));
    }

    @Test
    void pushTracking_ShouldCreateIndicator() {
        UUID shipmentId = UUID.randomUUID();
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenReturn(order);
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);
        when(shipmentIndicatorRepository.save(any(ShipmentIndicator.class))).thenAnswer(i -> i.getArgument(0));

        Shipment result = carrierService.pushTracking(shipmentId, ShipmentEventType.IN_TRANSIT, "Test event");

        assertNotNull(result);
        assertEquals(shipment, result);
        verify(fulfillmentOrderRepository).save(order);
        verify(shipmentRepository).save(shipment);
    }

    @Test
    void pushTracking_ShouldUpdateShipmentStatus_WhenDelivered() {
        UUID shipmentId = UUID.randomUUID();
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));
        when(shipmentRepository.save(any(Shipment.class))).thenAnswer(i -> i.getArgument(0));
        when(shipmentIndicatorRepository.save(any(ShipmentIndicator.class))).thenAnswer(i -> i.getArgument(0));

        Shipment result = carrierService.pushTracking(shipmentId, ShipmentEventType.FULFILLED, "Test event");

        assertNotNull(result);
        assertEquals(FulfillmentStatus.FULFILLED, result.getStatus());
        verify(shipmentRepository).save(shipment);
    }

    @Test
    void pushTracking_ShouldThrowException_WhenShipmentNotFound() {
        UUID shipmentId = UUID.randomUUID();
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carrierService.pushTracking(shipmentId, ShipmentEventType.IN_TRANSIT, "Test event"));
        verify(shipmentIndicatorRepository, never()).save(any(ShipmentIndicator.class));
    }
} 