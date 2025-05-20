package ru.kors.springsecurityexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kors.springsecurityexample.models.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByLogin(String login);

    Optional<Users> findById(Long userId);
}
