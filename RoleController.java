package io.luv.pulsehub.web;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.luv.pulsehub.domain.Permission;
import io.luv.pulsehub.service.RoleManagementService;

@Controller
@PreAuthorize("hasAuthority('ROLES_VIEW')")
public class RoleController {
    private final RoleManagementService roles;

    public RoleController(RoleManagementService roles) { this.roles = roles; }

    @GetMapping("/roles")
    String list(Model model) {
        model.addAttribute("roles", roles.findAll());
        return "roles/list";
    }

    @GetMapping("/roles/new")
    @PreAuthorize("hasAuthority('ROLES_MANAGE')")
    String createForm(Model model) {
        model.addAttribute("roleForm", new RoleForm());
        model.addAttribute("allPermissions", Permission.values());
        return "roles/form";
    }

    @GetMapping("/roles/{id}/edit")
    @PreAuthorize("hasAuthority('ROLES_MANAGE')")
    String editForm(@PathVariable long id, Model model) {
        var role = roles.findById(id);
        var form = new RoleForm();
        form.setId(role.getId());
        form.setName(role.getName());
        form.setDescription(role.getDescription());
        form.setPermissions(role.getPermissions());
        model.addAttribute("roleForm", form);
        model.addAttribute("allPermissions", Permission.values());
        return "roles/form";
    }

    @PostMapping("/roles/save")
    @PreAuthorize("hasAuthority('ROLES_MANAGE')")
    String save(@Valid @ModelAttribute RoleForm form, BindingResult result, Model model,
                RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("allPermissions", Permission.values());
            return "roles/form";
        }
        try {
            if (form.getId() == null) roles.create(form.getName(), form.getDescription(), form.getPermissions());
            else roles.update(form.getId(), form.getName(), form.getDescription(), form.getPermissions());
            redirect.addFlashAttribute("message", "Роль сохранена");
            return "redirect:/roles";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("allPermissions", Permission.values());
            return "roles/form";
        }
    }

    @PostMapping("/roles/{id}/delete")
    @PreAuthorize("hasAuthority('ROLES_MANAGE')")
    String delete(@PathVariable long id, RedirectAttributes redirect) {
        try {
            roles.delete(id);
            redirect.addFlashAttribute("message", "Роль удалена");
        } catch (IllegalArgumentException exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/roles";
    }
}
