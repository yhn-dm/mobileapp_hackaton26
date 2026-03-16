package com.example.api.controller;

import com.example.api.dto.request.CreateCarbonReportRequest;
import com.example.api.dto.request.UpdateCarbonReportRequest;
import com.example.api.dto.response.CarbonReportResponse;
import com.example.api.service.CarbonReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carbon-reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CarbonReportController {

    private final CarbonReportService carbonReportService;

    @GetMapping
    public ResponseEntity<List<CarbonReportResponse>> findAll(
            @RequestParam(required = false) UUID siteId) {
        if (siteId != null) {
            return ResponseEntity.ok(carbonReportService.findBySiteId(siteId));
        }
        return ResponseEntity.ok(carbonReportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarbonReportResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(carbonReportService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CarbonReportResponse> create(@Valid @RequestBody CreateCarbonReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carbonReportService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarbonReportResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateCarbonReportRequest request) {
        return ResponseEntity.ok(carbonReportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        carbonReportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
