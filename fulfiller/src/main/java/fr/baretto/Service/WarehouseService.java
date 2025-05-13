package fr.baretto.Service;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.Warehouse;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.WarehouseRepository;
import fr.baretto.Exception.FulfillmentOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WarehouseService {

    private final FulfillmentOrderRepository fulfillmentOrderRepository;
    private final ShipmentRepository shipmentRepository;

    @Autowired
    public WarehouseService(
            FulfillmentOrderRepository fulfillmentOrderRepository,
            ShipmentRepository shipmentRepository) {
        this.fulfillmentOrderRepository = fulfillmentOrderRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional
    public FulfillmentOrder startPreparation(UUID orderId) {
        FulfillmentOrder order = fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != FulfillmentStatus.ACCEPTED) {
            throw new RuntimeException("Order status must be ACCEPTED to start preparation");
        }

        order.setStatus(FulfillmentStatus.IN_PREPARATION);
        List<Shipment> shipments = shipmentRepository.findByFulfillmentOrderId(orderId);
        for (Shipment shipment : shipments) {
            shipment.setStatus(FulfillmentStatus.IN_PREPARATION);
            shipmentRepository.save(shipment);
        }

        return fulfillmentOrderRepository.save(order);
    }

    @Transactional
    public FulfillmentOrder finishPreparation(UUID orderId) {
        FulfillmentOrder order = fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != FulfillmentStatus.IN_PREPARATION) {
            throw new RuntimeException("Order status must be IN_PREPARATION to finish preparation");
        }

        order.setStatus(FulfillmentStatus.IN_DELIVERY);
        List<Shipment> shipments = shipmentRepository.findByFulfillmentOrderId(orderId);
        for (Shipment shipment : shipments) {
            shipment.setStatus(FulfillmentStatus.IN_DELIVERY);
            shipmentRepository.save(shipment);
        }

        return fulfillmentOrderRepository.save(order);
    }

    public List<OrderItem> getOrderItems(UUID orderId) {
        FulfillmentOrder order = fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new FulfillmentOrderException("Commande non trouvée: " + orderId));
        return order.getItems();
    }
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
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