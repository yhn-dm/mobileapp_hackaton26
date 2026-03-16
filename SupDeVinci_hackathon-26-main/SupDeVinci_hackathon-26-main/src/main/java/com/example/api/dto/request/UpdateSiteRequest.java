package com.example.api.dto.request;

import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSiteRequest {

    private UUID userId;

    private String name;

    private String address;

    private String city;

    private BigDecimal totalSurfaceM2;

    private Integer employeeCount;

    private Integer workstationCount;

    private Integer constructionYear;

    private String description;
}
