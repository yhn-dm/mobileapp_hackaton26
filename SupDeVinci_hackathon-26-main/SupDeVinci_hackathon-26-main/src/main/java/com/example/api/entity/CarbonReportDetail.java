package com.example.api.entity;

import com.example.api.enums.ReportCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "carbon_report_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"report_id", "category"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarbonReportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CarbonReport report;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "report_category")
    private ReportCategory category;

    @Column(name = "co2_kg")
    private BigDecimal co2Kg;

    private BigDecimal percentage;
}
