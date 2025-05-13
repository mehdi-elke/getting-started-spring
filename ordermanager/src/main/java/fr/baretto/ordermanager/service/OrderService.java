package fr.baretto.ordermanager.service;

import fr.baretto.ordermanager.dto.OrderRequest;
import fr.baretto.ordermanager.dto.OrderResponse;
import fr.baretto.ordermanager.dto.PaymentRequest;
import fr.baretto.ordermanager.model.Contact;
import fr.baretto.ordermanager.model.Order;
import fr.baretto.ordermanager.model.OrderStatus;
import fr.baretto.ordermanager.repository.OrderRepository;
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

    // Vérification du stock via Inventory Service.
    // Retourne directement un OrderResponse avec un message d’erreur si le stock est insuffisant.
    public OrderResponse validateOrder(OrderRequest request) {
        boolean stockAvailable = checkInventory(request.getOrderDetails(), request.getDeliveryZone());

        if (!stockAvailable) {
            return new OrderResponse("Stock insuffisant ou zone de livraison invalide.");
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

        orderRepository.save(order);
        return new OrderResponse(order.getOrderId(), order.getStatus(), order.getCreationDate());
    }

    private boolean checkInventory(String orderDetails, String zone) {
        String url = "http://localhost:8081/inventory/check?zone=" + zone + "&orderDetails=" + orderDetails;
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            return true;
        }
    }

    // Traitement du paiement via Payment Service.
    // Retourne directement un OrderResponse avec un message d’erreur en cas de problème.
    public OrderResponse payOrder(PaymentRequest paymentRequest) {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(paymentRequest.getOrderId());
        if (optionalOrder.isEmpty() || optionalOrder.get().getStatus() != OrderStatus.CREATED) {
            return new OrderResponse("Commande non trouvée ou état de commande invalide.");
        }
        boolean paymentSuccess = processPayment(paymentRequest);
        if (!paymentSuccess) {
            return new OrderResponse("Paiement refusé ou problème technique lors du paiement.");
        }
        Order order = optionalOrder.get();
        order.setStatus(OrderStatus.VALIDATED);
        order.setPaymentMethod(paymentRequest.getPaymentMethod());

        orderRepository.save(order);
        return new OrderResponse(order.getOrderId(), order.getStatus(), order.getCreationDate());
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