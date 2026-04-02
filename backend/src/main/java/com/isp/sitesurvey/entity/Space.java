package com.isp.sitesurvey.entity;

import com.isp.sitesurvey.enums.GeometryType;
import com.isp.sitesurvey.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spaces")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Space extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(name = "name", nullable = false, length = 280)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Builder.Default
    private SpaceType type = SpaceType.OTHER;

    @Enumerated(EnumType.STRING)
    @Column(name = "geometry_type")
    @Builder.Default
    private GeometryType geometryType = GeometryType.POLYGON;

    @Column(name = "geometry_wkt", columnDefinition = "TEXT")
    private String geometryWkt;

    @Column(name = "area_sq_m", precision = 10, scale = 2)
    private BigDecimal areaSqM;

    @Column(name = "elevation_m", precision = 8, scale = 2)
    private BigDecimal elevationM;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Equipment> equipment = new ArrayList<>();
}
