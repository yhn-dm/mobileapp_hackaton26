package com.example.api.dto.request;

import com.example.api.enums.ReportCategory;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCarbonReportDetailRequest {

    private ReportCategory category;

    private BigDecimal co2Kg;

    private BigDecimal percentage;
}
