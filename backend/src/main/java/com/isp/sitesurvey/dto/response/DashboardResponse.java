package com.isp.sitesurvey.dto.response;
public record DashboardResponse(
    long totalProperties,
    long totalBuildings,
    long totalFloors,
    long totalSpaces,
    long totalEquipment,
    long totalChecklistResponses,
    long pendingReports
) {}