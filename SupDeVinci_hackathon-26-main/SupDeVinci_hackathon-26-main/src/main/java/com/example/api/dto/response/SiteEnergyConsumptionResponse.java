package com.example.api.dto.response;

import com.example.api.enums.EnergySource;
import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteEnergyConsumptionResponse {
    private UUID id;
    private UUID siteId;
    private Integer year;
    private EnergySource source;
    private BigDecimal consumptionMwh;
}
