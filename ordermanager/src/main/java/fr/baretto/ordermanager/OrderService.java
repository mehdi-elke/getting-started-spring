package fr.baretto.ordermanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {

    // Vérification du stock via Inventory Service
    // Si stock OK, passer l'état à CREATED

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
        return order;
    }

    // Appel au Payment Service pour valider le paiement
    // Si paiement OK, passer l'état à VALIDATED

    public Order payOrder() {
        return null;
    }
}
