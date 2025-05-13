// Fichier: OrderService.java (mise à jour)
package fr.baretto.ordermanager;

import fr.baretto.ordermanager.OrderRequest;
import fr.baretto.ordermanager.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(String email, String phoneNumber, String address, String paymentMethod, String deliveryMethod, String orderDetails) {
        Order order = new Order();
        order.setUuid(UUID.randomUUID());
        order.setOrderId("CMD-" + order.getUuid());
        order.setStatus(OrderStatus.CREATED);
        order.setCreationDate(new Date());
        order.setContact(new Contact(email, phoneNumber));
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setDeliveryMethod(deliveryMethod);
        order.setOrderDetails(orderDetails);
        return orderRepository.save(order);
    }

    // Vérification du stock via Inventory Service (à simuler)
    // Si stock OK, passer l'état à CREATED
    public Order validateOrder(OrderRequest request) {
        // Simuler une vérification de stock
        boolean stockAvailable = checkInventory(request.getOrderDetails(), request.getDeliveryZone());

        if (!stockAvailable) {
            return null; // Stock indisponible
        }

        Order order = new Order();
        order.setUuid(UUID.randomUUID());
        order.setOrderId("CMD-" + order.getUuid().toString().substring(0, 8));
        order.setStatus(OrderStatus.CREATED);
        order.setCreationDate(new Date());
        order.setContact(new Contact(request.getEmail(), request.getPhoneNumber()));
        order.setAddress(request.getAddress());
        order.setDeliveryZone(request.getDeliveryZone());
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setOrderDetails(request.getOrderDetails());

        return orderRepository.save(order);
    }

    // Méthode simulée pour vérifier le stock
    private boolean checkInventory(String orderDetails, String zone) {
        // Simulation: 80% de chance que le stock soit disponible
        return Math.random() > 0.2;
    }

    // Appel au Payment Service pour valider le paiement
    // Si paiement OK, passer l'état à VALIDATED
    public Order payOrder(PaymentRequest paymentRequest) {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(paymentRequest.getOrderId());

        if (optionalOrder.isEmpty() || optionalOrder.get().getStatus() != OrderStatus.CREATED) {
            return null; // Commande non trouvée ou pas dans le bon état
        }

        // Simuler une validation de paiement
        boolean paymentSuccess = processPayment(paymentRequest);

        if (!paymentSuccess) {
            return null; // Paiement refusé
        }

        Order order = optionalOrder.get();
        order.setStatus(OrderStatus.VALIDATED);
        order.setPaymentMethod(paymentRequest.getPaymentMethod());

        return orderRepository.save(order);
    }

    // Méthode simulée pour traiter le paiement
    private boolean processPayment(PaymentRequest paymentRequest) {
        // Simulation: 90% de chance que le paiement soit accepté
        return Math.random() > 0.1;
    }

    // Méthode utilitaire pour récupérer une commande par son ID
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}