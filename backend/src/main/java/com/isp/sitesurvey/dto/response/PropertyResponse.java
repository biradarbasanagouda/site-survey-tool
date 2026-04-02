package com.isp.sitesurvey.dto.response;
import com.isp.sitesurvey.entity.Property;
import com.isp.sitesurvey.enums.GeometryType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record PropertyResponse(
    Long id, Long organizationId, String name,
    String addressLine1, String addressLine2, String city, String state, String postalCode, String country,
    GeometryType boundaryType, String boundaryWkt,
    BigDecimal centroidLat, BigDecimal centroidLon,
    LocalDateTime createdAt, LocalDateTime updatedAt
) {
    public static PropertyResponse from(Property p) {
        return new PropertyResponse(
            p.getId(), p.getOrganization().getId(), p.getName(),
            p.getAddressLine1(), p.getAddressLine2(), p.getCity(), p.getState(), p.getPostalCode(), p.getCountry(),
            p.getBoundaryType(), p.getBoundaryWkt(),
            p.getCentroidLat(), p.getCentroidLon(),
            p.getCreatedAt(), p.getUpdatedAt()
        );
    }
}