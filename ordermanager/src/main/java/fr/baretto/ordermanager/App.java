package fr.baretto.ordermanager;

import fr.baretto.ordermanager.dto.*;
import fr.baretto.ordermanager.model.OrderStatus;
import fr.baretto.ordermanager.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

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
    public void run(String... args) {
        System.out.println("Application démarrée avec succès");

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setStreet("12 rue Lafayette");
        deliveryAddress.setCity("Paris");
        deliveryAddress.setPostalCode("75001");
        deliveryAddress.setZone("77");
        deliveryAddress.setCountry("France");

        OrderLineDTO orderLineDTO = new OrderLineDTO();
        orderLineDTO.setProductId("123456");
        orderLineDTO.setQuantity(1);
        orderLineDTO.setPrice(100.0);
        orderLineDTO.setProductReference("DELL-XPS-13");

        ShipmentDTO shipmentDTO = new ShipmentDTO();
        shipmentDTO.setTrackingNumber("CHR094396875849");
        IndicatorDTO indicatorDTO = new IndicatorDTO();
        indicatorDTO.setEventType("CREATED");
        indicatorDTO.setEventDescription("Commande créée");

        IndicatorDTO indicatorDTO2 = new IndicatorDTO();
        indicatorDTO2.setEventType("VALIDATED");
        indicatorDTO2.setEventDescription("Commande validée");

        IndicatorDTO indicatorDTO3 = new IndicatorDTO();
        indicatorDTO3.setEventType("IN_PREPARATION");
        indicatorDTO3.setEventDescription("Commande en préparation");
        shipmentDTO.setIndicators(List.of(indicatorDTO, indicatorDTO2, indicatorDTO3));

        orderLineDTO.setShipment(List.of(shipmentDTO));
        orderLineDTO.setProductReference("DELL-XPS-13");


        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setEmail("alice.dupont@example.com");
        orderRequest.setPhoneNumber("0612345678");
        orderRequest.setDeliveryAddress(deliveryAddress);
        orderRequest.setOrderDetails(List.of(orderLineDTO));
        orderRequest.setOrderTracking("CHR094396875849");
        orderRequest.setPaymentMethod("Carte bancaire");

        // Validation de la commande
        OrderResponse orderResponse = orderService.validateOrder(orderRequest);
        if (orderResponse.getOrderId() != null) {
            System.out.println("Commande créée avec succès : " + orderResponse.getOrderId());

            // Création de la requête de paiement
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(orderResponse.getOrderId());
            paymentRequest.setAmount(100.0);
            paymentRequest.setPaymentMethod("Carte bancaire");

            // Validation du paiement
            OrderResponse paymentResponse = orderService.payOrder(paymentRequest);
            if (paymentResponse.getStatus() == OrderStatus.VALIDATED) {
                System.out.println("Paiement validé pour la commande : " + paymentResponse.getOrderId());

                // Traitement de la commande
                boolean processSuccess = orderService.processOrder(orderRequest);
                if (processSuccess) {
                    System.out.println("Commande traitée avec succès : " + paymentResponse.getOrderId());
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