package com.example.api.dto.request;

import com.example.api.enums.ParkingType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSiteParkingRequest {

    @NotNull
    private ParkingType type;

    @NotNull
    private Integer count;
}
