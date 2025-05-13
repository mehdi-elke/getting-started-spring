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
        orderService.createOrder("alice.dupont@example.com", "0612345678", "12 rue Lafayette, Paris", "Carte bancaire", "Livraison standard", "1x Laptop Dell XPS 13");
        orderService.createOrder("jean.martin@example.com", "0623456789", "34 avenue Victor Hugo, Lyon", "PayPal", "Livraison express", "2x iPhone 14 Pro");
        orderService.createOrder("sophie.leroy@example.com", "0634567890", "78 boulevard Haussmann, Paris", "Carte bancaire", "Click & Collect", "1x TV LG OLED");
        orderService.createOrder("marc.tremblay@example.com", "0745678901", "15 rue des Lilas, Marseille", "Virement bancaire", "Livraison standard", "3x Chaise de bureau");
        orderService.createOrder("nina.bertrand@example.com", "0656789012", "9 avenue Montaigne, Bordeaux", "Carte bancaire", "Livraison express", "1x Table en bois massif");
        orderService.createOrder("leo.durand@example.com", "0767890123", "4 rue Voltaire, Toulouse", "PayPal", "Livraison standard", "5x Livre 'Clean Code'");
    }
}