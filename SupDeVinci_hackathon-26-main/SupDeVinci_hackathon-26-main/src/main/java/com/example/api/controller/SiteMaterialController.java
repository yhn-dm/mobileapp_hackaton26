package com.example.api.controller;

import com.example.api.dto.request.CreateSiteMaterialRequest;
import com.example.api.dto.request.UpdateSiteMaterialRequest;
import com.example.api.dto.response.SiteMaterialResponse;
import com.example.api.service.SiteMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sites/{siteId}/materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SiteMaterialController {

    private final SiteMaterialService siteMaterialService;

    @GetMapping
    public ResponseEntity<List<SiteMaterialResponse>> findBySiteId(@PathVariable UUID siteId) {
        return ResponseEntity.ok(siteMaterialService.findBySiteId(siteId));
    }

    @GetMapping("/{materialId}")
    public ResponseEntity<SiteMaterialResponse> findById(
            @PathVariable UUID siteId,
            @PathVariable UUID materialId) {
        return ResponseEntity.ok(siteMaterialService.findById(materialId));
    }

    @PostMapping
    public ResponseEntity<SiteMaterialResponse> create(
            @PathVariable UUID siteId,
            @Valid @RequestBody CreateSiteMaterialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(siteMaterialService.create(siteId, request));
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<SiteMaterialResponse> update(
            @PathVariable UUID siteId,
            @PathVariable UUID materialId,
            @RequestBody UpdateSiteMaterialRequest request) {
        return ResponseEntity.ok(siteMaterialService.update(siteId, materialId, request));
    }

    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID siteId,
            @PathVariable UUID materialId) {
        siteMaterialService.delete(siteId, materialId);
        return ResponseEntity.noContent().build();
    }
}
