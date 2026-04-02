package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.enums.ReportStatus;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.ReportService;
import com.isp.sitesurvey.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PropertyRepository propertyRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public Report generate(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new ResourceNotFoundException("Property", propertyId));
        User user = securityUtils.getCurrentUser();
        Report report = Report.builder()
            .property(property)
            .requestedBy(user)
            .status(ReportStatus.PENDING)
            .build();
        Report saved = reportRepository.save(report);
        generateAsync(saved.getId());
        return saved;
    }

    @Async
    protected void generateAsync(Long reportId) {
        try {
            Thread.sleep(2000); // simulate PDF generation
            Report report = reportRepository.findById(reportId).orElseThrow();
            report.setStatus(ReportStatus.GENERATING);
            reportRepository.save(report);
            Thread.sleep(3000);
            report.setStatus(ReportStatus.DONE);
            reportRepository.save(report);
            log.info("Report {} generated successfully", reportId);
        } catch (Exception e) {
            reportRepository.findById(reportId).ifPresent(r -> {
                r.setStatus(ReportStatus.FAILED);
                reportRepository.save(r);
            });
            log.error("Report {} generation failed", reportId, e);
        }
    }

    @Override
    public Report getById(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report", id));
    }

    @Override
    public List<Report> listByProperty(Long propertyId) {
        return reportRepository.findByPropertyId(propertyId);
    }
}
