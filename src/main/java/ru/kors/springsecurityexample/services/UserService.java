package ru.kors.springsecurityexample.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kors.springsecurityexample.dto.LoginRequest;
import ru.kors.springsecurityexample.models.Users;
import ru.kors.springsecurityexample.repository.UserRepository;

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
}
