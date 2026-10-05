package io.luv.pulsehub.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.luv.pulsehub.domain.AuditEvent;
import io.luv.pulsehub.repository.AuditEventRepository;

import java.util.List;

@Service
public class DatabaseAuditService implements AuditService {
    private final AuditEventRepository repository;

    public DatabaseAuditService(AuditEventRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void record(String action, String details) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var actor = authentication == null ? "system" : authentication.getName();
        repository.save(new AuditEvent(actor, action, details));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditEvent> latest() {
        return repository.findTop100ByOrderByCreatedAtDesc();
    }
}
