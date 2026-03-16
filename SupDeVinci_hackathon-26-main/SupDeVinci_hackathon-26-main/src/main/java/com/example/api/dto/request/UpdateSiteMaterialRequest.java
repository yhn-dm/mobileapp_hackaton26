package com.example.api.dto.request;

import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSiteMaterialRequest {

    private UUID materialTypeId;

    private BigDecimal quantity;
}
