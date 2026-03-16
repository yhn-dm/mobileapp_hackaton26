package com.example.api.dto.response;

import com.example.api.enums.ReportCategory;
import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarbonReportDetailResponse {
    private UUID id;
    private UUID reportId;
    private ReportCategory category;
    private BigDecimal co2Kg;
    private BigDecimal percentage;
}
