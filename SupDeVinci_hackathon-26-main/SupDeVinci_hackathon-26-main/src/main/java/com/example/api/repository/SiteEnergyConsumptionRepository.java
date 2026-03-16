package com.example.api.repository;

import com.example.api.entity.SiteEnergyConsumption;
import com.example.api.enums.EnergySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SiteEnergyConsumptionRepository extends JpaRepository<SiteEnergyConsumption, UUID> {
    List<SiteEnergyConsumption> findBySiteId(UUID siteId);
    List<SiteEnergyConsumption> findBySiteIdAndYear(UUID siteId, Integer year);
    Optional<SiteEnergyConsumption> findBySiteIdAndYearAndSource(UUID siteId, Integer year, EnergySource source);
}
