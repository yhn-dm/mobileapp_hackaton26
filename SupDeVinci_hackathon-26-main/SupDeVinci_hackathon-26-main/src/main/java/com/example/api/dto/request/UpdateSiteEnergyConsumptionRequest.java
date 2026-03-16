package com.example.api.dto.request;

import com.example.api.enums.EnergySource;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSiteEnergyConsumptionRequest {

    private Integer year;

    private EnergySource source;

    private BigDecimal consumptionMwh;
}
