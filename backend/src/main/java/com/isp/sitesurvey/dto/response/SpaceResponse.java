package com.isp.sitesurvey.dto.response;
import com.isp.sitesurvey.entity.Space;
import com.isp.sitesurvey.enums.GeometryType;
import com.isp.sitesurvey.enums.SpaceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record SpaceResponse(
    Long id, Long floorId, String name, SpaceType type,
    GeometryType geometryType, String geometryWkt,
    BigDecimal areaSqM, BigDecimal elevationM, String notes,
    LocalDateTime createdAt, LocalDateTime updatedAt
) {
    public static SpaceResponse from(Space s) {
        return new SpaceResponse(
            s.getId(), s.getFloor().getId(), s.getName(), s.getType(),
            s.getGeometryType(), s.getGeometryWkt(),
            s.getAreaSqM(), s.getElevationM(), s.getNotes(),
            s.getCreatedAt(), s.getUpdatedAt()
        );
    }
}