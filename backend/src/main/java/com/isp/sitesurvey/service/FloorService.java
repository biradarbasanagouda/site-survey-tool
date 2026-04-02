package com.isp.sitesurvey.service;

import com.isp.sitesurvey.dto.request.FloorRequest;
import com.isp.sitesurvey.dto.response.FloorResponse;
import java.util.List;

public interface FloorService {
    FloorResponse create(FloorRequest req);
    FloorResponse getById(Long id);
    List<FloorResponse> listByBuilding(Long buildingId);
    FloorResponse update(Long id, FloorRequest req);
    void delete(Long id);
}
