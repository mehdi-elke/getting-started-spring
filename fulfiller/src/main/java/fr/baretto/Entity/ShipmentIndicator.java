package fr.baretto.Entity;

import fr.baretto.Enumeration.ShipmentEventType;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "shipment_indicators")
public class ShipmentIndicator extends TimestableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(name = "event_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentEventType eventType;

    @Column(name = "event_description")
    private String eventDescription;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public ShipmentEventType getEventType() {
        return eventType;
    }

    public void setEventType(ShipmentEventType eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}