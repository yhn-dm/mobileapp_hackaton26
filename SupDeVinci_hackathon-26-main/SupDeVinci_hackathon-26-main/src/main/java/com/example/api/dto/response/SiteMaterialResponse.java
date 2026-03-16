package com.example.api.dto.response;

import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteMaterialResponse {
    private UUID id;
    private UUID siteId;
    private UUID materialTypeId;
    private String materialTypeCode;
    private String materialTypeName;
    private BigDecimal quantity;
}
