package ru.kors.springsecurityexample.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kors.springsecurityexample.models.Users;
import ru.kors.springsecurityexample.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<Users> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean updateUserRole(Long userId, String newRole) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setRole(newRole);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Optional<Users> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String login = authentication.getName();
            return findByLogin(login);
        }
        return Optional.empty();
    }
}
