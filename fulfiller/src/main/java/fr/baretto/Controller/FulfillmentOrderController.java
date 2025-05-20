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
@RequestMapping("/fulfillment")
@Tag(name = "Fulfillment", description = "Gestion du fulfillment des commandes")
public class FulfillmentOrderController {

    private final FulfillmentOrderService fulfillmentOrderService;

    @Autowired
    public FulfillmentOrderController(FulfillmentOrderService fulfillmentOrderService) {
        this.fulfillmentOrderService = fulfillmentOrderService;
    }

    @PostMapping("/validate")
    @Operation(
        summary = "Valider une commande",
        description = "Valide une commande en changeant son statut en VALIDATED."
    )
    public ResponseEntity<FulfillmentOrder> validateOrder(
        @RequestBody ValidateOrderRequest request) {
        FulfillmentOrder order = fulfillmentOrderService.acceptOrder(request.getOrderId());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/start")
    @Operation(
        summary = "Démarrer la préparation d'une commande",
        description = "Lance la préparation d'une commande en changeant son statut en IN_PREPARATION. La commande doit être en statut VALIDATED."
    )
    public ResponseEntity<FulfillmentOrder> startFulfillment(
        @RequestBody StartFulfillmentRequest request) {
        FulfillmentOrder order = fulfillmentOrderService.markPrepared(request.getOrderId());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/ready")
    @Operation(
        summary = "Marquer une commande comme prête pour la livraison",
        description = "Termine la préparation et passe la commande en IN_DELIVERY."
    )
    public ResponseEntity<FulfillmentOrder> readyForDelivery(
        @RequestBody ReadyForDeliveryRequest request) {
        FulfillmentOrder order = fulfillmentOrderService.markReadyForDelivery(request.getOrderId());
        return ResponseEntity.ok(order);
    }

    @PostMapping
    @Operation(
        summary = "Créer une commande de fulfillment",
        description = "Crée une nouvelle commande de fulfillment à partir d'une référence."
    )
    public ResponseEntity<FulfillmentOrder> createOrder(@RequestBody FulfillmentOrder request) {
        FulfillmentOrder order = fulfillmentOrderService.createOrder(request);
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
        summary = "Récupérer toutes les commandes",
        description = "Retourne la liste complète des commandes."
    )
    public ResponseEntity<List<FulfillmentOrder>> getAllOrders() {
        return ResponseEntity.ok(fulfillmentOrderService.getAllOrders());
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

class StartFulfillmentRequest {
    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}

class ReadyForDeliveryRequest {
    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}

class ValidateOrderRequest {
    private UUID orderId;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
} 