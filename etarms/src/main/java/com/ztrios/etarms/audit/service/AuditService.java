package com.ztrios.etarms.audit.service;

import com.ztrios.etarms.audit.config.AuditorAwareImpl;
import com.ztrios.etarms.audit.entity.AuditLog;
import com.ztrios.etarms.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditorAwareImpl auditorAwareImpl;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(
            String action,
            String entityType,
            String entityId,
            String description
    ) {
        AuditLog log = new AuditLog();

//        log.setActor(auditorAwareImpl.getCurrentAuditor().toString());
        log.setActor(auditorAwareImpl.getCurrentAuditor().orElse("SYSTEM"));
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setTimestamp(Instant.now());
        log.setDescription(description);

        auditLogRepository.save(log);
    }
}
