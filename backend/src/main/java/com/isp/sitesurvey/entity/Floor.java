package com.isp.sitesurvey.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "floors")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Floor extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "level_label", nullable = false, length = 50)
    private String levelLabel;

    @Column(name = "elevation_m", precision = 8, scale = 2)
    @Builder.Default
    private BigDecimal elevationM = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_file_id")
    private FileEntity planFile;

    @Column(name = "scale_ratio", length = 50)
    private String scaleRatio;

    @Column(name = "anchor_points", columnDefinition = "JSON")
    private String anchorPoints;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Space> spaces = new ArrayList<>();
}
