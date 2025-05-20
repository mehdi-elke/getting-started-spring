package fr.baretto.Controller;

import fr.baretto.Entity.OrderItem;
import fr.baretto.Service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fulfillment/order-items")
@Tag(name = "OrderItem", description = "Gestion des items de commande")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;


    @GetMapping
    @Operation(
        summary = "Lister tous les OrderItems",
        description = "Retourne la liste de tous les OrderItems enregistrés."
    )
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemService.getAllOrderItems());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Récupérer un OrderItem par son UUID",
        description = "Retourne un OrderItem à partir de son identifiant UUID."
    )
    public ResponseEntity<OrderItem> getOrderItemById(
        @Parameter(description = "UUID de l'OrderItem") @PathVariable UUID id) {
        OrderItem orderItem = orderItemService.getOrderItemById(id);
        if (orderItem != null) {
            return ResponseEntity.ok(orderItem);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(
        summary = "Créer un OrderItem",
        description = "Crée un nouvel OrderItem et le sauvegarde en base."
    )
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        return ResponseEntity.ok(orderItemService.createOrderItem(orderItem));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour un OrderItem",
        description = "Met à jour un OrderItem existant à partir de son UUID."
    )
    public ResponseEntity<OrderItem> updateOrderItem(
        @Parameter(description = "UUID de l'OrderItem") @PathVariable UUID id,
        @RequestBody OrderItem orderItem) {
        OrderItem updatedOrderItem = orderItemService.updateOrderItem(id, orderItem);
        if (updatedOrderItem != null) {
            return ResponseEntity.ok(updatedOrderItem);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprimer un OrderItem",
        description = "Supprime un OrderItem à partir de son UUID."
    )
    public ResponseEntity<Void> deleteOrderItem(
        @Parameter(description = "UUID de l'OrderItem") @PathVariable UUID id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
} 