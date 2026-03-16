package com.example.api.service;

import com.example.api.dto.request.CreateMaterialTypeRequest;
import com.example.api.dto.request.UpdateMaterialTypeRequest;
import com.example.api.dto.response.MaterialTypeResponse;
import com.example.api.entity.MaterialType;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.MaterialTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialTypeService {

    private final MaterialTypeRepository materialTypeRepository;

    public List<MaterialTypeResponse> findAll() {
        return materialTypeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MaterialTypeResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public MaterialTypeResponse create(CreateMaterialTypeRequest request) {
        if (materialTypeRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Material type with code '" + request.getCode() + "' already exists");
        }
        MaterialType materialType = MaterialType.builder()
                .code(request.getCode())
                .name(request.getName())
                .unit(request.getUnit())
                .co2FactorKgPerUnit(request.getCo2FactorKgPerUnit())
                .source(request.getSource())
                .description(request.getDescription())
                .build();
        return toResponse(materialTypeRepository.save(materialType));
    }

    public MaterialTypeResponse update(UUID id, UpdateMaterialTypeRequest request) {
        MaterialType materialType = findEntityById(id);
        if (request.getCode() != null) {
            if (!request.getCode().equals(materialType.getCode()) && materialTypeRepository.existsByCode(request.getCode())) {
                throw new IllegalArgumentException("Material type with code '" + request.getCode() + "' already exists");
            }
            materialType.setCode(request.getCode());
        }
        if (request.getName() != null) materialType.setName(request.getName());
        if (request.getUnit() != null) materialType.setUnit(request.getUnit());
        if (request.getCo2FactorKgPerUnit() != null) materialType.setCo2FactorKgPerUnit(request.getCo2FactorKgPerUnit());
        if (request.getSource() != null) materialType.setSource(request.getSource());
        if (request.getDescription() != null) materialType.setDescription(request.getDescription());
        return toResponse(materialTypeRepository.save(materialType));
    }

    public void delete(UUID id) {
        if (!materialTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("MaterialType", "id", id);
        }
        materialTypeRepository.deleteById(id);
    }

    public MaterialType findEntityById(UUID id) {
        return materialTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialType", "id", id));
    }

    private MaterialTypeResponse toResponse(MaterialType m) {
        return MaterialTypeResponse.builder()
                .id(m.getId())
                .code(m.getCode())
                .name(m.getName())
                .unit(m.getUnit())
                .co2FactorKgPerUnit(m.getCo2FactorKgPerUnit())
                .source(m.getSource())
                .description(m.getDescription())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}
