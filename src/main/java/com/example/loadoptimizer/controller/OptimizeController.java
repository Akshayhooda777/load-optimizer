package com.example.loadoptimizer.controller;

import com.example.loadoptimizer.dto.OptimizeRequest;
import com.example.loadoptimizer.dto.OptimizeResponse;
import com.example.loadoptimizer.exception.InvalidInputException;
import com.example.loadoptimizer.service.OptimizeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/load-optimizer")
public class OptimizeController {

    private final OptimizeService service;

    public OptimizeController(OptimizeService service) {
        this.service = service;
    }

    @PostMapping("/optimize")
    public ResponseEntity<OptimizeResponse> optimize(@Valid @RequestBody OptimizeRequest request) {
        try {
            OptimizeResponse response = service.optimize(request);
            return ResponseEntity.ok(response);
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}