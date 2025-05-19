package fr.baretto.Command;

import fr.baretto.Entity.Carrier;
import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Entity.Warehouse;
import fr.baretto.Entity.DeliveryAddress;
import fr.baretto.Entity.Contact;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Enumeration.ShipmentEventType;
import fr.baretto.Repository.CarrierRepository;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.OrderItemRepository;
import fr.baretto.Repository.ShipmentIndicatorRepository;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.WarehouseRepository;
import fr.baretto.Service.CarrierService;
import fr.baretto.Service.FulfillmentOrderService;
import fr.baretto.Service.WarehouseService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TestCommandService {
    private final FulfillmentOrderService fulfillmentOrderService;
    private final WarehouseService warehouseService;
    private final CarrierService carrierService;
    private final CarrierRepository carrierRepository;
    private final ShipmentRepository shipmentRepository;
    private final FulfillmentOrderRepository fulfillmentOrderRepository;
    private final WarehouseRepository warehouseRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShipmentIndicatorRepository shipmentIndicatorRepository;
    @Autowired
    private EntityManager entityManager;

    public TestCommandService(FulfillmentOrderService fulfillmentOrderService,
                              WarehouseService warehouseService,
                              CarrierService carrierService,
                              CarrierRepository carrierRepository,
                              ShipmentRepository shipmentRepository,
                              FulfillmentOrderRepository fulfillmentOrderRepository,
                              WarehouseRepository warehouseRepository,
                              OrderItemRepository orderItemRepository,
                              ShipmentIndicatorRepository shipmentIndicatorRepository) {
        this.fulfillmentOrderService = fulfillmentOrderService;
        this.warehouseService = warehouseService;
        this.carrierService = carrierService;
        this.carrierRepository = carrierRepository;
        this.shipmentRepository = shipmentRepository;
        this.fulfillmentOrderRepository = fulfillmentOrderRepository;
        this.warehouseRepository = warehouseRepository;
        this.orderItemRepository = orderItemRepository;
        this.shipmentIndicatorRepository = shipmentIndicatorRepository;
    }

    private void verify(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    @Transactional
    public void runTest() {
        System.out.println("=== Démarrage du test de commande ===");

        verify(warehouseRepository.count() > 0, "Aucun entrepôt trouvé dans la base de données");
        verify(carrierRepository.count() > 0, "Aucun transporteur trouvé dans la base de données");

        FulfillmentOrder order = new FulfillmentOrder();
        order.setCustomerId(UUID.randomUUID());
        
        // Adresse de livraison factice
        DeliveryAddress address = new DeliveryAddress();
        address.setZone("Zone A");
        address.setStreet("1 rue de la Paix");
        address.setCity("Paris");
        address.setPostalCode("75000");
        address.setCountry("France");
        order.setDeliveryAddress(address);
        
        // Contact factice
        Contact contact = new Contact();
        contact.setEmail("test@novamart.fr");
        contact.setPhoneNumber("0102030405");
        order.setContact(contact);
        
        order.setStatus(FulfillmentStatus.CREATED);
        order = fulfillmentOrderRepository.save(order);

        OrderItem item = fulfillmentOrderService.addItem(
            order.getId(),
            "PROD-001",
            2,
            new BigDecimal("19.99")
        );
        verify(item != null, "L'item n'a pas été ajouté");
        verify(orderItemRepository.findByFulfillmentOrderId(order.getId()).size() == 1, "L'item n'a pas été sauvegardé");

        order = fulfillmentOrderService.acceptOrder(order.getId());
        verify(order.getStatus() == FulfillmentStatus.VALIDATED, "Le statut n'est pas VALIDATED");

        order = fulfillmentOrderService.markPrepared(order.getId());
        verify(order.getStatus() == FulfillmentStatus.IN_PREPARATION, "Le statut n'est pas IN_PREPARATION");

        Carrier carrier = carrierRepository.findAll().stream().findFirst()
            .orElseThrow(() -> new IllegalStateException("Aucun transporteur trouvé"));

        Shipment shipment = carrierService.createShipment(
            order.getId(),
            carrier.getId()
        );
        verify(shipment != null, "L'expédition n'a pas été créée");
        verify(shipment.getStatus() == FulfillmentStatus.VALIDATED, "Le statut initial de l'expédition n'est pas VALIDATED");

        shipment = carrierService.pushTracking(
            shipment.getId(),
            ShipmentEventType.IN_TRANSIT,
            "En cours de livraison"
        );
        verify(shipment.getStatus() == FulfillmentStatus.IN_DELIVERY, "Le statut n'est pas IN_DELIVERY");

        List<ShipmentIndicator> indicators = shipmentIndicatorRepository.findByShipmentIdOrderByCreatedAtAsc(shipment.getId());
        verify(!indicators.isEmpty(), "Aucun indicateur n'a été créé");

        FulfillmentOrder finalOrder = fulfillmentOrderRepository.findById(order.getId())
            .orElseThrow(() -> new IllegalStateException("Commande non trouvée"));
        verify(finalOrder.getStatus() == FulfillmentStatus.IN_DELIVERY, "Le statut final de la commande n'est pas IN_DELIVERY");

        Shipment finalShipment = shipmentRepository.findById(shipment.getId())
            .orElseThrow(() -> new IllegalStateException("Expédition non trouvée"));
        verify(finalShipment.getStatus() == FulfillmentStatus.IN_DELIVERY, "Le statut final de l'expédition n'est pas IN_DELIVERY");
    }
} 