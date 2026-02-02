package com.example.loadoptimizer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record Order(
        @NotBlank String id,
        @Min(0) long payoutCents,
        @Min(0) int weightLbs,
        @Min(0) int volumeCuft,
        @NotBlank String origin,
        @NotBlank String destination,
        @NotNull LocalDate pickupDate,
        @NotNull LocalDate deliveryDate,
        boolean isHazmat
) {
    public boolean isValid() {
        return pickupDate.isBefore(deliveryDate) || pickupDate.isEqual(deliveryDate);
    }
}