package fr.baretto.ordermanager.controller;

import fr.baretto.ordermanager.dto.OrderRequest;
import fr.baretto.ordermanager.dto.OrderResponse;
import fr.baretto.ordermanager.dto.PaymentRequest;
import fr.baretto.ordermanager.exception.OrderException;
import fr.baretto.ordermanager.model.Order;
import fr.baretto.ordermanager.service.OrderService;
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
        OrderResponse response = orderService.validateOrder(request);
        HttpStatus status = (response.getOrderId() != null) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/pay")
    public ResponseEntity<OrderResponse> payOrder(@RequestBody PaymentRequest request) {
        OrderResponse response = orderService.payOrder(request);
        HttpStatus status = (response.getOrderId() != null) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
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

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<OrderResponse> handleOrderException(OrderException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new OrderResponse(ex.getMessage()));
    }
}