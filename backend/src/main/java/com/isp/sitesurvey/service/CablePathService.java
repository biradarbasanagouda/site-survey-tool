package com.isp.sitesurvey.service;

import com.isp.sitesurvey.entity.CablePath;
import java.util.List;

public interface CablePathService {
    CablePath create(CablePath cablePath);
    CablePath getById(Long id);
    List<CablePath> listByProperty(Long propertyId);
    void delete(Long id);
}
