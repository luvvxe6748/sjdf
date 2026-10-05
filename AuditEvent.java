package io.luv.pulsehub.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_events")
public class AuditEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false, length = 40)
    private String actor;
    @Column(nullable = false, length = 80)
    private String action;
    @Column(nullable = false, length = 200)
    private String details;

    protected AuditEvent() {
    }

    public AuditEvent(String actor, String action, String details) {
        this.createdAt = LocalDateTime.now();
        this.actor = actor;
        this.action = action;
        this.details = details;
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getActor() { return actor; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
}
