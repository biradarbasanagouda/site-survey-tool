package com.isp.sitesurvey.service;
import com.isp.sitesurvey.dto.request.BuildingRequest;
import com.isp.sitesurvey.dto.response.BuildingResponse;
import java.util.List;
public interface BuildingService {
    BuildingResponse create(BuildingRequest req);
    BuildingResponse getById(Long id);
    List<BuildingResponse> listByProperty(Long propertyId);
    BuildingResponse update(Long id, BuildingRequest req);
    void delete(Long id);
}