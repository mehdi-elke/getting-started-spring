package fr.baretto.ordermanager;

import fr.baretto.ordermanager.OrderRequest;
import fr.baretto.ordermanager.OrderResponse;
import fr.baretto.ordermanager.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/validate")
    public ResponseEntity<OrderResponse> validateOrder(@RequestBody OrderRequest request) {
        Order order = orderService.validateOrder(request);

        if (order == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new OrderResponse("Stock insuffisant ou livraison indisponible"));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new OrderResponse(order.getOrderId(), order.getStatus(), order.getCreationDate()));
    }

    @PostMapping("/pay")
    public ResponseEntity<OrderResponse> payOrder(@RequestBody PaymentRequest request) {
        Order order = orderService.payOrder(request);

        if (order == null) {
            Optional<Order> existingOrder = orderService.getOrderById(request.getOrderId());

            if (existingOrder.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new OrderResponse("Commande non trouvée"));
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new OrderResponse("Paiement refusé ou commande dans un état invalide"));
            }
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new OrderResponse(order.getOrderId(), order.getStatus(), order.getCreationDate()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new OrderResponse("Commande non trouvée"));
        }

        Order order = optionalOrder.get();
        return ResponseEntity.ok(new OrderResponse(order.getOrderId(), order.getStatus(), order.getCreationDate()));
    }
}