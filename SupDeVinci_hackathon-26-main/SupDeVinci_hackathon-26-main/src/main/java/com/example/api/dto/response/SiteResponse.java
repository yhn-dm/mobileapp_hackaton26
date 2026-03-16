package com.example.api.dto.response;

import java.math.BigDecimal;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private String address;
    private String city;
    private BigDecimal totalSurfaceM2;
    private Integer employeeCount;
    private Integer workstationCount;
    private Integer constructionYear;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
