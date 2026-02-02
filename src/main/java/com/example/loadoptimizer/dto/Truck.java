package com.example.loadoptimizer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record Truck(
        @NotBlank String id,
        @Min(1) int maxWeightLbs,
        @Min(1) int maxVolumeCuft
) {}