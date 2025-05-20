package fr.baretto.Service;

import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.ShipmentIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentIndicatorRepository shipmentIndicatorRepository;

    @Autowired
    public ShipmentService(ShipmentRepository shipmentRepository,
                          ShipmentIndicatorRepository shipmentIndicatorRepository) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentIndicatorRepository = shipmentIndicatorRepository;
    }

    @Transactional(readOnly = true)
    public List<ShipmentIndicator> getShipmentHistory(UUID shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        return shipmentIndicatorRepository.findByShipmentId(shipmentId);
    }

    @Transactional(readOnly = true)
    public Shipment getShipment(UUID shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
    }

    @Transactional
    public List<ShipmentIndicator> getShipmentIndicators(UUID shipmentId) {
        Shipment shipment = getShipment(shipmentId);
        return shipmentIndicatorRepository.findByShipmentOrderByCreatedAtDesc(shipment);
    }

    @Transactional
    public Shipment updateTrackingNumber(UUID shipmentId, String trackingNumber) {
        Shipment shipment = getShipment(shipmentId);
        shipment.setTrackingNumber(trackingNumber);
        return shipmentRepository.save(shipment);
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }
} 