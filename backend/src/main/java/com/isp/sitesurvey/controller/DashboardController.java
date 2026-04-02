package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.response.DashboardResponse;
import com.isp.sitesurvey.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@RequestParam Long orgId) {
        return ResponseEntity.ok(dashboardService.getDashboard(orgId));
    }
}