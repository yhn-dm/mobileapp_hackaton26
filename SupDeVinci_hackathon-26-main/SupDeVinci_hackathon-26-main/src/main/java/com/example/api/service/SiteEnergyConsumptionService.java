package com.example.api.service;

import com.example.api.dto.request.CreateSiteEnergyConsumptionRequest;
import com.example.api.dto.request.UpdateSiteEnergyConsumptionRequest;
import com.example.api.dto.response.SiteEnergyConsumptionResponse;
import com.example.api.entity.Site;
import com.example.api.entity.SiteEnergyConsumption;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.SiteEnergyConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteEnergyConsumptionService {

    private final SiteEnergyConsumptionRepository consumptionRepository;
    private final SiteService siteService;

    public List<SiteEnergyConsumptionResponse> findBySiteId(UUID siteId) {
        siteService.findEntityById(siteId);
        return consumptionRepository.findBySiteId(siteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SiteEnergyConsumptionResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public SiteEnergyConsumptionResponse create(UUID siteId, CreateSiteEnergyConsumptionRequest request) {
        Site site = siteService.findEntityById(siteId);
        if (consumptionRepository.findBySiteIdAndYearAndSource(siteId, request.getYear(), request.getSource()).isPresent()) {
            throw new IllegalArgumentException(
                    "Energy consumption for source " + request.getSource() + " and year " + request.getYear() + " already exists");
        }
        SiteEnergyConsumption consumption = SiteEnergyConsumption.builder()
                .site(site)
                .year(request.getYear())
                .source(request.getSource())
                .consumptionMwh(request.getConsumptionMwh())
                .build();
        return toResponse(consumptionRepository.save(consumption));
    }

    public SiteEnergyConsumptionResponse update(UUID siteId, UUID consumptionId, UpdateSiteEnergyConsumptionRequest request) {
        SiteEnergyConsumption consumption = findEntityById(consumptionId);
        if (!consumption.getSite().getId().equals(siteId)) {
            throw new IllegalArgumentException("Energy consumption does not belong to site " + siteId);
        }
        if (request.getYear() != null) consumption.setYear(request.getYear());
        if (request.getSource() != null) consumption.setSource(request.getSource());
        if (request.getConsumptionMwh() != null) consumption.setConsumptionMwh(request.getConsumptionMwh());
        return toResponse(consumptionRepository.save(consumption));
    }

    public void delete(UUID siteId, UUID consumptionId) {
        SiteEnergyConsumption consumption = findEntityById(consumptionId);
        if (!consumption.getSite().getId().equals(siteId)) {
            throw new IllegalArgumentException("Energy consumption does not belong to site " + siteId);
        }
        consumptionRepository.deleteById(consumptionId);
    }

    private SiteEnergyConsumption findEntityById(UUID id) {
        return consumptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SiteEnergyConsumption", "id", id));
    }

    private SiteEnergyConsumptionResponse toResponse(SiteEnergyConsumption c) {
        return SiteEnergyConsumptionResponse.builder()
                .id(c.getId())
                .siteId(c.getSite().getId())
                .year(c.getYear())
                .source(c.getSource())
                .consumptionMwh(c.getConsumptionMwh())
                .build();
    }
}
