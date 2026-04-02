package com.isp.sitesurvey.dto.request;
import com.isp.sitesurvey.enums.GeometryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record BuildingRequest(
    @NotNull Long propertyId,
    @NotBlank String name,
    String code,
    GeometryType footprintType,
    String footprintWkt,
    Integer floorsCount
) {}