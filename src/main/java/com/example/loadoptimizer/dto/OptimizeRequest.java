package com.example.loadoptimizer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OptimizeRequest(
        @NotNull @Valid Truck truck,
        @NotNull @Size(max = 20) List<@Valid Order> orders
) {}