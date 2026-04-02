package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.Report;
import com.isp.sitesurvey.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPropertyId(Long propertyId);
    List<Report> findByStatus(ReportStatus status);
}