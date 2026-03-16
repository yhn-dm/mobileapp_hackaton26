package com.example.api.dto.request;

import com.example.api.enums.EnergySource;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEnergyEmissionFactorRequest {

    private EnergySource source;

    private String countryCode;

    private Integer year;

    private BigDecimal factorKgCo2PerKwh;

    private String sourceName;
}
