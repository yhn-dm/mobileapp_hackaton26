package com.example.api.repository;

import com.example.api.entity.SiteParking;
import com.example.api.enums.ParkingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SiteParkingRepository extends JpaRepository<SiteParking, UUID> {
    List<SiteParking> findBySiteId(UUID siteId);
    Optional<SiteParking> findBySiteIdAndType(UUID siteId, ParkingType type);
    boolean existsBySiteIdAndType(UUID siteId, ParkingType type);
}
