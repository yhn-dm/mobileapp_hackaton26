package com.example.api.entity;

import com.example.api.enums.EnergySource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "energy_emission_factors", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"source", "country_code", "year"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyEmissionFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "energy_source")
    private EnergySource source;

    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private Integer year;

    @Column(name = "factor_kg_co2_per_kwh", nullable = false)
    private BigDecimal factorKgCo2PerKwh;

    @Column(name = "source_name")
    private String sourceName;
}
