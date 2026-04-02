package com.isp.sitesurvey.service;

import com.isp.sitesurvey.entity.Report;
import java.util.List;

public interface ReportService {
    Report generate(Long propertyId);
    Report getById(Long id);
    List<Report> listByProperty(Long propertyId);
}
