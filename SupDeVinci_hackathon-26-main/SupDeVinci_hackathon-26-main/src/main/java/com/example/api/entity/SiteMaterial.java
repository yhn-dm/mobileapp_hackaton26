package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "site_materials", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"site_id", "material_type_id"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_type_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MaterialType materialType;

    @Column(nullable = false)
    private BigDecimal quantity;
}
