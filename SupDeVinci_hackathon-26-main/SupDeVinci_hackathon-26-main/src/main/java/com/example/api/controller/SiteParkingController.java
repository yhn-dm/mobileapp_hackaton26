package com.example.api.controller;

import com.example.api.dto.request.CreateSiteParkingRequest;
import com.example.api.dto.request.UpdateSiteParkingRequest;
import com.example.api.dto.response.SiteParkingResponse;
import com.example.api.service.SiteParkingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sites/{siteId}/parking")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SiteParkingController {

    private final SiteParkingService siteParkingService;

    @GetMapping
    public ResponseEntity<List<SiteParkingResponse>> findBySiteId(@PathVariable UUID siteId) {
        return ResponseEntity.ok(siteParkingService.findBySiteId(siteId));
    }

    @GetMapping("/{parkingId}")
    public ResponseEntity<SiteParkingResponse> findById(
            @PathVariable UUID siteId,
            @PathVariable UUID parkingId) {
        return ResponseEntity.ok(siteParkingService.findById(parkingId));
    }

    @PostMapping
    public ResponseEntity<SiteParkingResponse> create(
            @PathVariable UUID siteId,
            @Valid @RequestBody CreateSiteParkingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(siteParkingService.create(siteId, request));
    }

    @PutMapping("/{parkingId}")
    public ResponseEntity<SiteParkingResponse> update(
            @PathVariable UUID siteId,
            @PathVariable UUID parkingId,
            @RequestBody UpdateSiteParkingRequest request) {
        return ResponseEntity.ok(siteParkingService.update(siteId, parkingId, request));
    }

    @DeleteMapping("/{parkingId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID siteId,
            @PathVariable UUID parkingId) {
        siteParkingService.delete(siteId, parkingId);
        return ResponseEntity.noContent().build();
    }
}
