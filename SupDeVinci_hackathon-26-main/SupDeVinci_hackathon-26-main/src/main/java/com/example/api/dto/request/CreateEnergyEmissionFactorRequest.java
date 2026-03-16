package com.example.api.dto.request;

import com.example.api.enums.EnergySource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEnergyEmissionFactorRequest {

    @NotNull
    private EnergySource source;

    @NotBlank
    private String countryCode;

    @NotNull
    private Integer year;

    @NotNull
    private BigDecimal factorKgCo2PerKwh;

    private String sourceName;
}
