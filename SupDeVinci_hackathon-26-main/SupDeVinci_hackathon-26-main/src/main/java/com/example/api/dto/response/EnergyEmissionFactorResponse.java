package com.example.api.dto.response;

import com.example.api.enums.EnergySource;
import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyEmissionFactorResponse {
    private UUID id;
    private EnergySource source;
    private String countryCode;
    private Integer year;
    private BigDecimal factorKgCo2PerKwh;
    private String sourceName;
}
