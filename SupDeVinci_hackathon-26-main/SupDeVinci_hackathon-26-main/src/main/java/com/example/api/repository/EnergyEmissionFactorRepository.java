package com.example.api.repository;

import com.example.api.entity.EnergyEmissionFactor;
import com.example.api.enums.EnergySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnergyEmissionFactorRepository extends JpaRepository<EnergyEmissionFactor, UUID> {
    List<EnergyEmissionFactor> findByCountryCode(String countryCode);
    List<EnergyEmissionFactor> findBySource(EnergySource source);
    Optional<EnergyEmissionFactor> findBySourceAndCountryCodeAndYear(EnergySource source, String countryCode, Integer year);
}
