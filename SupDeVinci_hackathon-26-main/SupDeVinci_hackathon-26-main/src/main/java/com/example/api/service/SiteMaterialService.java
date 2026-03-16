package com.example.api.service;

import com.example.api.dto.request.CreateSiteMaterialRequest;
import com.example.api.dto.request.UpdateSiteMaterialRequest;
import com.example.api.dto.response.SiteMaterialResponse;
import com.example.api.entity.MaterialType;
import com.example.api.entity.Site;
import com.example.api.entity.SiteMaterial;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.SiteMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteMaterialService {

    private final SiteMaterialRepository siteMaterialRepository;
    private final SiteService siteService;
    private final MaterialTypeService materialTypeService;

    public List<SiteMaterialResponse> findBySiteId(UUID siteId) {
        siteService.findEntityById(siteId);
        return siteMaterialRepository.findBySiteId(siteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SiteMaterialResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public SiteMaterialResponse create(UUID siteId, CreateSiteMaterialRequest request) {
        Site site = siteService.findEntityById(siteId);
        MaterialType materialType = materialTypeService.findEntityById(request.getMaterialTypeId());
        if (siteMaterialRepository.existsBySiteIdAndMaterialTypeId(siteId, request.getMaterialTypeId())) {
            throw new IllegalArgumentException("Material type already linked to this site");
        }
        SiteMaterial siteMaterial = SiteMaterial.builder()
                .site(site)
                .materialType(materialType)
                .quantity(request.getQuantity())
                .build();
        return toResponse(siteMaterialRepository.save(siteMaterial));
    }

    public SiteMaterialResponse update(UUID siteId, UUID materialId, UpdateSiteMaterialRequest request) {
        SiteMaterial siteMaterial = findEntityById(materialId);
        if (!siteMaterial.getSite().getId().equals(siteId)) {
            throw new IllegalArgumentException("Site material does not belong to site " + siteId);
        }
        if (request.getMaterialTypeId() != null) {
            MaterialType materialType = materialTypeService.findEntityById(request.getMaterialTypeId());
            siteMaterial.setMaterialType(materialType);
        }
        if (request.getQuantity() != null) siteMaterial.setQuantity(request.getQuantity());
        return toResponse(siteMaterialRepository.save(siteMaterial));
    }

    public void delete(UUID siteId, UUID materialId) {
        SiteMaterial siteMaterial = findEntityById(materialId);
        if (!siteMaterial.getSite().getId().equals(siteId)) {
            throw new IllegalArgumentException("Site material does not belong to site " + siteId);
        }
        siteMaterialRepository.deleteById(materialId);
    }

    private SiteMaterial findEntityById(UUID id) {
        return siteMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SiteMaterial", "id", id));
    }

    private SiteMaterialResponse toResponse(SiteMaterial sm) {
        return SiteMaterialResponse.builder()
                .id(sm.getId())
                .siteId(sm.getSite().getId())
                .materialTypeId(sm.getMaterialType().getId())
                .materialTypeCode(sm.getMaterialType().getCode())
                .materialTypeName(sm.getMaterialType().getName())
                .quantity(sm.getQuantity())
                .build();
    }
}
