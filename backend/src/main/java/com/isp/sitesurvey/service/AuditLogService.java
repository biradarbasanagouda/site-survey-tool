package com.isp.sitesurvey.service;

import com.isp.sitesurvey.entity.AuditLog;
import com.isp.sitesurvey.entity.User;
import com.isp.sitesurvey.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Async
    public void log(User actor, String action, String entityType, Long entityId, String changeSet) {
        AuditLog entry = AuditLog.builder()
            .actor(actor)
            .action(action)
            .entityType(entityType)
            .entityId(entityId)
            .changeSet(changeSet)
            .build();
        auditLogRepository.save(entry);
    }

    public Page<AuditLog> findByEntity(String entityType, Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
    }

    public Page<AuditLog> findByActor(Long actorId, Pageable pageable) {
        return auditLogRepository.findByActorId(actorId, pageable);
    }
}
