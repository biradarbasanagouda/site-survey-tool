package com.isp.sitesurvey.dto.request;
import com.isp.sitesurvey.enums.RfTool;
import jakarta.validation.constraints.NotNull;
public record RfScanRequest(
    @NotNull Long propertyId,
    Long floorId,
    RfTool tool,
    String parsedJson
) {}