package com.isp.sitesurvey.dto.request;
import com.isp.sitesurvey.enums.GeometryType;
import com.isp.sitesurvey.enums.SpaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record SpaceRequest(
    @NotNull Long floorId,
    @NotBlank String name,
    SpaceType type,
    GeometryType geometryType,
    String geometryWkt,
    BigDecimal areaSqM,
    BigDecimal elevationM,
    String notes
) {}