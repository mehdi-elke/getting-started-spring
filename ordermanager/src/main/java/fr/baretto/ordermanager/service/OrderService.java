package fr.baretto.ordermanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.baretto.ordermanager.dto.OrderRequest;
import fr.baretto.ordermanager.dto.OrderResponse;
import fr.baretto.ordermanager.dto.PaymentRequest;
import fr.baretto.ordermanager.model.Contact;
import fr.baretto.ordermanager.model.Order;
import fr.baretto.ordermanager.model.OrderStatus;
import fr.baretto.ordermanager.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Vérification du stock via Inventory Service.
    // Retourne directement un OrderResponse avec un message d’erreur si le stock est insuffisant.
    public OrderResponse validateOrder(OrderRequest request) {
        boolean stockAvailable = checkInventory(request.getOrderDetailToString());

        if (!stockAvailable) {
            return new OrderResponse("Stock insuffisant ou zone de livraison invalide.");
        }

        Order order = new Order();
        order.setUuid(UUID.randomUUID());
        order.setOrderId(order.getUuid().toString());
        order.setStatus(OrderStatus.CREATED);
        order.setCreationDate(new Date());
        order.setContact(new Contact(request.getEmail(), request.getPhoneNumber()));
        order.setAddress(request.getAddressString());
        order.setDeliveryZone(request.getZone());
        order.setOrderDetails(request.getOrderDetailToString());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderTracking(request.getOrderTracking());

        orderRepository.save(order);
        return new OrderResponse(order.getOrderId(), order.getStatus(), order.getCreationDate());
    }

    private boolean checkInventory(String orderDetails) {
        String url = "http://localhost:8082/inventory/check/productId?orderDetails=" + orderDetails;
        try {
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            return true;
        }
    }

    public OrderResponse payOrder(PaymentRequest paymentRequest) {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(paymentRequest.getOrderId());
        if (optionalOrder.isEmpty() || optionalOrder.get().getStatus() != OrderStatus.CREATED) {
            return new OrderResponse("Commande non trouvée ou état de commande invalide.");
        }
        Order order = optionalOrder.get();

        boolean paymentSuccess = processPayment(paymentRequest);
        if (!paymentSuccess) {
            return new OrderResponse("Paiement refusé ou problème technique lors du paiement.");
        }
        order.setStatus(OrderStatus.VALIDATED);
        order.setPaymentMethod(paymentRequest.getPaymentMethod().toString());

        orderRepository.save(order);
        return new OrderResponse(order.getOrderId(), order.getStatus(), order.getCreationDate());
    }

    private boolean processPayment(PaymentRequest paymentRequest) {
        String url = "http://localhost:8083/payment/process";
        try {
            restTemplate.postForEntity(url, paymentRequest, String.class);
            return true;
        } catch (Exception e) {
            log.error("e: ", new RuntimeException("Erreur lors du paiement de la commande", e));
            return false;
        }
    }

    public void simuleCommand(PaymentRequest paymentRequest) throws InterruptedException {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(paymentRequest.getOrderId());
        Order orderVrai = optionalOrder.get();

        orderVrai.setStatus(OrderStatus.IN_PREPARATION);
        System.out.println("📦 Votre colis est en préparation...");

        Thread.sleep(2000); // 2 secondes

        orderVrai.setStatus(OrderStatus.IN_DELIVERY);
        System.out.println("🚚 Votre colis est en cours de livraison...");

        Thread.sleep(2000); // 2 secondes

        orderVrai.setStatus(OrderStatus.FULFILLED);
        System.out.println("✅ Votre colis a été livré avec succès !");
    }

    public boolean processOrder(OrderRequest order, PaymentRequest paymentRequest) throws JsonProcessingException {
        Optional<Order> optionalOrder = orderRepository.findByOrderId(paymentRequest.getOrderId());
        Order orderVrai = optionalOrder.get();

        // Construction de l'objet à envoyer
        Map<String, Object> fulfillmentRequest = new HashMap<>();
        fulfillmentRequest.put("customerId", order.getCustomerId());

        Map<String, String> deliveryAddress = new HashMap<>();
        deliveryAddress.put("zone", order.getZone());
        deliveryAddress.put("street", order.getDeliveryAddress().getStreet());
        deliveryAddress.put("city", order.getDeliveryAddress().getCity());
        deliveryAddress.put("postalCode", order.getDeliveryAddress().getPostalCode());
        deliveryAddress.put("country", order.getDeliveryAddress().getCountry());
        fulfillmentRequest.put("deliveryAddress", deliveryAddress);

        Map<String, String> contact = new HashMap<>();
        contact.put("email", order.getEmail());
        contact.put("phoneNumber", order.getPhoneNumber());
        fulfillmentRequest.put("contact", contact);

        List<Map<String, Object>> orderLines = new ArrayList<>();
        for (int i = 0; i < order.getOrderDetails().size(); i++) {
            Map<String, Object> orderLine = new HashMap<>();
            orderLine.put("productId", order.getOrderDetails().get(i).getProductReference());
            orderLine.put("quantity", order.getOrderDetails().get(i).getQuantity());
            orderLine.put("price", order.getOrderDetails().get(i).getPrice());
            List<Map<String, Object>> shipments = new ArrayList<>();
            for (int j = 0; j < order.getOrderDetails().get(i).getShipment().size(); j++) {
                Map<String, Object> shipment = new HashMap<>();
                shipment.put("trackingNumber", order.getOrderDetails().get(i).getShipment().get(j).getTrackingNumber());

                List<Map<String, String>> indicators = new ArrayList<>();
                for (int k = 0; k < order.getOrderDetails().get(i).getShipment().get(j).getIndicators().size(); k++) {
                    Map<String, String> indicator = new HashMap<>();
                    indicator.put("eventType", order.getOrderDetails().get(i).getShipment().get(j).getIndicators().get(k).getEventType());
                    indicator.put("eventDescription", order.getOrderDetails().get(i).getShipment().get(j).getIndicators().get(k).getEventDescription());
                    indicators.add(indicator);
                }

                shipment.put("indicators", indicators);
                shipments.add(shipment);
            }

            orderLine.put("shipment", shipments);
            orderLines.add(orderLine);
        }

        fulfillmentRequest.put("orderLines", orderLines);

        String url = "http://localhost:8081/fulfillment";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, fulfillmentRequest, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                orderVrai.setStatus(OrderStatus.IN_PREPARATION);
                return true;
            } else {
                System.err.println("Erreur lors de l'envoi de la requête : " + response.getStatusCode());
                return false;
            }
        } catch (Exception e) {
            log.error("e: ", new RuntimeException("Erreur lors de l'envoi de la requête de traitement de commande", e));
            return false;
        }
    }


    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}