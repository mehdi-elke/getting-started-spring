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

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    // Vérification du stock via Inventory Service.
    // Retourne directement un OrderResponse avec un message d’erreur si le stock est insuffisant.
    public OrderResponse validateOrder(OrderRequest request) {
        boolean stockAvailable = checkInventory(request.getOrderDetailToString(), request.getZone());

        if (!stockAvailable) {
            return new OrderResponse("Stock insuffisant ou zone de livraison invalide.");
        }

        Order order = new Order();
        order.setUuid(UUID.randomUUID());
        order.setOrderId("CMD-" + order.getUuid().toString().substring(0, 8));
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
            return true;
        }
    }

    public boolean processOrder(OrderRequest order) {

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
            orderLine.put("quantity", order.getOrderDetails().get(i).getQuantity()); // Exemple
            orderLine.put("price", order.getOrderDetails().get(i).getPrice());

            Map<String, Object> shipment = new HashMap<>();
            for (int j = 0; j < order.getOrderDetails().get(i).getShipment().size(); j++) {
                shipment.put("trackingNumber", order.getOrderDetails().get(i).getShipment().get(j).getTrackingNumber());
                List<Map<String, String>> indicators = new ArrayList<>();
                for (int k = 0; k < order.getOrderDetails().get(i).getShipment().get(j).getIndicators().size(); k++) {
                    Map<String, String> indicator = new HashMap<>();
                    indicator.put("eventType", order.getOrderDetails().get(i).getShipment().get(j).getIndicators().get(k).getEventType());
                    indicator.put("eventDescription", order.getOrderDetails().get(i).getShipment().get(j).getIndicators().get(k).getEventDescription());
                    indicators.add(indicator);
                }
                shipment.put("indicators", indicators);
            }
            orderLine.put("shipment", shipment);

            orderLines.add(orderLine);
        }

        fulfillmentRequest.put("orderLines", orderLines);

        System.out.printf(fulfillmentRequest.toString());

        // Envoi de la requête
        String url = "http://localhost:8081/fullfilment";
        try {
            ResponseEntity<Boolean> response = restTemplate.postForEntity(url, fulfillmentRequest, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            return false;
        }
    }


    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }
}