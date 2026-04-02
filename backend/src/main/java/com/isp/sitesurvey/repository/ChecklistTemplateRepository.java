package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.ChecklistTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, Long> {
    List<ChecklistTemplate> findByOrganizationIdAndIsActiveTrue(Long orgId);
    List<ChecklistTemplate> findByOrganizationIdAndScopeAndIsActiveTrue(Long orgId, String scope);
}