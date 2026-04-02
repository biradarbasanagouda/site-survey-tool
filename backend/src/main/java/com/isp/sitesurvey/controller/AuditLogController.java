package com.isp.sitesurvey.controller;

import com.isp.sitesurvey.entity.AuditLog;
import com.isp.sitesurvey.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/entity")
    public ResponseEntity<Page<AuditLog>> byEntity(
            @RequestParam String entityType,
            @RequestParam Long entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(auditLogService.findByEntity(
            entityType, entityId,
            PageRequest.of(page, size, Sort.by("createdAt").descending())
        ));
    }

    @GetMapping("/actor/{actorId}")
    public ResponseEntity<Page<AuditLog>> byActor(
            @PathVariable Long actorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(auditLogService.findByActor(
            actorId,
            PageRequest.of(page, size, Sort.by("createdAt").descending())
        ));
    }
}
