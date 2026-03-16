package com.example.api.dto.request;

import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaterialTypeRequest {

    private String code;

    private String name;

    private String unit;

    private BigDecimal co2FactorKgPerUnit;

    private String source;

    private String description;
}
