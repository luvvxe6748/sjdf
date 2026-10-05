package io.luv.pulsehub.web;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.luv.pulsehub.service.RoleManagementService;
import io.luv.pulsehub.service.UserManagementService;

@Controller
@PreAuthorize("hasAuthority('USERS_VIEW')")
public class UserController {
    private final UserManagementService users;
    private final RoleManagementService roles;

    public UserController(UserManagementService users, RoleManagementService roles) {
        this.users = users;
        this.roles = roles;
    }

    @GetMapping("/users")
    String list(Model model) {
        model.addAttribute("users", users.findAll());
        return "users/list";
    }

    @GetMapping("/users/new")
    @PreAuthorize("hasAuthority('USERS_MANAGE')")
    String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("roles", roles.findAll());
        return "users/form";
    }

    @GetMapping("/users/{id}/edit")
    @PreAuthorize("hasAuthority('USERS_MANAGE')")
    String editForm(@PathVariable long id, Model model) {
        var user = users.findById(id);
        var form = new UserForm();
        form.setId(user.getId());
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setRoleId(user.getRole().getId());
        form.setEnabled(user.isEnabled());
        model.addAttribute("userForm", form);
        model.addAttribute("roles", roles.findAll());
        return "users/form";
    }

    @PostMapping("/users/save")
    @PreAuthorize("hasAuthority('USERS_MANAGE')")
    String save(@Valid @ModelAttribute UserForm form, BindingResult result, Model model,
                RedirectAttributes redirect) {
        if (form.getId() == null && (form.getPassword() == null || form.getPassword().length() < 6)) {
            result.rejectValue("password", "password.short", "Минимум 6 символов");
        }
        if (result.hasErrors()) {
            model.addAttribute("roles", roles.findAll());
            return "users/form";
        }
        try {
            if (form.getId() == null) {
                users.create(form.getUsername(), form.getFullName(), form.getPassword(), form.getRoleId());
            } else {
                users.update(form.getId(), form.getFullName(), form.getPassword(), form.getRoleId(), form.isEnabled());
            }
            redirect.addFlashAttribute("message", "Данные сохранены");
            return "redirect:/users";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("error", exception.getMessage());
            model.addAttribute("roles", roles.findAll());
            return "users/form";
        }
    }

    @PostMapping("/users/{id}/delete")
    @PreAuthorize("hasAuthority('USERS_MANAGE')")
    String delete(@PathVariable long id, Authentication authentication, RedirectAttributes redirect) {
        try {
            users.delete(id, authentication.getName());
            redirect.addFlashAttribute("message", "Пользователь удалён");
        } catch (IllegalArgumentException exception) {
            redirect.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:/users";
    }
}
