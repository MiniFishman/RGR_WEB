package ru.kors.springsecurityexample.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kors.springsecurityexample.dto.UserDTO;
import ru.kors.springsecurityexample.models.Users;
import ru.kors.springsecurityexample.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public String register(@RequestBody Users user) {
        service.register(user);
        return "Сохранено";
    }

    @GetMapping("/current")
    public String currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Текущий юзер: " + authentication.getName();
    }

    @PostMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "Выход осуществлён";
    }

    @GetMapping("/role")
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return "ROLE_ANONYMOUS";
        }

        return authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_ANONYMOUS");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return service.findAllUsers()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getLogin(), user.getRole()))
                .collect(Collectors.toList());
    }

    @PutMapping("/update-role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String role = body.get("role");
        if (role == null) {
            return ResponseEntity.badRequest().body("Role is missing");
        }
        boolean updated = service.updateUserRole(id, role);
        if (updated) {
            return ResponseEntity.ok("Role updated");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
