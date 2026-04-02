package com.isp.sitesurvey.service;
import com.isp.sitesurvey.dto.request.PropertyRequest;
import com.isp.sitesurvey.dto.response.*;
import org.springframework.data.domain.Pageable;
public interface PropertyService {
    PropertyResponse create(PropertyRequest req);
    PropertyResponse getById(Long id);
    PageResponse<PropertyResponse> listByOrg(Long orgId, Pageable pageable);
    PropertyResponse update(Long id, PropertyRequest req);
    void delete(Long id);
}