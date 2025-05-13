// Fichier: OrderService.java (mise à jour)
package fr.baretto.ordermanager;

import fr.baretto.ordermanager.OrderRequest;
import fr.baretto.ordermanager.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Vérification du stock via Inventory Service (à simuler)
    // Si stock OK, passer l'état à CREATED
    public Order validateOrder(OrderRequest request) {
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

    private boolean checkInventory(String orderDetails, String zone) {
        String url = "http://localhost:8081/inventory/check?zone=" + zone + "&orderDetails=" + orderDetails;
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            return false; // Paiement refusé
        }
    }

    // Appel au Payment Service pour valider le paiement
    // Si paiement OK, passer l'état à VALIDATED
    public Order payOrder(PaymentRequest paymentRequest) {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(paymentRequest.getOrderId());

        if (optionalOrder.isEmpty() || optionalOrder.get().getStatus() != OrderStatus.CREATED) {
            return null; // Commande non trouvée ou pas dans le bon état
        }

        boolean paymentSuccess = processPayment(paymentRequest);

        if (!paymentSuccess) {
            return null; // Paiement refusé
        }

        Order order = optionalOrder.get();
        order.setStatus(OrderStatus.VALIDATED);
        order.setPaymentMethod(paymentRequest.getPaymentMethod());

        return orderRepository.save(order);
    }

    private boolean processPayment(PaymentRequest paymentRequest) {
        String url = "http://localhost:8081/payment/process";
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(url, paymentRequest, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}