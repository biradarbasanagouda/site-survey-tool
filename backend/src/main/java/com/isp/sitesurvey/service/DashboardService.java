package com.isp.sitesurvey.service;
import com.isp.sitesurvey.dto.response.DashboardResponse;
public interface DashboardService {
    DashboardResponse getDashboard(Long orgId);
}