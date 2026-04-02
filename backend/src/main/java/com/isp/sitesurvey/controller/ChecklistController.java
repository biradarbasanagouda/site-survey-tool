package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.dto.request.ChecklistResponseRequest;
import com.isp.sitesurvey.entity.*;
import com.isp.sitesurvey.exception.ResourceNotFoundException;
import com.isp.sitesurvey.repository.*;
import com.isp.sitesurvey.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/checklists")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistTemplateRepository templateRepository;
    private final ChecklistResponseRepository responseRepository;
    private final SecurityUtils securityUtils;

    @GetMapping("/templates")
    public ResponseEntity<List<ChecklistTemplate>> getTemplates(@RequestParam Long orgId,
                                                                 @RequestParam(required=false) String scope) {
        List<ChecklistTemplate> templates = scope != null
            ? templateRepository.findByOrganizationIdAndScopeAndIsActiveTrue(orgId, scope)
            : templateRepository.findByOrganizationIdAndIsActiveTrue(orgId);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<ChecklistTemplate> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplate", id)));
    }

    @PostMapping("/responses")
    public ResponseEntity<ChecklistResponse> submitResponse(@Valid @RequestBody ChecklistResponseRequest req) {
        ChecklistTemplate template = templateRepository.findById(req.templateId())
            .orElseThrow(() -> new ResourceNotFoundException("ChecklistTemplate", req.templateId()));
        User currentUser = securityUtils.getCurrentUser();
        ChecklistResponse response = ChecklistResponse.builder()
            .template(template).targetType(req.targetType()).targetId(req.targetId())
            .answersJson(req.answersJson()).photosManifest(req.photosManifest())
            .submittedBy(currentUser)
            .submittedAt(req.submit() ? LocalDateTime.now() : null)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseRepository.save(response));
    }

    @GetMapping("/responses")
    public ResponseEntity<List<ChecklistResponse>> getResponses(@RequestParam String targetType,
                                                                  @RequestParam Long targetId) {
        return ResponseEntity.ok(responseRepository.findByTargetTypeAndTargetId(targetType, targetId));
    }
}