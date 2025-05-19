package fr.baretto.Controller;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Entity.Warehouse;
import fr.baretto.Service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fulfillment/warehouses")
@Tag(name = "Warehouse", description = "Gestion des entrepôts et de la préparation des commandes")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("/orders/{orderId}/start-preparation")
    @Operation(
        summary = "Démarrer la préparation d'une commande",
        description = "Change le statut de la commande de fulfillment en ACCEPTED."
    )
    public ResponseEntity<FulfillmentOrder> startPreparation(
        @Parameter(description = "UUID de la commande de fulfillment") @PathVariable UUID orderId) {
        FulfillmentOrder order = warehouseService.startPreparation(orderId);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/orders/{orderId}/finish-preparation")
    @Operation(
        summary = "Terminer la préparation d'une commande",
        description = "Change le statut de la commande de fulfillment en IN_DELIVERY."
    )
    public ResponseEntity<FulfillmentOrder> finishPreparation(
        @Parameter(description = "UUID de la commande de fulfillment") @PathVariable UUID orderId) {
        FulfillmentOrder order = warehouseService.finishPreparation(orderId);
        return ResponseEntity.ok(order);
    }


    @GetMapping()
    @Operation(
        summary = "Lister les entrepôts",
        description = "Retourne la liste des entrepôts."
    )
    public ResponseEntity<List<Warehouse>> getWarehouses() {
        List<Warehouse> warehouses = warehouseService.getWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Récupérer un entrepôt",
        description = "Retourne un entrepôt à partir de son identifiant UUID."
    )
    public ResponseEntity<Warehouse> getWarehouseById(
        @Parameter(description = "UUID de l'entrepôt") @PathVariable UUID id) {
        Warehouse warehouse = warehouseService.getWarehouseById(id);
        if (warehouse != null) {
            return ResponseEntity.ok(warehouse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/code/{code}")
    @Operation(
        summary = "Récupérer un entrepôt par son code",
        description = "Retourne un entrepôt à partir de son code."
    )
    public ResponseEntity<Warehouse> getWarehouseByCode(
        @Parameter(description = "Code de l'entrepôt") @PathVariable String code) {
        Warehouse warehouse = warehouseService.getWarehouseByCode(code);
        if (warehouse != null) {
            return ResponseEntity.ok(warehouse);
        }
        return ResponseEntity.notFound().build();
    }
} 