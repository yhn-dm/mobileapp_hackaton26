package com.example.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaterialTypeRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String unit;

    @NotNull
    private BigDecimal co2FactorKgPerUnit;

    private String source;

    private String description;
}
