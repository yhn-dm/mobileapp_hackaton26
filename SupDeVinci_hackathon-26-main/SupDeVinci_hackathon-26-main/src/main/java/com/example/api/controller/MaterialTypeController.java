package com.example.api.controller;

import com.example.api.dto.request.CreateMaterialTypeRequest;
import com.example.api.dto.request.UpdateMaterialTypeRequest;
import com.example.api.dto.response.MaterialTypeResponse;
import com.example.api.service.MaterialTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/material-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaterialTypeController {

    private final MaterialTypeService materialTypeService;

    @GetMapping
    public ResponseEntity<List<MaterialTypeResponse>> findAll() {
        return ResponseEntity.ok(materialTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialTypeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(materialTypeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MaterialTypeResponse> create(@Valid @RequestBody CreateMaterialTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialTypeService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialTypeResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateMaterialTypeRequest request) {
        return ResponseEntity.ok(materialTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        materialTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
