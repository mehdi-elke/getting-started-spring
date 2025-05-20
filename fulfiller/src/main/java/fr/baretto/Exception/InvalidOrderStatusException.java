package fr.baretto.Exception;

import fr.baretto.Enumeration.FulfillmentStatus;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(FulfillmentStatus currentStatus, FulfillmentStatus expectedStatus) {
        super(String.format("Invalid order status: %s. Expected: %s", currentStatus, expectedStatus));
    }
} 