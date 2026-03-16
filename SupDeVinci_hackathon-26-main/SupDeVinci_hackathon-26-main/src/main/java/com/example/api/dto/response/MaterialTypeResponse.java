package com.example.api.dto.response;

import java.math.BigDecimal;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialTypeResponse {
    private UUID id;
    private String code;
    private String name;
    private String unit;
    private BigDecimal co2FactorKgPerUnit;
    private String source;
    private String description;
    private LocalDateTime updatedAt;
}
