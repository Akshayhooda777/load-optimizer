package com.example.loadoptimizer.dto;

import java.math.BigDecimal;
import java.util.List;

public record OptimizeResponse(
        String truckId,
        List<String> selectedOrderIds,
        long totalPayoutCents,
        int totalWeightLbs,
        int totalVolumeCuft,
        BigDecimal utilizationWeightPercent,
        BigDecimal utilizationVolumePercent
) {}