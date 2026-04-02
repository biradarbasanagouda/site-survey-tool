package com.isp.sitesurvey.entity;

import com.isp.sitesurvey.enums.RfTool;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rf_scans")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RfScan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tool", nullable = false)
    @Builder.Default
    private RfTool tool = RfTool.MANUAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_file_id")
    private FileEntity rawFile;

    @Column(name = "parsed_json", columnDefinition = "JSON")
    private String parsedJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heatmap_file_id")
    private FileEntity heatmapFile;
}
