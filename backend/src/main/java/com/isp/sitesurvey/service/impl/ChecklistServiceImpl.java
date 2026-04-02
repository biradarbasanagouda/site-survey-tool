package com.isp.sitesurvey.service.impl;

import com.isp.sitesurvey.dto.request.ChecklistResponseRequest;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.service.ChecklistService;
import com.isp.sitesurvey.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistTemplateRepository templateRepository;
    private final ChecklistResponseRepository responseRepository;
    private final SecurityUtils securityUtils;

    @Override
    public List<ChecklistTemplate> getTemplates(Long orgId, String scope) {
        return scope != null
            ? templateRepository.findByOrganizationIdAndScopeAndIsActiveTrue(orgId, scope)
            : templateRepository.findByOrganizationIdAndIsActiveTrue(orgId);
    }

    @Override
    public ChecklistTemplate getTemplate(Long id) {
        return templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplate", id));
    }

    @Override
    @Transactional
    public ChecklistResponse submitResponse(ChecklistResponseRequest req) {
        ChecklistTemplate template = getTemplate(req.templateId());
        User currentUser = securityUtils.getCurrentUser();
        ChecklistResponse response = ChecklistResponse.builder()
            .template(template)
            .targetType(req.targetType())
            .targetId(req.targetId())
            .answersJson(req.answersJson())
            .photosManifest(req.photosManifest())
            .submittedBy(currentUser)
            .submittedAt(req.submit() ? LocalDateTime.now() : null)
            .build();
        return responseRepository.save(response);
    }

    @Override
    public List<ChecklistResponse> getResponses(String targetType, Long targetId) {
        return responseRepository.findByTargetTypeAndTargetId(targetType, targetId);
    }
}
