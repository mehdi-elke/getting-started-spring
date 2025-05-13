package fr.baretto.Service;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        order.setStatus(FulfillmentStatus.CREATED);
        
        item1 = new OrderItem();
        item1.setId(UUID.randomUUID());
        item1.setFulfillmentOrder(order);
        item1.setProductId("PROD-001");
        item1.setQuantity(2);
        
        item2 = new OrderItem();
        item2.setId(UUID.randomUUID());
        item2.setFulfillmentOrder(order);
        item2.setProductId("PROD-002");
        item2.setQuantity(1);

        order.setItems(Arrays.asList(item1, item2));
    }

    @Test
    void acceptOrder_ShouldChangeStatusToAccepted() {
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(fulfillmentOrderRepository.save(any(FulfillmentOrder.class))).thenAnswer(i -> i.getArgument(0));

        FulfillmentOrder result = fulfillmentOrderService.acceptOrder(orderId);

        assertNotNull(result);
        assertEquals(FulfillmentStatus.ACCEPTED, result.getStatus());
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
        order.setStatus(FulfillmentStatus.ACCEPTED);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.acceptOrder(orderId));
        verify(fulfillmentOrderRepository, never()).save(any(FulfillmentOrder.class));
    }

    @Test
    void markPrepared_ShouldChangeStatusToInPreparation() {
        order.setStatus(FulfillmentStatus.ACCEPTED);
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
        order.setStatus(FulfillmentStatus.ACCEPTED);
        when(fulfillmentOrderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByFulfillmentOrderId(orderId)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> fulfillmentOrderService.markPrepared(orderId));
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
} 