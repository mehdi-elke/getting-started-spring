package fr.baretto.Service;

import fr.baretto.Controller.CreateShipmentRequest;
import fr.baretto.Entity.Carrier;
import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Entity.Warehouse;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Repository.CarrierRepository;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.ShipmentIndicatorRepository;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.WarehouseRepository;
import fr.baretto.Exception.FulfillmentOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CarrierService {
    private final CarrierRepository carrierRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentIndicatorRepository shipmentIndicatorRepository;
    private final FulfillmentOrderRepository fulfillmentOrderRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public CarrierService(CarrierRepository carrierRepository,
                         ShipmentRepository shipmentRepository,
                         ShipmentIndicatorRepository shipmentIndicatorRepository,
                         FulfillmentOrderRepository fulfillmentOrderRepository,
                         WarehouseRepository warehouseRepository) {
        this.carrierRepository = carrierRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentIndicatorRepository = shipmentIndicatorRepository;
        this.fulfillmentOrderRepository = fulfillmentOrderRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public List<Carrier> getAllCarriers() {
        return carrierRepository.findAll();
    }

    public Carrier getCarrierById(UUID id) {
        return carrierRepository.findById(id)
            .orElseThrow(() -> new FulfillmentOrderException("Transporteur non trouvé"));
    }

    @Transactional
    public Carrier createCarrier(String code, String name) {
        Carrier carrier = new Carrier();
        carrier.setCode(code);
        carrier.setName(name);
        return carrierRepository.save(carrier);
    }

    @Transactional
    public Carrier updateCarrier(UUID id, String code, String name) {
        Carrier carrier = getCarrierById(id);
        carrier.setCode(code);
        carrier.setName(name);
        return carrierRepository.save(carrier);
    }

    public Set<Warehouse> getCarrierWarehouses(UUID id) {
        Carrier carrier = getCarrierById(id);
        return carrier.getIncludedWarehouses();
    }

    @Transactional
    public Carrier addWarehouseToCarrier(UUID carrierId, UUID warehouseId) {
        Carrier carrier = getCarrierById(carrierId);
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new FulfillmentOrderException("Entrepôt non trouvé"));
        
        carrier.getIncludedWarehouses().add(warehouse);
        return carrierRepository.save(carrier);
    }

    @Transactional
    public Carrier removeWarehouseFromCarrier(UUID carrierId, UUID warehouseId) {
        Carrier carrier = getCarrierById(carrierId);
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
            .orElseThrow(() -> new FulfillmentOrderException("Entrepôt non trouvé"));
        
        carrier.getIncludedWarehouses().remove(warehouse);
        return carrierRepository.save(carrier);
    }

    @Transactional
    public Shipment createShipment(UUID orderId, UUID carrierId) {
        FulfillmentOrder order = fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new FulfillmentOrderException("Commande non trouvée"));
        
        Carrier carrier = carrierRepository.findById(carrierId)
                .orElseThrow(() -> new FulfillmentOrderException("Transporteur non trouvé"));

        if (order.getOrderLines().isEmpty()) {
            throw new FulfillmentOrderException("Impossible de créer un colis pour une commande sans articles");
        }

        Shipment shipment = new Shipment();
        shipment.setFulfillmentOrder(order);
        shipment.setCarrier(carrier);
        shipment.setTrackingNumber("TRACK-" + UUID.randomUUID().toString().substring(0, 8));
        shipment.setCurrency("EUR");
        shipment.setOrderItem(order.getOrderLines().get(0));
        
        Shipment savedShipment = shipmentRepository.save(shipment);
        
        ShipmentIndicator indicator = new ShipmentIndicator();
        indicator.setShipment(savedShipment);
        indicator.setEventType(ShipmentEventType.CREATED);
        indicator.setEventDescription("Création du colis");
        shipmentIndicatorRepository.save(indicator);
        
        return savedShipment;
    }

    @Transactional
    public Shipment pushTracking(UUID shipmentId, ShipmentEventType eventType, String description) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new FulfillmentOrderException("Colis non trouvé"));
        
        FulfillmentOrder order = shipment.getFulfillmentOrder();
        if (eventType == ShipmentEventType.FULFILLED) {
            ShipmentIndicator newIndicator = new ShipmentIndicator();
            newIndicator.setShipment(shipment);
            newIndicator.setEventType(ShipmentEventType.FULFILLED);
            newIndicator.setEventDescription(description);

            shipment.addIndicator(newIndicator);
        } else {
            ShipmentIndicator newIndicator = new ShipmentIndicator();
            newIndicator.setShipment(shipment);
            newIndicator.setEventType(eventType);
            newIndicator.setEventDescription(description);
            newIndicator.setEventType(ShipmentEventType.IN_TRANSIT);
            shipment.addIndicator(newIndicator);
        }
        fulfillmentOrderRepository.save(order);
        shipment = shipmentRepository.save(shipment);

        return shipment;
    }

    public List<ShipmentIndicator> getShipmentHistory(UUID shipmentId) {
        return shipmentIndicatorRepository.findByShipmentId(shipmentId);
    }

    public Shipment updateShipment(UUID id, CreateShipmentRequest request) {
        Shipment s = shipmentRepository.findById(id)
                .orElseThrow(() -> new FulfillmentOrderException("Colis non trouvé"));

        if (request.getTrackingNumber() != null) s.setTrackingNumber(request.getTrackingNumber());
        if (request.getTrackingUrl() != null) s.setTrackingUrl(request.getTrackingUrl());
        if (request.getEstimatedPrice() != null) s.setEstimatedPrice(request.getEstimatedPrice());
        if (request.getShippingPrice() != null) s.setShippingPrice(request.getShippingPrice());
        if (request.getCurrency() != null) s.setCurrency(request.getCurrency());
        if (request.getLabelUrl() != null) s.setLabelUrl(request.getLabelUrl());
        if (request.getCarrierCode() != null) s.setCarrierCode(request.getCarrierCode());
        if (request.getCarrier() != null) {
            Carrier carrier = carrierRepository.findById(request.getCarrier())
                    .orElseThrow(() -> new FulfillmentOrderException("Transporteur non trouvé"));
            s.setCarrier(carrier);
        }

        return shipmentRepository.save(s);
    }
}