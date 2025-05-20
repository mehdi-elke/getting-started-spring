package fr.baretto.Repository;

import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShipmentIndicatorRepository extends JpaRepository<ShipmentIndicator, UUID> {
    List<ShipmentIndicator> findByShipmentIdOrderByCreatedAtAsc(UUID shipmentId);
    List<ShipmentIndicator> findByShipmentOrderByCreatedAtDesc(Shipment shipment);
    List<ShipmentIndicator> findByShipmentId(UUID shipmentId);
} 