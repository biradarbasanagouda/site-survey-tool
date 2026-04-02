package com.isp.sitesurvey.dto.request;
import com.isp.sitesurvey.enums.EquipmentType;
import com.isp.sitesurvey.enums.GeometryType;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record EquipmentRequest(
    @NotNull Long spaceId,
    @NotNull EquipmentType type,
    String model,
    String vendor,
    BigDecimal powerWatts,
    BigDecimal heatLoadBtuh,
    String mounting,
    GeometryType geometryType,
    String geometryWkt,
    String serialNumber
) {}