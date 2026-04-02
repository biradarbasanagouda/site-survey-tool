package com.isp.sitesurvey.service;

import com.isp.sitesurvey.dto.request.SpaceRequest;
import com.isp.sitesurvey.dto.response.SpaceResponse;
import java.util.List;

public interface SpaceService {
    SpaceResponse create(SpaceRequest req);
    SpaceResponse getById(Long id);
    List<SpaceResponse> listByFloor(Long floorId);
    SpaceResponse update(Long id, SpaceRequest req);
    void delete(Long id);
}
