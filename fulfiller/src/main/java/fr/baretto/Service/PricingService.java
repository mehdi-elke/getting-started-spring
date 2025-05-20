package fr.baretto.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PricingService {

    private static final Map<String, Double> ZONE_MULTIPLIERS = new HashMap<>();
    private static final double BASE_PRICE_PER_KG = 2.5;
    private static final double BASE_PRICE_PER_M3 = 50.0;
    private static final double MINIMUM_PRICE = 5.0;

    static {
        ZONE_MULTIPLIERS.put("FR", 1.0);
        ZONE_MULTIPLIERS.put("EU", 1.5);
        ZONE_MULTIPLIERS.put("WORLD", 2.5);
    }

    public Double estimate(Double weight, Double volume, String zone) {
        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }
        if (volume == null || volume <= 0) {
            throw new IllegalArgumentException("Volume must be positive");
        }
        if (zone == null || !ZONE_MULTIPLIERS.containsKey(zone)) {
            throw new IllegalArgumentException("Invalid zone: " + zone);
        }

        double weightPrice = weight * BASE_PRICE_PER_KG;
        double volumePrice = volume * BASE_PRICE_PER_M3;
        double basePrice = Math.max(weightPrice, volumePrice);
        double zoneMultiplier = ZONE_MULTIPLIERS.get(zone);
        double finalPrice = basePrice * zoneMultiplier;
        
        return Math.max(finalPrice, MINIMUM_PRICE);
    }
} 