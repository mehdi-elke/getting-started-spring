package fr.baretto.Service;

import fr.baretto.Entity.FulfillmentOrder;
import fr.baretto.Entity.OrderItem;
import fr.baretto.Entity.Shipment;
import fr.baretto.Entity.ShipmentIndicator;
import fr.baretto.Enumeration.FulfillmentStatus;
import fr.baretto.Repository.FulfillmentOrderRepository;
import fr.baretto.Repository.OrderItemRepository;
import fr.baretto.Repository.ShipmentIndicatorRepository;
import fr.baretto.Repository.ShipmentRepository;
import fr.baretto.Repository.CarrierRepository;
import fr.baretto.Entity.Carrier;
import fr.baretto.Exception.FulfillmentOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class FulfillmentOrderService {

    private final FulfillmentOrderRepository fulfillmentOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentIndicatorRepository shipmentIndicatorRepository;
    private final CarrierRepository carrierRepository;

    @Autowired
    public FulfillmentOrderService(
            FulfillmentOrderRepository fulfillmentOrderRepository,
            OrderItemRepository orderItemRepository,
            ShipmentRepository shipmentRepository,
            ShipmentIndicatorRepository shipmentIndicatorRepository,
            CarrierRepository carrierRepository) {
        this.fulfillmentOrderRepository = fulfillmentOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentIndicatorRepository = shipmentIndicatorRepository;
        this.carrierRepository = carrierRepository;
    }

    @Transactional
    public FulfillmentOrder createOrder(FulfillmentOrder orderReference) {
        FulfillmentOrder order = new FulfillmentOrder();
        order.setStatus(FulfillmentStatus.CREATED);
        return fulfillmentOrderRepository.save(order);
    }

    @Transactional
    public FulfillmentOrder acceptOrder(UUID orderId) {
        FulfillmentOrder order = getOrder(orderId);
        if (order.getStatus() != FulfillmentStatus.CREATED) {
            throw new FulfillmentOrderException("La commande doit être en statut CREATED pour être validée");
        }
        if (order.getOrderLines().isEmpty()) {
            throw new FulfillmentOrderException("La commande doit contenir au moins un article pour être validée");
        }
        order.setStatus(FulfillmentStatus.VALIDATED);
        return fulfillmentOrderRepository.save(order);
    }

    @Transactional
    public FulfillmentOrder markPrepared(UUID orderId) {
        FulfillmentOrder order = getOrder(orderId);
        if (order.getStatus() != FulfillmentStatus.VALIDATED) {
            throw new FulfillmentOrderException("La commande doit être en statut VALIDATED pour être marquée comme préparée");
        }
        List<OrderItem> items = orderItemRepository.findByFulfillmentOrderId(orderId);
        if (items.isEmpty()) {
            throw new FulfillmentOrderException("La commande doit avoir au moins un item pour être marquée comme préparée");
        }
        
        // Vérifier que tous les articles ont un prix valide
        for (OrderItem item : items) {
            if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new FulfillmentOrderException("Tous les articles doivent avoir un prix valide");
            }
        }
        
        order.setStatus(FulfillmentStatus.IN_PREPARATION);
        return fulfillmentOrderRepository.save(order);
    }

    @Transactional
    public FulfillmentOrder markReadyForDelivery(UUID orderId) {
        FulfillmentOrder order = getOrder(orderId);
        if (order.getStatus() != FulfillmentStatus.IN_PREPARATION) {
            throw new FulfillmentOrderException("La commande doit être en statut IN_PREPARATION pour être marquée comme prête pour la livraison");
        }
        
        // Vérifier que tous les articles ont été préparés
        List<OrderItem> items = orderItemRepository.findByFulfillmentOrderId(orderId);
        if (items.isEmpty()) {
            throw new FulfillmentOrderException("La commande doit avoir au moins un item pour être marquée comme prête pour la livraison");
        }
        
        order.setStatus(FulfillmentStatus.IN_DELIVERY);
        return fulfillmentOrderRepository.save(order);
    }

    @Transactional
    public OrderItem addItem(UUID orderId, String productId, int quantity, BigDecimal price) {
        FulfillmentOrder order = getOrder(orderId);
        if (order.getStatus() == FulfillmentStatus.IN_DELIVERY || 
            order.getStatus() == FulfillmentStatus.IN_TRANSIT || 
            order.getStatus() == FulfillmentStatus.FULFILLED) {
            throw new FulfillmentOrderException("Impossible d'ajouter un item à une commande en cours de livraison ou livrée");
        }

        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setPrice(price);
        order.addOrderLine(item);
        fulfillmentOrderRepository.save(order);

        if (order.getOrderLines().size() == 1) {
            Carrier carrier = carrierRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new FulfillmentOrderException("Aucun transporteur disponible"));
            Shipment shipment = new Shipment();
            shipment.setFulfillmentOrder(order);
            shipment.setCarrier(carrier);
            shipment.setStatus(FulfillmentStatus.VALIDATED);
            shipment.setCurrency("EUR");
            shipment.setTrackingNumber("AUTO-" + UUID.randomUUID());
            shipment.setOrderItem(item);
            shipmentRepository.save(shipment);
        }

        return item;
    }

    public FulfillmentOrder getOrder(UUID orderId) {
        return fulfillmentOrderRepository.findById(orderId)
                .orElseThrow(() -> new FulfillmentOrderException("Commande non trouvée: " + orderId));
    }

    public List<OrderItem> getOrderItems(UUID orderId) {
        FulfillmentOrder order = getOrder(orderId);
        return order.getOrderLines();
    }

    public List<FulfillmentOrder> getAllOrders() {
        return fulfillmentOrderRepository.findAll();
    }
} 