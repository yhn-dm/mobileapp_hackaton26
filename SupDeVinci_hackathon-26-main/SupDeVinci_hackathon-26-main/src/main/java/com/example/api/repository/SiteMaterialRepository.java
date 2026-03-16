package com.example.api.repository;

import com.example.api.entity.SiteMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SiteMaterialRepository extends JpaRepository<SiteMaterial, UUID> {
    List<SiteMaterial> findBySiteId(UUID siteId);
    Optional<SiteMaterial> findBySiteIdAndMaterialTypeId(UUID siteId, UUID materialTypeId);
    boolean existsBySiteIdAndMaterialTypeId(UUID siteId, UUID materialTypeId);
}
