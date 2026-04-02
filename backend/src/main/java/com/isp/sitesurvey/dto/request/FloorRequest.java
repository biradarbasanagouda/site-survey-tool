package com.isp.sitesurvey.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record FloorRequest(
    @NotNull Long buildingId,
    @NotBlank String levelLabel,
    BigDecimal elevationM,
    String scaleRatio,
    String anchorPoints
) {}