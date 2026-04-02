package com.isp.sitesurvey.entity;

import com.isp.sitesurvey.enums.EquipmentType;
import com.isp.sitesurvey.enums.GeometryType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "equipment")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EquipmentType type;

    @Column(name = "model", length = 280)
    private String model;

    @Column(name = "vendor", length = 200)
    private String vendor;

    @Column(name = "power_watts", precision = 10, scale = 2)
    private BigDecimal powerWatts;

    @Column(name = "heat_load_btuh", precision = 10, scale = 2)
    private BigDecimal heatLoadBtuh;

    @Column(name = "mounting", length = 100)
    private String mounting;

    @Enumerated(EnumType.STRING)
    @Column(name = "geometry_type")
    @Builder.Default
    private GeometryType geometryType = GeometryType.POINT;

    @Column(name = "geometry_wkt", columnDefinition = "TEXT")
    private String geometryWkt;

    @Column(name = "serial_number", length = 120)
    private String serialNumber;
}
