package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carbon_reports")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarbonReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Site site;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @Column(name = "reference_year")
    private Integer referenceYear;

    @Column(name = "construction_co2_kg")
    private BigDecimal constructionCo2Kg;

    @Column(name = "exploitation_co2_kg")
    private BigDecimal exploitationCo2Kg;

    @Column(name = "total_co2_kg", insertable = false, updatable = false)
    private BigDecimal totalCo2Kg;

    @Column(name = "co2_per_m2")
    private BigDecimal co2PerM2;

    @Column(name = "co2_per_employee")
    private BigDecimal co2PerEmployee;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CarbonReportDetail> details;
}
