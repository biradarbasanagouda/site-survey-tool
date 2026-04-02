package com.isp.sitesurvey.dto.response;
import com.isp.sitesurvey.entity.Building;
import com.isp.sitesurvey.enums.GeometryType;
import java.time.LocalDateTime;
public record BuildingResponse(
    Long id, Long propertyId, String name, String code,
    GeometryType footprintType, String footprintWkt, Integer floorsCount,
    LocalDateTime createdAt, LocalDateTime updatedAt
) {
    public static BuildingResponse from(Building b) {
        return new BuildingResponse(
            b.getId(), b.getProperty().getId(), b.getName(), b.getCode(),
            b.getFootprintType(), b.getFootprintWkt(), b.getFloorsCount(),
            b.getCreatedAt(), b.getUpdatedAt()
        );
    }
}