package com.example.api.entity;

import com.example.api.enums.EnergySource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "site_energy_consumptions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"site_id", "year", "source"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteEnergyConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Site site;

    @Column(nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "energy_source")
    private EnergySource source;

    @Column(name = "consumption_mwh", nullable = false)
    private BigDecimal consumptionMwh;
}
