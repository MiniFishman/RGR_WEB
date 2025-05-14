package ru.kors.springsecurityexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kors.springsecurityexample.models.Cart;
import ru.kors.springsecurityexample.models.Users;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    boolean existsByUser(Users user);

    Optional<Cart> findByUser(Users user);
}
