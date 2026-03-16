package com.example.api.service;

import com.example.api.dto.request.CreateCarbonReportRequest;
import com.example.api.dto.request.UpdateCarbonReportRequest;
import com.example.api.dto.response.CarbonReportResponse;
import com.example.api.entity.CarbonReport;
import com.example.api.entity.Site;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.CarbonReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CarbonReportService {

    private final CarbonReportRepository carbonReportRepository;
    private final SiteService siteService;

    public List<CarbonReportResponse> findAll() {
        return carbonReportRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CarbonReportResponse> findBySiteId(UUID siteId) {
        siteService.findEntityById(siteId);
        return carbonReportRepository.findBySiteIdOrderByCalculatedAtDesc(siteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CarbonReportResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public CarbonReportResponse create(CreateCarbonReportRequest request) {
        Site site = siteService.findEntityById(request.getSiteId());
        CarbonReport report = CarbonReport.builder()
                .site(site)
                .calculatedAt(LocalDateTime.now())
                .referenceYear(request.getReferenceYear())
                .constructionCo2Kg(request.getConstructionCo2Kg())
                .exploitationCo2Kg(request.getExploitationCo2Kg())
                .co2PerM2(request.getCo2PerM2())
                .co2PerEmployee(request.getCo2PerEmployee())
                .notes(request.getNotes())
                .build();
        return toResponse(carbonReportRepository.save(report));
    }

    public CarbonReportResponse update(UUID id, UpdateCarbonReportRequest request) {
        CarbonReport report = findEntityById(id);
        if (request.getReferenceYear() != null) report.setReferenceYear(request.getReferenceYear());
        if (request.getConstructionCo2Kg() != null) report.setConstructionCo2Kg(request.getConstructionCo2Kg());
        if (request.getExploitationCo2Kg() != null) report.setExploitationCo2Kg(request.getExploitationCo2Kg());
        if (request.getCo2PerM2() != null) report.setCo2PerM2(request.getCo2PerM2());
        if (request.getCo2PerEmployee() != null) report.setCo2PerEmployee(request.getCo2PerEmployee());
        if (request.getNotes() != null) report.setNotes(request.getNotes());
        return toResponse(carbonReportRepository.save(report));
    }

    public void delete(UUID id) {
        if (!carbonReportRepository.existsById(id)) {
            throw new ResourceNotFoundException("CarbonReport", "id", id);
        }
        carbonReportRepository.deleteById(id);
    }

    public CarbonReport findEntityById(UUID id) {
        return carbonReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CarbonReport", "id", id));
    }

    private CarbonReportResponse toResponse(CarbonReport r) {
        return CarbonReportResponse.builder()
                .id(r.getId())
                .siteId(r.getSite().getId())
                .calculatedAt(r.getCalculatedAt())
                .referenceYear(r.getReferenceYear())
                .constructionCo2Kg(r.getConstructionCo2Kg())
                .exploitationCo2Kg(r.getExploitationCo2Kg())
                .totalCo2Kg(r.getTotalCo2Kg())
                .co2PerM2(r.getCo2PerM2())
                .co2PerEmployee(r.getCo2PerEmployee())
                .notes(r.getNotes())
                .build();
    }
}
