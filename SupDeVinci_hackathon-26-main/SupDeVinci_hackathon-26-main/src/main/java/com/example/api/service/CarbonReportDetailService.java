package com.example.api.service;

import com.example.api.dto.request.CreateCarbonReportDetailRequest;
import com.example.api.dto.request.UpdateCarbonReportDetailRequest;
import com.example.api.dto.response.CarbonReportDetailResponse;
import com.example.api.entity.CarbonReport;
import com.example.api.entity.CarbonReportDetail;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.CarbonReportDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CarbonReportDetailService {

    private final CarbonReportDetailRepository detailRepository;
    private final CarbonReportService carbonReportService;

    public List<CarbonReportDetailResponse> findByReportId(UUID reportId) {
        carbonReportService.findEntityById(reportId);
        return detailRepository.findByReportId(reportId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CarbonReportDetailResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public CarbonReportDetailResponse create(UUID reportId, CreateCarbonReportDetailRequest request) {
        CarbonReport report = carbonReportService.findEntityById(reportId);
        if (detailRepository.findByReportIdAndCategory(reportId, request.getCategory()).isPresent()) {
            throw new IllegalArgumentException("Detail with category " + request.getCategory() + " already exists for this report");
        }
        CarbonReportDetail detail = CarbonReportDetail.builder()
                .report(report)
                .category(request.getCategory())
                .co2Kg(request.getCo2Kg())
                .percentage(request.getPercentage())
                .build();
        return toResponse(detailRepository.save(detail));
    }

    public CarbonReportDetailResponse update(UUID reportId, UUID detailId, UpdateCarbonReportDetailRequest request) {
        CarbonReportDetail detail = findEntityById(detailId);
        if (!detail.getReport().getId().equals(reportId)) {
            throw new IllegalArgumentException("Report detail does not belong to report " + reportId);
        }
        if (request.getCategory() != null) detail.setCategory(request.getCategory());
        if (request.getCo2Kg() != null) detail.setCo2Kg(request.getCo2Kg());
        if (request.getPercentage() != null) detail.setPercentage(request.getPercentage());
        return toResponse(detailRepository.save(detail));
    }

    public void delete(UUID reportId, UUID detailId) {
        CarbonReportDetail detail = findEntityById(detailId);
        if (!detail.getReport().getId().equals(reportId)) {
            throw new IllegalArgumentException("Report detail does not belong to report " + reportId);
        }
        detailRepository.deleteById(detailId);
    }

    private CarbonReportDetail findEntityById(UUID id) {
        return detailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CarbonReportDetail", "id", id));
    }

    private CarbonReportDetailResponse toResponse(CarbonReportDetail d) {
        return CarbonReportDetailResponse.builder()
                .id(d.getId())
                .reportId(d.getReport().getId())
                .category(d.getCategory())
                .co2Kg(d.getCo2Kg())
                .percentage(d.getPercentage())
                .build();
    }
}
