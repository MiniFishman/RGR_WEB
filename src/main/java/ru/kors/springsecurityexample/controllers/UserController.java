package ru.kors.springsecurityexample.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kors.springsecurityexample.models.Users;
import ru.kors.springsecurityexample.services.UserService;

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
}
