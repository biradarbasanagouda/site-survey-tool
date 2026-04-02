package com.isp.sitesurvey.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "checklist_templates")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistTemplate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "scope", nullable = false, length = 50)
    private String scope;

    @Column(name = "version", nullable = false)
    @Builder.Default
    private Integer version = 1;

    @Column(name = "schema_json", nullable = false, columnDefinition = "JSON")
    private String schemaJson;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
