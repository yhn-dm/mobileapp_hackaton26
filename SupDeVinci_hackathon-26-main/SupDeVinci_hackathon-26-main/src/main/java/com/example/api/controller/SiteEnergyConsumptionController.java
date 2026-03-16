package com.example.api.controller;

import com.example.api.dto.request.CreateSiteEnergyConsumptionRequest;
import com.example.api.dto.request.UpdateSiteEnergyConsumptionRequest;
import com.example.api.dto.response.SiteEnergyConsumptionResponse;
import com.example.api.service.SiteEnergyConsumptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sites/{siteId}/energy-consumptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SiteEnergyConsumptionController {

    private final SiteEnergyConsumptionService energyConsumptionService;

    @GetMapping
    public ResponseEntity<List<SiteEnergyConsumptionResponse>> findBySiteId(@PathVariable UUID siteId) {
        return ResponseEntity.ok(energyConsumptionService.findBySiteId(siteId));
    }

    @GetMapping("/{consumptionId}")
    public ResponseEntity<SiteEnergyConsumptionResponse> findById(
            @PathVariable UUID siteId,
            @PathVariable UUID consumptionId) {
        return ResponseEntity.ok(energyConsumptionService.findById(consumptionId));
    }

    @PostMapping
    public ResponseEntity<SiteEnergyConsumptionResponse> create(
            @PathVariable UUID siteId,
            @Valid @RequestBody CreateSiteEnergyConsumptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(energyConsumptionService.create(siteId, request));
    }

    @PutMapping("/{consumptionId}")
    public ResponseEntity<SiteEnergyConsumptionResponse> update(
            @PathVariable UUID siteId,
            @PathVariable UUID consumptionId,
            @RequestBody UpdateSiteEnergyConsumptionRequest request) {
        return ResponseEntity.ok(energyConsumptionService.update(siteId, consumptionId, request));
    }

    @DeleteMapping("/{consumptionId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID siteId,
            @PathVariable UUID consumptionId) {
        energyConsumptionService.delete(siteId, consumptionId);
        return ResponseEntity.noContent().build();
    }
}
