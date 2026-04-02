package com.isp.sitesurvey.service;

import com.isp.sitesurvey.dto.request.RfScanRequest;
import com.isp.sitesurvey.entity.RfScan;
import java.util.List;

public interface RfScanService {
    RfScan create(RfScanRequest req);
    RfScan getById(Long id);
    List<RfScan> listByProperty(Long propertyId, Long floorId);
}
