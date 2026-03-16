package com.example.api.dto.request;

import com.example.api.enums.ReportCategory;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarbonReportDetailRequest {

    @NotNull
    private ReportCategory category;

    private BigDecimal co2Kg;

    private BigDecimal percentage;
}
