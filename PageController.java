package io.luv.pulsehub.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import io.luv.pulsehub.repository.AuditEventRepository;
import io.luv.pulsehub.repository.RoleRepository;
import io.luv.pulsehub.repository.UserAccountRepository;

@Controller
public class PageController {
    private final UserAccountRepository users;
    private final RoleRepository roles;
    private final AuditEventRepository events;

    public PageController(UserAccountRepository users, RoleRepository roles, AuditEventRepository events) {
        this.users = users;
        this.roles = roles;
        this.events = events;
    }

    @GetMapping("/login")
    String login() { return "login"; }

    @GetMapping("/")
    String dashboard(Model model) {
        model.addAttribute("userCount", users.count());
        model.addAttribute("roleCount", roles.count());
        model.addAttribute("eventCount", events.count());
        return "dashboard";
    }

    @GetMapping("/access-denied")
    String denied() { return "access-denied"; }
}
