package fr.baretto.Service;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Exception.InvalidOrderStatusException;
import fr.baretto.Exception.OrderNotFoundException;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.OrderItemRepository;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WarehouseServiceTest {

    @Mock
    private FulfillmentOrderRepository fulfillmentOrderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    private UUID orderId;
    private FulfillmentOrder order;
    private List<OrderItem> items;
    private Shipment shipment;
    private Shipment shipment2;
    private WarehouseService warehouseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.warehouseService = new WarehouseService(fulfillmentOrderRepository, shipmentRepository, warehouseRepository);

        orderId = UUID.randomUUID();
        order = new FulfillmentOrder();
        order.setId(orderId);
        order.setStatus(FulfillmentStatus.IN_PREPARATION);

        shipment = new Shipment();
        shipment.setId(UUID.randomUUID());
        shipment.setFulfillmentOrder(order);

        ShipmentIndicator shipmentIndicator = new ShipmentIndicator();
        shipmentIndicator.setId(UUID.randomUUID());
        shipmentIndicator.setEventType(ShipmentEventType.IN_PREPARATION);
        shipmentIndicator.setEventDescription("Préparation de la commande en cours");
        shipment.addIndicator(shipmentIndicator);

        shipment2 = new Shipment();
        shipment2.setId(UUID.randomUUID());
        shipment2.setFulfillmentOrder(order);
        ShipmentIndicator shipmentIndicator2 = new ShipmentIndicator();
        shipmentIndicator2.setId(UUID.randomUUID());
        shipmentIndicator2.setEventType(ShipmentEventType.IN_DELIVERY);
        shipmentIndicator2.setEventDescription("En cours de livraison");
        shipment.addIndicator(shipmentIndicator);

        items = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(UUID.randomUUID());
        item.setProductId("TEST-PRODUCT");
        item.setQuantity(1);
        item.addShipment(shipment);
        item.addShipment(shipment2);
        items.add(item);
        order.setOrderLines(items);
    }

    @Test
    void startPreparation_ShouldChangeStatusToInPreparation() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(shipmentRepository.findByFulfillmentOrderId(orderId)).thenReturn(Arrays.asList(shipment));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenReturn(order);
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);

        order.setStatus(FulfillmentStatus.VALIDATED);

        FulfillmentOrder result = warehouseService.startPreparation(orderId);

        assertEquals(FulfillmentStatus.IN_PREPARATION, result.getStatus());
        verify(fulfillmentOrderRepository).save(order);
        verify(shipmentRepository).save(shipment);
    }

    @Test
    void startPreparation_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> warehouseService.startPreparation(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void startPreparation_ShouldThrowException_WhenInvalidStatus() {
        order.setStatus(FulfillmentStatus.IN_DELIVERY);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> warehouseService.startPreparation(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void finishPreparation_ShouldChangeStatusToInDelivery() {
        order.setStatus(FulfillmentStatus.IN_PREPARATION);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(shipmentRepository.findByFulfillmentOrderId(orderId)).thenReturn(Arrays.asList(shipment));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenReturn(order);
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);

        FulfillmentOrder result = warehouseService.finishPreparation(orderId);

        assertEquals(FulfillmentStatus.IN_DELIVERY, result.getStatus());
        verify(fulfillmentOrderRepository).save(order);
        verify(shipmentRepository).save(shipment);
    }

    @Test
    void finishPreparation_ShouldThrowException_WhenOrderNotFound() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> warehouseService.finishPreparation(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void finishPreparation_ShouldThrowException_WhenInvalidStatus() {
        order.setStatus(FulfillmentStatus.IN_DELIVERY);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> warehouseService.finishPreparation(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void getOrderItems_ShouldReturnItems() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        List<OrderItem> result = warehouseService.getOrderItems(orderId);

        assertEquals(items, result);
        verify(fulfillmentOrderRepository).findById(orderId);
    }
} 