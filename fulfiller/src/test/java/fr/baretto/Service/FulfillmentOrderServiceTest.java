package fr.baretto.Service;

import fr.baretto.Entity.*;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Exception.FulfillmentOrderException;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.OrderItemRepository;
import fr.baretto.Repository.CarrierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FulfillmentOrderServiceTest {

    @Mock
    private FulfillmentOrderRepository fulfillmentOrderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private FulfillmentOrderService fulfillmentOrderService;

    private UUID orderId;
    private FulfillmentOrder order;
    private OrderItem item1;
    private OrderItem item2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        orderId = UUID.randomUUID();
        
        order = new FulfillmentOrder();
        order.setId(orderId);

        item1 = new OrderItem();
        item1.setId(UUID.randomUUID());
        item1.setFulfillmentOrder(order);
        item1.setProductId("PROD-001");
        item1.setQuantity(2);
        item1.setPrice(BigDecimal.valueOf(10.00));
        
        item2 = new OrderItem();
        item2.setId(UUID.randomUUID());
        item2.setFulfillmentOrder(order);
        item2.setProductId("PROD-002");
        item2.setQuantity(1);
        item2.setPrice(BigDecimal.valueOf(20.00));

        Shipment shipment = new Shipment();
        shipment.setId(UUID.randomUUID());
        shipment.setFulfillmentOrder(order);
        shipment.setOrderItem(item1);
        shipment.setTrackingNumber("TRACK-001");
        shipment.setTrackingUrl("http://example.com/track");
        item1.addShipment(shipment);
        Shipment shipment2 = new Shipment();
        shipment2.setId(UUID.randomUUID());
        shipment2.setFulfillmentOrder(order);
        shipment2.setOrderItem(item2);
        shipment2.setTrackingNumber("TRACK-002");
        shipment2.setTrackingUrl("http://example.com/track");
        item2.addShipment(shipment2);
        ShipmentIndicator shipmentIndicator = new ShipmentIndicator();
        shipmentIndicator.setId(UUID.randomUUID());
        shipmentIndicator.setEventType(ShipmentEventType.CREATED);
        shipmentIndicator.setEventDescription("Création de commande");
        shipmentIndicator.setCreatedAt(java.time.LocalDateTime.now());
        shipmentIndicator.setUpdatedAt(java.time.LocalDateTime.now());
        shipment.addIndicator(shipmentIndicator);
        shipment2.addIndicator(shipmentIndicator);

        order.addOrderLine(item1);
        order.addOrderLine(item2);

    }

    @Test
    void acceptOrder_ShouldChangeStatusToValidated() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenAnswer(i -> i.getArgument(0));

        FulfillmentOrder result = fulfillmentOrderService.acceptOrder(orderId);

        assertNotNull(result);
        assertEquals(FulfillmentStatus.VALIDATED, result.getStatus());
        verify(fulfillmentOrderRepository).save(any(FulfillmentOrder.class));
    }

    @Test
    void acceptOrder_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.acceptOrder(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void acceptOrder_ShouldThrowException_WhenInvalidStatus() {
        ShipmentIndicator indicator = new ShipmentIndicator();
        indicator.setEventType(ShipmentEventType.VALIDATED);
        indicator.setCreatedAt(LocalDateTime.now());
        indicator.setUpdatedAt(LocalDateTime.now());

        for (OrderItem item : order.getOrderLines()) {
            for (Shipment shipment : item.getShipment()) {
                shipment.addIndicator(indicator);
            }
        }
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.acceptOrder(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void markPrepared_ShouldChangeStatusToInPreparation() {
        order.setStatus(FulfillmentStatus.VALIDATED);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByFulfillmentOrderId(orderId)).thenReturn(Arrays.asList(item1, item2));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenAnswer(i -> i.getArgument(0));

        FulfillmentOrder result = fulfillmentOrderService.markPrepared(orderId);

        assertNotNull(result);
        assertEquals(FulfillmentStatus.IN_PREPARATION, result.getStatus());
        verify(fulfillmentOrderRepository).save(any(FulfillmentOrder.class));
    }

    @Test
    void markPrepared_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.markPrepared(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void markPrepared_ShouldThrowException_WhenInvalidStatus() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.markPrepared(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void markPrepared_ShouldThrowException_WhenNoItems() {
        order.setStatus(FulfillmentStatus.VALIDATED);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByFulfillmentOrderId(orderId)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.markPrepared(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void markReadyForDelivery_ShouldChangeStatusToInDelivery() {
        when(orderItemRepository.findByFulfillmentOrderId(orderId)).thenReturn(Arrays.asList(item1, item2));
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenAnswer(i -> i.getArgument(0));

        for (OrderItem item : order.getOrderLines()) {
            for (Shipment shipment : item.getShipment()) {
                ShipmentIndicator indicator = new ShipmentIndicator();
                indicator.setId(UUID.randomUUID());
                indicator.setEventType(ShipmentEventType.IN_PREPARATION);
                indicator.setEventDescription("Préparation terminée");
                indicator.setCreatedAt(java.time.LocalDateTime.now());
                indicator.setUpdatedAt(java.time.LocalDateTime.now());
                indicator.setShipment(shipment);
                shipment.addIndicator(indicator);
            }
        }

        FulfillmentOrder result = fulfillmentOrderService.markReadyForDelivery(orderId);

        assertNotNull(result);
        boolean hasDeliveryIndicator = result.getOrderLines().stream()
                .flatMap(item -> item.getShipment().stream())
                .flatMap(shipment -> shipment.getIndicators().stream())
                .anyMatch(indicator -> indicator.getEventType() == ShipmentEventType.IN_DELIVERY);

        assertTrue(hasDeliveryIndicator);
        verify(fulfillmentOrderRepository).save(any(FulfillmentOrder.class));
    }


    @Test
    void markReadyForDelivery_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.markReadyForDelivery(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void markReadyForDelivery_ShouldThrowException_WhenInvalidStatus() {
        order.addOrderLine(item1);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.markReadyForDelivery(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void getOrder_ShouldReturnOrder() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        FulfillmentOrder result = fulfillmentOrderService.getOrder(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(FulfillmentStatus.CREATED, result.getStatus());
    }

    @Test
    void getOrder_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.getOrder(orderId));
    }

    @Test
    void getOrderItems_ShouldReturnItems() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByFulfillmentOrderId(orderId)).thenReturn(Arrays.asList(item1, item2));

        List<OrderItem> results = fulfillmentOrderService.getOrderItems(orderId);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("PROD-001", results.get(0).getProductId());
        assertEquals("PROD-002", results.get(1).getProductId());
    }

    @Test
    void createOrder_ShouldCreateNewOrder() {
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenAnswer(i -> i.getArgument(0));

        FulfillmentOrder result = fulfillmentOrderService.createOrder(new FulfillmentOrder());

        assertNotNull(result);
        assertEquals(FulfillmentStatus.CREATED, result.getStatus());
        verify(fulfillmentOrderRepository).save(any(FulfillmentOrder.class));
    }

    @Test
    void addItem_ShouldAddItemToOrder() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenAnswer(i -> i.getArgument(0));
        when(carrierRepository.findAll()).thenReturn(Collections.singletonList(new Carrier()));

        OrderItem result = fulfillmentOrderService.addItem(orderId, "PROD-003", 1, new BigDecimal("10.00"));

        assertNotNull(result);
        assertEquals("PROD-003", result.getProductId());
        assertEquals(1, result.getQuantity());
        assertEquals(new BigDecimal("10.00"), result.getPrice());
        verify(fulfillmentOrderRepository).save(any(FulfillmentOrder.class));
    }

    @Test
    void addItem_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            fulfillmentOrderService.addItem(orderId, "PROD-003", 1, new BigDecimal("10.00")));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    void addItem_ShouldThrowException_WhenOrderInDelivery() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        for (OrderItem item : order.getOrderLines()) {
            for (Shipment shipment : item.getShipment()) {
                ShipmentIndicator indicator = new ShipmentIndicator();
                indicator.setEventType(ShipmentEventType.IN_DELIVERY);
                indicator.setCreatedAt(LocalDateTime.now());
                indicator.setUpdatedAt(LocalDateTime.now());
                indicator.setShipment(shipment);
                shipment.addIndicator(indicator);
            }
        }

        assertThrows(FulfillmentOrderException.class, () ->
                fulfillmentOrderService.addItem(orderId, "PROD-003", 1, new BigDecimal("10.00"))
        );
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }


    @Test
    void addItem_ShouldThrowException_WhenNoCarrierAvailable() {
        order.setOrderLines(new java.util.ArrayList<>());
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(carrierRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> 
            fulfillmentOrderService.addItem(orderId, "PROD-003", 1, new BigDecimal("10.00")));
    }
} 