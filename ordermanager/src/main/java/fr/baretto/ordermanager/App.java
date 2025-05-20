package fr.baretto.ordermanager;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.baretto.ordermanager.controller.PaymentMethod;
import fr.baretto.ordermanager.dto.*;
import fr.baretto.ordermanager.model.OrderStatus;
import fr.baretto.ordermanager.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class App implements CommandLineRunner {

    private final OrderService orderService;

    public App(OrderService orderService) {
        this.orderService = orderService;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws JsonProcessingException, InterruptedException {
        System.out.println("🚀 Application démarrée avec succès");

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setStreet("12 rue Lafayette");
        deliveryAddress.setCity("Paris");
        deliveryAddress.setPostalCode("75001");
        deliveryAddress.setZone("77");
        deliveryAddress.setCountry("France");

        OrderLineDTO orderLineDTO1 = new OrderLineDTO();
        orderLineDTO1.setProductId("123456");
        orderLineDTO1.setQuantity(2); // Exemple : 2 unités
        orderLineDTO1.setPrice(100.0); // Exemple : 100€ par unité
        orderLineDTO1.setProductReference("DELL-XPS-13");

        OrderLineDTO orderLineDTO2 = new OrderLineDTO();
        orderLineDTO2.setProductId("789012");
        orderLineDTO2.setQuantity(1); // Exemple : 1 unité
        orderLineDTO2.setPrice(50.0); // Exemple : 50€ par unité
        orderLineDTO2.setProductReference("LOGITECH-MOUSE");

        ShipmentDTO shipmentDTO = new ShipmentDTO();
        shipmentDTO.setTrackingNumber("CHR094396875849");
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        indicatorDTO.setEventType("CREATED");
        indicatorDTO.setEventDescription("Commande créée");

        shipmentDTO.setIndicators(List.of(indicatorDTO));
        orderLineDTO1.setShipment(List.of(shipmentDTO));
        orderLineDTO2.setShipment(List.of(shipmentDTO));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setEmail("alice.dupont@example.com");
        orderRequest.setPhoneNumber("0612345678");
        orderRequest.setDeliveryAddress(deliveryAddress);
        orderRequest.setOrderDetails(List.of(orderLineDTO1, orderLineDTO2));
        orderRequest.setOrderTracking("CHR094396875849");
        orderRequest.setCustomerId(UUID.randomUUID().toString());
        orderRequest.setPaymentMethod("Carte bancaire");

        // Calcul du montant total
        double totalAmount = orderRequest.getOrderDetails().stream()
                .mapToDouble(orderLine -> orderLine.getPrice() * orderLine.getQuantity())
                .sum();

        // Validation de la commande
        OrderResponse orderResponse = orderService.validateOrder(orderRequest);
        if (orderResponse.getOrderId() != null) {
            System.out.println("✅ Commande créée avec succès : " + orderResponse.getOrderId());

            // Création de la requête de paiement
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(orderResponse.getOrderId());
            paymentRequest.setAmount(totalAmount); // Montant total calculé
            paymentRequest.setPaymentMethod(PaymentMethod.APPLE_PAY);
            System.out.println("💰 Montant total de la commande : " + totalAmount);

            // Validation du paiement
            OrderResponse paymentResponse = orderService.payOrder(paymentRequest);
            if (paymentResponse.getStatus() == OrderStatus.VALIDATED) {
                System.out.println("🎉 Paiement validé pour la commande : " + paymentResponse.getOrderId());

                // Traitement de la commande
                boolean processSuccess = orderService.processOrder(orderRequest, paymentRequest);
                if (processSuccess) {
                    System.out.println("📦 Commande pris en compte avec succès : " + paymentResponse.getOrderId());

                    orderService.simuleCommand(paymentRequest);
                } else {
                    System.out.println("Erreur lors du traitement de la commande : " + paymentResponse.getOrderId());
                }
            } else {
                System.out.println("Erreur lors du paiement : " + paymentResponse.getMessage());
            }
        } else {
            System.out.println("Erreur lors de la création de la commande : " + orderResponse.getMessage());
        }
    }
}