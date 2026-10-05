package io.luv.pulsehub.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.luv.pulsehub.service.AuditService;

@Controller
public class AuditController {
    private final AuditService audit;

    public AuditController(AuditService audit) { this.audit = audit; }

    @GetMapping("/audit")
    @PreAuthorize("hasAuthority('AUDIT_VIEW')")
    String audit(Model model) {
        model.addAttribute("events", audit.latest());
        return "audit";
    }
}
