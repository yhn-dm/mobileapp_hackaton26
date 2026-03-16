package com.example.api.dto.request;

import com.example.api.enums.ParkingType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSiteParkingRequest {

    private ParkingType type;

    private Integer count;
}
