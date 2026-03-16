package com.example.api.service;

import com.example.api.dto.request.CreateSiteParkingRequest;
import com.example.api.dto.request.UpdateSiteParkingRequest;
import com.example.api.dto.response.SiteParkingResponse;
import com.example.api.entity.Site;
import com.example.api.entity.SiteParking;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.SiteParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteParkingService {

    private final SiteParkingRepository siteParkingRepository;
    private final SiteService siteService;

    public List<SiteParkingResponse> findBySiteId(UUID siteId) {
        siteService.findEntityById(siteId); // validate site exists
        return siteParkingRepository.findBySiteId(siteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SiteParkingResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public SiteParkingResponse create(UUID siteId, CreateSiteParkingRequest request) {
        Site site = siteService.findEntityById(siteId);
        if (siteParkingRepository.existsBySiteIdAndType(siteId, request.getType())) {
            throw new IllegalArgumentException("Parking of type " + request.getType() + " already exists for this site");
        }
        SiteParking parking = SiteParking.builder()
                .site(site)
                .type(request.getType())
                .count(request.getCount())
                .build();
        return toResponse(siteParkingRepository.save(parking));
    }

    public SiteParkingResponse update(UUID siteId, UUID parkingId, UpdateSiteParkingRequest request) {
        SiteParking parking = findEntityById(parkingId);
        if (!parking.getSite().getId().equals(siteId)) {
            throw new IllegalArgumentException("Parking does not belong to site " + siteId);
        }
        if (request.getType() != null) parking.setType(request.getType());
        if (request.getCount() != null) parking.setCount(request.getCount());
        return toResponse(siteParkingRepository.save(parking));
    }

    public void delete(UUID siteId, UUID parkingId) {
        SiteParking parking = findEntityById(parkingId);
        if (!parking.getSite().getId().equals(siteId)) {
            throw new IllegalArgumentException("Parking does not belong to site " + siteId);
        }
        siteParkingRepository.deleteById(parkingId);
    }

    private SiteParking findEntityById(UUID id) {
        return siteParkingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SiteParking", "id", id));
    }

    private SiteParkingResponse toResponse(SiteParking parking) {
        return SiteParkingResponse.builder()
                .id(parking.getId())
                .siteId(parking.getSite().getId())
                .type(parking.getType())
                .count(parking.getCount())
                .build();
    }
}
