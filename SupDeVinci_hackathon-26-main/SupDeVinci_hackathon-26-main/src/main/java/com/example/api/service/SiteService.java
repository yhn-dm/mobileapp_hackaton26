package com.example.api.service;

import com.example.api.dto.request.CreateSiteRequest;
import com.example.api.dto.request.UpdateSiteRequest;
import com.example.api.dto.response.SiteResponse;
import com.example.api.entity.Site;
import com.example.api.entity.User;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteService {

    private final SiteRepository siteRepository;
    private final UserService userService;

    public List<SiteResponse> findAll() {
        return siteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SiteResponse> findByUserId(UUID userId) {
        return siteRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SiteResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public SiteResponse create(CreateSiteRequest request) {
        User user = userService.findEntityById(request.getUserId());
        Site site = Site.builder()
                .user(user)
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .totalSurfaceM2(request.getTotalSurfaceM2())
                .employeeCount(request.getEmployeeCount())
                .workstationCount(request.getWorkstationCount())
                .constructionYear(request.getConstructionYear())
                .description(request.getDescription())
                .build();
        return toResponse(siteRepository.save(site));
    }

    public SiteResponse update(UUID id, UpdateSiteRequest request) {
        Site site = findEntityById(id);
        if (request.getUserId() != null) {
            User user = userService.findEntityById(request.getUserId());
            site.setUser(user);
        }
        if (request.getName() != null) site.setName(request.getName());
        if (request.getAddress() != null) site.setAddress(request.getAddress());
        if (request.getCity() != null) site.setCity(request.getCity());
        if (request.getTotalSurfaceM2() != null) site.setTotalSurfaceM2(request.getTotalSurfaceM2());
        if (request.getEmployeeCount() != null) site.setEmployeeCount(request.getEmployeeCount());
        if (request.getWorkstationCount() != null) site.setWorkstationCount(request.getWorkstationCount());
        if (request.getConstructionYear() != null) site.setConstructionYear(request.getConstructionYear());
        if (request.getDescription() != null) site.setDescription(request.getDescription());
        return toResponse(siteRepository.save(site));
    }

    public void delete(UUID id) {
        if (!siteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Site", "id", id);
        }
        siteRepository.deleteById(id);
    }

    public Site findEntityById(UUID id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site", "id", id));
    }

    public SiteResponse toResponse(Site site) {
        return SiteResponse.builder()
                .id(site.getId())
                .userId(site.getUser().getId())
                .name(site.getName())
                .address(site.getAddress())
                .city(site.getCity())
                .totalSurfaceM2(site.getTotalSurfaceM2())
                .employeeCount(site.getEmployeeCount())
                .workstationCount(site.getWorkstationCount())
                .constructionYear(site.getConstructionYear())
                .description(site.getDescription())
                .createdAt(site.getCreatedAt())
                .updatedAt(site.getUpdatedAt())
                .build();
    }
}
