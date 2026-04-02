package com.isp.sitesurvey.service;

import com.isp.sitesurvey.dto.request.ChecklistResponseRequest;
import com.isp.sitesurvey.entity.*;
import java.util.List;

public interface ChecklistService {
    List<ChecklistTemplate> getTemplates(Long orgId, String scope);
    ChecklistTemplate getTemplate(Long id);
    ChecklistResponse submitResponse(ChecklistResponseRequest req);
    List<ChecklistResponse> getResponses(String targetType, Long targetId);
}
