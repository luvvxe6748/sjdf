package io.luv.pulsehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.luv.pulsehub.domain.AuditEvent;

import java.util.List;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
    List<AuditEvent> findTop100ByOrderByCreatedAtDesc();
}
