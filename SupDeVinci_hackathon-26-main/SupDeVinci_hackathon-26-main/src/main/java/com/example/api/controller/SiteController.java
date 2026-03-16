package com.example.api.controller;

import com.example.api.dto.request.CreateSiteRequest;
import com.example.api.dto.request.UpdateSiteRequest;
import com.example.api.dto.response.SiteResponse;
import com.example.api.service.SiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SiteController {

    private final SiteService siteService;

    @GetMapping
    public ResponseEntity<List<SiteResponse>> findAll(
            @RequestParam(required = false) UUID userId) {
        if (userId != null) {
            return ResponseEntity.ok(siteService.findByUserId(userId));
        }
        return ResponseEntity.ok(siteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SiteResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(siteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SiteResponse> create(@Valid @RequestBody CreateSiteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(siteService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SiteResponse> update(@PathVariable UUID id, @RequestBody UpdateSiteRequest request) {
        return ResponseEntity.ok(siteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        siteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
