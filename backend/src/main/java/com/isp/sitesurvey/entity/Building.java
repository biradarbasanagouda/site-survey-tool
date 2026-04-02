package com.isp.sitesurvey.entity;

import com.isp.sitesurvey.enums.GeometryType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "buildings")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Building extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "name", nullable = false, length = 280)
    private String name;

    @Column(name = "code", length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "footprint_type")
    private GeometryType footprintType;

    @Column(name = "footprint_wkt", columnDefinition = "TEXT")
    private String footprintWkt;

    @Column(name = "floors_count")
    @Builder.Default
    private Integer floorsCount = 1;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Floor> floors = new ArrayList<>();
}
