package com.example.api.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarbonReportRequest {

    @NotNull
    private UUID siteId;

    private Integer referenceYear;

    private BigDecimal constructionCo2Kg;

    private BigDecimal exploitationCo2Kg;

    private BigDecimal co2PerM2;

    private BigDecimal co2PerEmployee;

    private String notes;
}
