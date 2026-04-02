package com.isp.sitesurvey.dto.request;
import com.isp.sitesurvey.enums.GeometryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
public record PropertyRequest(
    @NotNull Long organizationId,
    @NotBlank String name,
    String addressLine1,
    String addressLine2,
    String city,
    String state,
    String postalCode,
    String country,
    GeometryType boundaryType,
    String boundaryWkt,
    BigDecimal centroidLat,
    BigDecimal centroidLon
) {}