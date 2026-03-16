package com.example.api.dto.request;

import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCarbonReportRequest {

    private Integer referenceYear;

    private BigDecimal constructionCo2Kg;

    private BigDecimal exploitationCo2Kg;

    private BigDecimal co2PerM2;

    private BigDecimal co2PerEmployee;

    private String notes;
}
