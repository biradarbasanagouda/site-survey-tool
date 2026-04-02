package com.isp.sitesurvey.entity;

import com.isp.sitesurvey.enums.CableMedium;
import com.isp.sitesurvey.enums.GeometryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cable_paths")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CablePath extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_space_id")
    private Space fromSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_space_id")
    private Space toSpace;

    @Enumerated(EnumType.STRING)
    @Column(name = "medium", nullable = false)
    private CableMedium medium;

    @Column(name = "length_m", precision = 10, scale = 2)
    private BigDecimal lengthM;

    @Column(name = "slack_loops")
    @Builder.Default
    private Integer slackLoops = 0;

    @Column(name = "splice_points")
    @Builder.Default
    private Integer splicePoints = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "geometry_type")
    @Builder.Default
    private GeometryType geometryType = GeometryType.LINESTRING;

    @Column(name = "geometry_wkt", columnDefinition = "TEXT")
    private String geometryWkt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
