package fr.baretto.ordermanager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    //POST /orders/validate
    // - Entrée : Créer une commande (produits + adresse de livraison)
    // - Sortie : Commande en CREATED si stock OK

    @PostMapping("/validate")
    public ResponseEntity<?> createOrder() {
        // Appel Inventory Service pour check
        // Si OK : enregistrer la commande en base avec status CREATED
        return null;
    }

    //POST /orders/pay
    // - Entrée : Informations paiement
    // - Sortie : Commande VALIDATED ou refusée

    @PostMapping("/pay")
    public ResponseEntity<?> payOrder() {
        // Appel Payment Service
        // Si OK : mettre status VALIDATED
        // Réserver le stock
        // Appeler Fulfillment Service
        return null;
    }
}
