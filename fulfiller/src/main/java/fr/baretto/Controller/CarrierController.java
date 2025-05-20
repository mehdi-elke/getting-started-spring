package fr.baretto.Controller;

import fr.baretto.Entity.Carrier;
import fr.baretto.Entity.Warehouse;
import fr.baretto.Service.CarrierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/fulfillment/carriers")
@Tag(name = "Carrier", description = "Gestion des transporteurs")
public class CarrierController {

    private final CarrierService carrierService;

    @Autowired
    public CarrierController(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    @GetMapping()
    @Operation(
        summary = "Récupérer tous les transporteurs avec leurs données complètes",
        description = "Retourne la liste complète des transporteurs avec leurs entrepôts associés."
    )
    public ResponseEntity<List<Carrier>> getAllCarriersWithDetails() {
        return ResponseEntity.ok(carrierService.getAllCarriers());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Récupérer un transporteur par son UUID",
        description = "Retourne un transporteur à partir de son identifiant UUID."
    )
    public ResponseEntity<Carrier> getCarrierById(
        @Parameter(description = "UUID du transporteur") @PathVariable UUID id) {
        return ResponseEntity.ok(carrierService.getCarrierById(id));
    }

    @PostMapping
    @Operation(
        summary = "Créer un transporteur",
        description = "Crée un nouveau transporteur avec un code et un nom."
    )
    public ResponseEntity<Carrier> createCarrier(@RequestBody CreateCarrierRequest request) {
        Carrier carrier = carrierService.createCarrier(request.getCode(), request.getName());
        return ResponseEntity.ok(carrier);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Mettre à jour un transporteur",
        description = "Met à jour les informations d'un transporteur existant."
    )
    public ResponseEntity<Carrier> updateCarrier(
        @Parameter(description = "UUID du transporteur") @PathVariable UUID id,
        @RequestBody UpdateCarrierRequest request) {
        Carrier carrier = carrierService.updateCarrier(id, request.getCode(), request.getName());
        return ResponseEntity.ok(carrier);
    }

    @GetMapping("/{id}/warehouses")
    @Operation(
        summary = "Lister les entrepôts d'un transporteur",
        description = "Retourne la liste des entrepôts associés à un transporteur."
    )
    public ResponseEntity<Set<Warehouse>> getCarrierWarehouses(
        @Parameter(description = "UUID du transporteur") @PathVariable UUID id) {
        return ResponseEntity.ok(carrierService.getCarrierWarehouses(id));
    }

    @PostMapping("/{id}/warehouses/{warehouseId}")
    @Operation(
        summary = "Associer un entrepôt à un transporteur",
        description = "Ajoute un entrepôt à la liste des entrepôts d'un transporteur."
    )
    public ResponseEntity<Carrier> addWarehouseToCarrier(
        @Parameter(description = "UUID du transporteur") @PathVariable UUID id,
        @Parameter(description = "UUID de l'entrepôt") @PathVariable UUID warehouseId) {
        Carrier carrier = carrierService.addWarehouseToCarrier(id, warehouseId);
        return ResponseEntity.ok(carrier);
    }

    @DeleteMapping("/{id}/warehouses/{warehouseId}")
    @Operation(
        summary = "Dissocier un entrepôt d'un transporteur",
        description = "Retire un entrepôt de la liste des entrepôts d'un transporteur."
    )
    public ResponseEntity<Carrier> removeWarehouseFromCarrier(
        @Parameter(description = "UUID du transporteur") @PathVariable UUID id,
        @Parameter(description = "UUID de l'entrepôt") @PathVariable UUID warehouseId) {
        Carrier carrier = carrierService.removeWarehouseFromCarrier(id, warehouseId);
        return ResponseEntity.ok(carrier);
    }
}

class CreateCarrierRequest {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class UpdateCarrierRequest {
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 