package com.isp.sitesurvey.repository;
import com.isp.sitesurvey.entity.ChecklistResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ChecklistResponseRepository extends JpaRepository<ChecklistResponse, Long> {
    List<ChecklistResponse> findByTargetTypeAndTargetId(String targetType, Long targetId);
    List<ChecklistResponse> findByTemplateId(Long templateId);
}