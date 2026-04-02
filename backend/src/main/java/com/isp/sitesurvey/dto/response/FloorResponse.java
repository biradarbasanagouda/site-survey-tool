package com.isp.sitesurvey.dto.response;
import com.isp.sitesurvey.entity.Floor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record FloorResponse(
    Long id, Long buildingId, String levelLabel,
    BigDecimal elevationM, Long planFileId, String scaleRatio,
    LocalDateTime createdAt, LocalDateTime updatedAt
) {
    public static FloorResponse from(Floor f) {
        return new FloorResponse(
            f.getId(), f.getBuilding().getId(), f.getLevelLabel(),
            f.getElevationM(), f.getPlanFile() != null ? f.getPlanFile().getId() : null,
            f.getScaleRatio(), f.getCreatedAt(), f.getUpdatedAt()
        );
    }
}