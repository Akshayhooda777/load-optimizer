package com.example.loadoptimizer.service;

import com.example.loadoptimizer.dto.OptimizeRequest;
import com.example.loadoptimizer.dto.OptimizeResponse;
import com.example.loadoptimizer.dto.Order;
import com.example.loadoptimizer.dto.Truck;
import com.example.loadoptimizer.exception.InvalidInputException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class OptimizeService {

    // Simple in-memory cache for repeated requests (bonus: could use Caffeine)
    private final Map<String, OptimizeResponse> cache = new HashMap<>();

    public OptimizeResponse optimize(OptimizeRequest request) {
        Truck truck = request.truck();
        List<Order> orders = request.orders();

        // Validate payload size
        if (orders.size() > 20) {
            throw new InvalidInputException("Payload too large: max 20 orders");
        }

        // Validate orders
        for (Order order : orders) {
            if (!order.isValid()) {
                throw new InvalidInputException("Invalid order: " + order.id() + " has invalid dates");
            }
        }

        // Cache key: hash of request (simple string concat for demo)
        String cacheKey = truck.id() + orders.toString();
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        // If no orders, return empty
        if (orders.isEmpty()) {
            return buildEmptyResponse(truck);
        }

        int n = orders.size();
        long bestRevenue = 0;
        int bestMask = 0;

        // Bitmask DP: Check all 2^n subsets
        for (int mask = 0; mask < (1 << n); mask++) {
            long revenue = 0;
            int weight = 0;
            int volume = 0;
            String origin = null;
            String dest = null;
            Boolean hazmat = null;

            boolean valid = true;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    Order o = orders.get(i);
                    revenue += o.payoutCents();
                    weight += o.weightLbs();
                    volume += o.volumeCuft();

                    // Check compatibility
                    if (origin == null) {
                        origin = o.origin();
                        dest = o.destination();
                        hazmat = o.isHazmat();
                    } else if (!origin.equals(o.origin()) || !dest.equals(o.destination()) || !hazmat.equals(o.isHazmat())) {
                        valid = false;
                        break;
                    }
                }
            }

            if (valid && weight <= truck.maxWeightLbs() && volume <= truck.maxVolumeCuft() && revenue > bestRevenue) {
                bestRevenue = revenue;
                bestMask = mask;
            }
        }

        // Build response from best mask
        List<String> selectedIds = new ArrayList<>();
        int totalWeight = 0;
        int totalVolume = 0;
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) {
                Order o = orders.get(i);
                selectedIds.add(o.id());
                totalWeight += o.weightLbs();
                totalVolume += o.volumeCuft();
            }
        }

        BigDecimal weightUtil = totalWeight == 0 ? BigDecimal.ZERO :
                BigDecimal.valueOf(totalWeight).divide(BigDecimal.valueOf(truck.maxWeightLbs()), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal volumeUtil = totalVolume == 0 ? BigDecimal.ZERO :
                BigDecimal.valueOf(totalVolume).divide(BigDecimal.valueOf(truck.maxVolumeCuft()), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        OptimizeResponse response = new OptimizeResponse(
                truck.id(), selectedIds, bestRevenue, totalWeight, totalVolume, weightUtil, volumeUtil
        );

        cache.put(cacheKey, response);
        return response;
    }

    private OptimizeResponse buildEmptyResponse(Truck truck) {
        return new OptimizeResponse(truck.id(), List.of(), 0L, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}