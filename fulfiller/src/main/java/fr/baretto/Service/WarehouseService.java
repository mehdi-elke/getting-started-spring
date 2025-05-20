package fr.baretto.Service;

import fr.baretto.Entity.*;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.WarehouseRepository;
import fr.baretto.Exception.FulfillmentOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final FulfillmentOrderRepository fulfillmentOrderRepository;
    private final ShipmentRepository shipmentRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(
            FulfillmentOrderRepository fulfillmentOrderRepository,
            ShipmentRepository shipmentRepository,
            WarehouseRepository warehouseRepository) {
        this.fulfillmentOrderRepository = fulfillmentOrderRepository;
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Transactional
    public FulfillmentOrder startPreparation(UUID orderId) {
        FulfillmentOrder order = fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new FulfillmentOrderException("Commande non trouvée"));

        if (order.getStatus() != FulfillmentStatus.VALIDATED) {
            throw new FulfillmentOrderException("La commande doit être en statut VALIDATED pour commencer la préparation");
        }

        List<Shipment> shipments = shipmentRepository.findByFulfillmentOrderId(orderId);
        for (Shipment shipment : shipments) {
            ShipmentIndicator shipmentIndicator = shipment.getLatestIndicator();
            ShipmentIndicator newIndicator = new ShipmentIndicator();
            newIndicator.setShipment(shipment);
            newIndicator.setEventType(ShipmentEventType.IN_PREPARATION);
            newIndicator.setEventDescription("Préparation de la commande en cours");
            shipment.addIndicator(newIndicator);
            shipmentRepository.save(shipment);
        }

        return fulfillmentOrderRepository.save(order);
    }

    @Transactional
    public FulfillmentOrder finishPreparation(UUID orderId) {
        FulfillmentOrder order = fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new FulfillmentOrderException("Commande non trouvée"));

        if (order.getStatus() != FulfillmentStatus.IN_PREPARATION) {
            throw new FulfillmentOrderException("La commande doit être en statut IN_PREPARATION pour terminer la préparation");
        }

        List<Shipment> shipments = shipmentRepository.findByFulfillmentOrderId(orderId);
        for (Shipment shipment : shipments) {
            ShipmentIndicator shipmentIndicator = shipment.getLatestIndicator();
            ShipmentIndicator newIndicator = new ShipmentIndicator();
            newIndicator.setShipment(shipment);
            newIndicator.setEventType(ShipmentEventType.IN_DELIVERY);
            newIndicator.setEventDescription("Préparation de la commande terminée");
            shipment.addIndicator(newIndicator);
            shipmentRepository.save(shipment);
        }

        return fulfillmentOrderRepository.save(order);
    }

    public List<OrderItem> getOrderItems(UUID orderId) {
        FulfillmentOrder order = fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new FulfillmentOrderException("Commande non trouvée: " + orderId));
        return order.getOrderLines();
    }
    
    public List<Warehouse> getWarehouses() {
        return warehouseRepository.findAll();
    }

    public Warehouse getWarehouseById(UUID id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new FulfillmentOrderException("Entrepôt non trouvé: " + id));
    }

    public Warehouse getWarehouseByCode(String code) {
        return warehouseRepository.findByCode(code)
                .orElseThrow(() -> new FulfillmentOrderException("Entrepôt non trouvé: " + code));
    }
}