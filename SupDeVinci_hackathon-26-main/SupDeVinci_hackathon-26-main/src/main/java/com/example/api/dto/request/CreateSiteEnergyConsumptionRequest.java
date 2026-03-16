package com.example.api.dto.request;

import com.example.api.enums.EnergySource;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSiteEnergyConsumptionRequest {

    @NotNull
    private Integer year;

    @NotNull
    private EnergySource source;

    @NotNull
    private BigDecimal consumptionMwh;
}
