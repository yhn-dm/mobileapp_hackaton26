package com.example.api.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSiteMaterialRequest {

    @NotNull
    private UUID materialTypeId;

    @NotNull
    private BigDecimal quantity;
}
