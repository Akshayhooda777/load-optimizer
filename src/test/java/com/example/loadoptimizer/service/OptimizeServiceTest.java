package com.example.loadoptimizer.service;

import com.example.loadoptimizer.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OptimizeServiceTest {

    @Autowired
    private com.example.loadoptimizer.service.OptimizeService service;

    @Test
    void testOptimize() {
        Truck truck = new Truck("truck-123", 44000, 3000);
        List<Order> orders = List.of(
                new Order("ord-001", 250000, 18000, 1200, "Los Angeles, CA", "Dallas, TX", LocalDate.parse("2025-12-05"), LocalDate.parse("2025-12-09"), false),
                new Order("ord-002", 180000, 12000, 900, "Los Angeles, CA", "Dallas, TX", LocalDate.parse("2025-12-04"), LocalDate.parse("2025-12-10"), false),
                new Order("ord-003", 320000, 30000, 1800, "Los Angeles, CA", "Dallas, TX", LocalDate.parse("2025-12-06"), LocalDate.parse("2025-12-08"), true)
        );
        OptimizeRequest request = new OptimizeRequest(truck, orders);

        OptimizeResponse response = service.optimize(request);

        assertEquals("truck-123", response.truckId());
        assertEquals(List.of("ord-001", "ord-002"), response.selectedOrderIds());
        assertEquals(430000, response.totalPayoutCents());
        assertEquals(30000, response.totalWeightLbs());
        assertEquals(2100, response.totalVolumeCuft());
    }
}