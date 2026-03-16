package com.example.api.dto.response;

import java.math.BigDecimal;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarbonReportResponse {
    private UUID id;
    private UUID siteId;
    private LocalDateTime calculatedAt;
    private Integer referenceYear;
    private BigDecimal constructionCo2Kg;
    private BigDecimal exploitationCo2Kg;
    private BigDecimal totalCo2Kg;
    private BigDecimal co2PerM2;
    private BigDecimal co2PerEmployee;
    private String notes;
}
