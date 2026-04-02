package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.enums.ReportStatus;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;
    private final PropertyRepository propertyRepository;
    private final SecurityUtils securityUtils;

    @PostMapping("/generate")
    public ResponseEntity<Report> generate(@RequestBody Map<String, Object> body) {
        Long propertyId = Long.parseLong(body.get("propertyId").toString());
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new ResourceNotFoundException("Property", propertyId));
        User user = securityUtils.getCurrentUser();
        Report report = Report.builder()
            .property(property).requestedBy(user)
            .status(ReportStatus.PENDING)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(reportRepository.save(report));
    }

    @GetMapping
    public ResponseEntity<List<Report>> listByProperty(@RequestParam Long propertyId) {
        return ResponseEntity.ok(reportRepository.findByPropertyId(propertyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report", id)));
    }
}