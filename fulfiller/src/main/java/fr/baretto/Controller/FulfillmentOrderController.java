package fr.baretto.Controller;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Service.FulfillmentOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fulfillment/orders")
@Tag(name = "FulfillmentOrder", description = "Gestion des commandes de fulfillment")
public class FulfillmentOrderController {

    private final FulfillmentOrderService fulfillmentOrderService;

    @Autowired
    public FulfillmentOrderController(FulfillmentOrderService fulfillmentOrderService) {
        this.fulfillmentOrderService = fulfillmentOrderService;
    }

    @PostMapping
    @Operation(
        summary = "Créer une commande de fulfillment",
        description = "Crée une nouvelle commande de fulfillment à partir d'une référence."
    )
    public ResponseEntity<FulfillmentOrder> createOrder(@RequestBody CreateOrderRequest request) {
        FulfillmentOrder order = fulfillmentOrderService.createOrder(request.getOrderReference());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/prepare")
    @Operation(
        summary = "Accepter une commande de fulfillment",
        description = "Change le statut de la commande en ACCEPTED."
    )
    public ResponseEntity<FulfillmentOrder> prepareOrder(
        @Parameter(description = "UUID de la commande de fulfillment") @PathVariable UUID id) {
        FulfillmentOrder order = fulfillmentOrderService.acceptOrder(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/prepared")
    @Operation(
        summary = "Marquer une commande comme préparée",
        description = "Change le statut de la commande en IN_PREPARATION si tous les items sont disponibles."
    )
    public ResponseEntity<FulfillmentOrder> markOrderPrepared(
        @Parameter(description = "UUID de la commande de fulfillment") @PathVariable UUID id) {
        FulfillmentOrder order = fulfillmentOrderService.markPrepared(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Récupérer une commande de fulfillment",
        description = "Retourne les informations d'une commande de fulfillment par son UUID."
    )
    public ResponseEntity<FulfillmentOrder> getOrder(
        @Parameter(description = "UUID de la commande de fulfillment") @PathVariable UUID id) {
        FulfillmentOrder order = fulfillmentOrderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/items")
    @Operation(
        summary = "Ajouter un item à une commande",
        description = "Ajoute un nouvel item à une commande de fulfillment existante."
    )
    public ResponseEntity<OrderItem> addItem(
        @Parameter(description = "UUID de la commande") @PathVariable UUID id,
        @RequestBody AddItemRequest request) {
        OrderItem item = fulfillmentOrderService.addItem(
            id,
            request.getProductId(),
            request.getQuantity(),
            new BigDecimal(String.valueOf(request.getPrice()))
        );
        return ResponseEntity.ok(item);
    }
    // Récuperer les orderitems par une commande
    @GetMapping("/{id}/items")
    @Operation(
        summary = "Récupérer les items d'une commande",
        description = "Retourne les items d'une commande de fulfillment par son UUID."
    )
    public ResponseEntity<List<OrderItem>> getOrderItems(
        @Parameter(description = "UUID de la commande de fulfillment") @PathVariable UUID id) {
        List<OrderItem> items = fulfillmentOrderService.getOrderItems(id);
        return ResponseEntity.ok(items);
    }

    @GetMapping()
    @Operation(
        summary = "Récupérer toutes les commandes avec leurs données complètes",
        description = "Retourne la liste complète des commandes avec leurs items et expéditions associés."
    )
    public ResponseEntity<List<FulfillmentOrder>> getAllOrdersWithDetails() {
        return ResponseEntity.ok(fulfillmentOrderService.getAllOrders());
    }
}



class CreateOrderRequest {
    private String orderReference;

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }
}

class AddItemRequest {
    private String productId;
    private int quantity;
    private double price;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
} 