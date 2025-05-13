// File: ordermanager/src/main/java/fr/baretto/ordermanager/App.java
package fr.baretto.ordermanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setEmail("alice.dupont@example.com");
        orderRequest.setPhoneNumber("0612345678");
        orderRequest.setAddress("12 rue Lafayette, Paris");
        orderRequest.setDeliveryMethod("Chronopost");
        orderRequest.setDeliveryZone("Zone 1");
        orderRequest.setOrderDetails("1x Laptop Dell XPS 13");
        orderRequest.setOrderTracking("CHR094396875849");
        orderRequest.setPaymentMethod("Carte bancaire");

        OrderResponse response = orderService.validateOrder(orderRequest);
        if (response.getOrderId() != null) {
            System.out.println("Commande créée avec succès : " + response.getOrderId());
        } else {
            System.out.println("Erreur lors de la création de la commande : " + response.getMessage());
        }
    }
}