package com.example.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSiteRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    private String name;

    private String address;

    private String city;

    private BigDecimal totalSurfaceM2;

    private Integer employeeCount;

    private Integer workstationCount;

    private Integer constructionYear;

    private String description;
}
