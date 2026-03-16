package com.example.api.repository;

import com.example.api.entity.CarbonReportDetail;
import com.example.api.enums.ReportCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarbonReportDetailRepository extends JpaRepository<CarbonReportDetail, UUID> {
    List<CarbonReportDetail> findByReportId(UUID reportId);
    Optional<CarbonReportDetail> findByReportIdAndCategory(UUID reportId, ReportCategory category);
}
