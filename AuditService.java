package io.luv.pulsehub.service;

import io.luv.pulsehub.domain.AuditEvent;

import java.util.List;

// Интерфейс позволяет заменить реализацию журнала.
public interface AuditService {
    void record(String action, String details);
    List<AuditEvent> latest();
}
