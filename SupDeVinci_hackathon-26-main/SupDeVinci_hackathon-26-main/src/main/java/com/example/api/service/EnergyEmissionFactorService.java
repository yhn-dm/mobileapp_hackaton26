package com.example.api.service;

import com.example.api.dto.request.CreateEnergyEmissionFactorRequest;
import com.example.api.dto.request.UpdateEnergyEmissionFactorRequest;
import com.example.api.dto.response.EnergyEmissionFactorResponse;
import com.example.api.entity.EnergyEmissionFactor;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.repository.EnergyEmissionFactorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnergyEmissionFactorService {

    private final EnergyEmissionFactorRepository factorRepository;

    public List<EnergyEmissionFactorResponse> findAll() {
        return factorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EnergyEmissionFactorResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public EnergyEmissionFactorResponse create(CreateEnergyEmissionFactorRequest request) {
        if (factorRepository.findBySourceAndCountryCodeAndYear(
                request.getSource(), request.getCountryCode(), request.getYear()).isPresent()) {
            throw new IllegalArgumentException("Emission factor already exists for this source/country/year combination");
        }
        EnergyEmissionFactor factor = EnergyEmissionFactor.builder()
                .source(request.getSource())
                .countryCode(request.getCountryCode())
                .year(request.getYear())
                .factorKgCo2PerKwh(request.getFactorKgCo2PerKwh())
                .sourceName(request.getSourceName())
                .build();
        return toResponse(factorRepository.save(factor));
    }

    public EnergyEmissionFactorResponse update(UUID id, UpdateEnergyEmissionFactorRequest request) {
        EnergyEmissionFactor factor = findEntityById(id);
        if (request.getSource() != null) factor.setSource(request.getSource());
        if (request.getCountryCode() != null) factor.setCountryCode(request.getCountryCode());
        if (request.getYear() != null) factor.setYear(request.getYear());
        if (request.getFactorKgCo2PerKwh() != null) factor.setFactorKgCo2PerKwh(request.getFactorKgCo2PerKwh());
        if (request.getSourceName() != null) factor.setSourceName(request.getSourceName());
        return toResponse(factorRepository.save(factor));
    }

    public void delete(UUID id) {
        if (!factorRepository.existsById(id)) {
            throw new ResourceNotFoundException("EnergyEmissionFactor", "id", id);
        }
        factorRepository.deleteById(id);
    }

    private EnergyEmissionFactor findEntityById(UUID id) {
        return factorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EnergyEmissionFactor", "id", id));
    }

    private EnergyEmissionFactorResponse toResponse(EnergyEmissionFactor f) {
        return EnergyEmissionFactorResponse.builder()
                .id(f.getId())
                .source(f.getSource())
                .countryCode(f.getCountryCode())
                .year(f.getYear())
                .factorKgCo2PerKwh(f.getFactorKgCo2PerKwh())
                .sourceName(f.getSourceName())
                .build();
    }
}
