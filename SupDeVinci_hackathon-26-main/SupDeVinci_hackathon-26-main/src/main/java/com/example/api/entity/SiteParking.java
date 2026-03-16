package com.example.api.entity;

import com.example.api.enums.ParkingType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "site_parking", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"site_id", "type"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteParking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Site site;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "parking_type")
    private ParkingType type;

    @Column(nullable = false)
    private Integer count;
}
