package fr.baretto.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private OrderItem orderItem;
    private UUID itemId;
    private String productId;
    private int quantity;
    private BigDecimal price;
    private FulfillmentOrder fulfillmentOrder;

    @BeforeEach
    void setUp() {
        itemId = UUID.randomUUID();
        productId = "TEST-PRODUCT";
        quantity = 2;
        price = new BigDecimal("19.99");
        fulfillmentOrder = new FulfillmentOrder();
        fulfillmentOrder.setId(UUID.randomUUID());

        orderItem = new OrderItem();
        orderItem.setId(itemId);
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);
        orderItem.setFulfillmentOrder(fulfillmentOrder);
    }

    @Test
    void constructor_WithProductIdAndQuantity_ShouldInitializeCorrectly() {
        OrderItem newItem = new OrderItem(productId, quantity);

        assertEquals(productId, newItem.getProductId());
        assertEquals(quantity, newItem.getQuantity());
        assertNull(newItem.getPrice());
        assertNull(newItem.getFulfillmentOrder());
    }

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Test ID
        assertEquals(itemId, orderItem.getId());
        UUID newId = UUID.randomUUID();
        orderItem.setId(newId);
        assertEquals(newId, orderItem.getId());

        // Test ProductId
        assertEquals(productId, orderItem.getProductId());
        String newProductId = "NEW-PRODUCT";
        orderItem.setProductId(newProductId);
        assertEquals(newProductId, orderItem.getProductId());

        // Test Quantity
        assertEquals(quantity, orderItem.getQuantity());
        int newQuantity = 3;
        orderItem.setQuantity(newQuantity);
        assertEquals(newQuantity, orderItem.getQuantity());

        // Test Price
        assertEquals(price, orderItem.getPrice());
        BigDecimal newPrice = new BigDecimal("29.99");
        orderItem.setPrice(newPrice);
        assertEquals(newPrice, orderItem.getPrice());

        // Test FulfillmentOrder
        assertEquals(fulfillmentOrder, orderItem.getFulfillmentOrder());
        FulfillmentOrder newOrder = new FulfillmentOrder();
        newOrder.setId(UUID.randomUUID());
        orderItem.setFulfillmentOrder(newOrder);
        assertEquals(newOrder, orderItem.getFulfillmentOrder());
    }

    @Test
    void getProductReference_ShouldReturnProductId() {
        assertEquals(productId, orderItem.getProductReference());
    }
} 