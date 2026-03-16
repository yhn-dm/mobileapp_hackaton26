package com.example.api.controller;

import com.example.api.dto.request.CreateCarbonReportDetailRequest;
import com.example.api.dto.request.UpdateCarbonReportDetailRequest;
import com.example.api.dto.response.CarbonReportDetailResponse;
import com.example.api.service.CarbonReportDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carbon-reports/{reportId}/details")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CarbonReportDetailController {

    private final CarbonReportDetailService detailService;

    @GetMapping
    public ResponseEntity<List<CarbonReportDetailResponse>> findByReportId(@PathVariable UUID reportId) {
        return ResponseEntity.ok(detailService.findByReportId(reportId));
    }

    @GetMapping("/{detailId}")
    public ResponseEntity<CarbonReportDetailResponse> findById(
            @PathVariable UUID reportId,
            @PathVariable UUID detailId) {
        return ResponseEntity.ok(detailService.findById(detailId));
    }

    @PostMapping
    public ResponseEntity<CarbonReportDetailResponse> create(
            @PathVariable UUID reportId,
            @Valid @RequestBody CreateCarbonReportDetailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(detailService.create(reportId, request));
    }

    @PutMapping("/{detailId}")
    public ResponseEntity<CarbonReportDetailResponse> update(
            @PathVariable UUID reportId,
            @PathVariable UUID detailId,
            @RequestBody UpdateCarbonReportDetailRequest request) {
        return ResponseEntity.ok(detailService.update(reportId, detailId, request));
    }

    @DeleteMapping("/{detailId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID reportId,
            @PathVariable UUID detailId) {
        detailService.delete(reportId, detailId);
        return ResponseEntity.noContent().build();
    }
}
