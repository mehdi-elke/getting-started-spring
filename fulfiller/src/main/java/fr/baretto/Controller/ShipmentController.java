package fr.baretto.Controller;

import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Entity.Shipment;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Service.CarrierService;
import fr.baretto.Service.ShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shipment")
@Tag(name = "Shipment", description = "Gestion des expéditions et du suivi des colis")
public class ShipmentController {

    private final CarrierService carrierService;
    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(CarrierService carrierService, ShipmentService shipmentService) {
        this.carrierService = carrierService;
        this.shipmentService = shipmentService;
    }

    @PostMapping("/start")
    @Operation(
        summary = "Démarrer l'expédition",
        description = "Marque un Shipment comme IN_TRANSIT."
    )
    public ResponseEntity<Shipment> startShipment(
        @RequestBody StartShipmentRequest request) {
        Shipment shipment = carrierService.pushTracking(
            request.getShipmentId(),
            ShipmentEventType.IN_TRANSIT,
            "Shipment started"
        );
        return ResponseEntity.ok(shipment);
    }

    @PostMapping("/delivered")
    @Operation(
        summary = "Marquer une expédition comme livrée",
        description = "Marque un Shipment comme FULFILLED."
    )
    public ResponseEntity<Shipment> markAsDelivered(
        @RequestBody DeliveredShipmentRequest request) {
        Shipment shipment = carrierService.pushTracking(
            request.getShipmentId(),
            ShipmentEventType.FULFILLED,
            "Shipment delivered"
        );
        return ResponseEntity.ok(shipment);
    }

    @PostMapping
    @Operation(
        summary = "Créer un Shipment",
        description = "Crée un nouveau Shipment pour une commande de fulfillment."
    )
    public ResponseEntity<Shipment> createShipment(
        @RequestBody CreateShipmentRequest request) {
        Shipment shipment = carrierService.createShipment(
            request.getFulfillmentOrderId(),
            request.getCarrier()
        );
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Récupérer un Shipment",
        description = "Retourne les détails d'un Shipment par son UUID."
    )
    public ResponseEntity<Shipment> getShipment(
        @Parameter(description = "UUID du Shipment") @PathVariable UUID id) {
        Shipment shipment = carrierService.getShipmentHistory(id).stream()
            .findFirst()
            .map(ShipmentIndicator::getShipment)
            .orElseThrow(() -> new RuntimeException("Shipment not found"));
        return ResponseEntity.ok(shipment);
    }

    @GetMapping("/{id}/indicators")
    @Operation(
        summary = "Récupérer l'historique d'un Shipment",
        description = "Retourne la liste des ShipmentIndicator associés à un Shipment."
    )
    public ResponseEntity<List<ShipmentIndicator>> getShipmentHistory(
        @Parameter(description = "UUID du Shipment") @PathVariable UUID id) {
        List<ShipmentIndicator> indicators = carrierService.getShipmentHistory(id);
        return ResponseEntity.ok(indicators);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour un Shipment",
        description = "Met à jour les informations d'un Shipment."
    )
    public ResponseEntity<Shipment> updateShipment(
        @Parameter(description = "UUID du Shipment") @PathVariable UUID id,
        @RequestBody CreateShipmentRequest request) {
        Shipment shipment = carrierService.updateShipment(id, request);
        return ResponseEntity.ok(shipment);
    }

    @GetMapping()
    @Operation(
        summary = "Récupérer toutes les expéditions avec leurs données complètes",
        description = "Retourne la liste complète des expéditions avec leurs commandes et transporteurs associés."
    )
    public ResponseEntity<List<Shipment>> getAllShipmentsWithDetails() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }
}

class StartShipmentRequest {
    private UUID shipmentId;

    public UUID getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }
}

class DeliveredShipmentRequest {
    private UUID shipmentId;

    public UUID getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(UUID shipmentId) {
        this.shipmentId = shipmentId;
    }
} 