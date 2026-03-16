package com.example.api.dto.response;

import com.example.api.enums.ParkingType;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteParkingResponse {
    private UUID id;
    private UUID siteId;
    private ParkingType type;
    private Integer count;
}
