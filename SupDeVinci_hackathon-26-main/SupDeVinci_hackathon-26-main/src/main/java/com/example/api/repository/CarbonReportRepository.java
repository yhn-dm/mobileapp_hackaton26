package com.example.api.repository;

import com.example.api.entity.CarbonReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarbonReportRepository extends JpaRepository<CarbonReport, UUID> {
    List<CarbonReport> findBySiteId(UUID siteId);
    List<CarbonReport> findBySiteIdOrderByCalculatedAtDesc(UUID siteId);
}
