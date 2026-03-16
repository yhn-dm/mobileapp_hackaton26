package com.example.api.controller;

import com.example.api.dto.request.CreateEnergyEmissionFactorRequest;
import com.example.api.dto.request.UpdateEnergyEmissionFactorRequest;
import com.example.api.dto.response.EnergyEmissionFactorResponse;
import com.example.api.service.EnergyEmissionFactorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/energy-emission-factors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnergyEmissionFactorController {

    private final EnergyEmissionFactorService energyEmissionFactorService;

    @GetMapping
    public ResponseEntity<List<EnergyEmissionFactorResponse>> findAll() {
        return ResponseEntity.ok(energyEmissionFactorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnergyEmissionFactorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(energyEmissionFactorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<EnergyEmissionFactorResponse> create(
            @Valid @RequestBody CreateEnergyEmissionFactorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(energyEmissionFactorService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnergyEmissionFactorResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateEnergyEmissionFactorRequest request) {
        return ResponseEntity.ok(energyEmissionFactorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        energyEmissionFactorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
