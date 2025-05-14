package ru.kors.springsecurityexample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kors.springsecurityexample.models.Books;
import ru.kors.springsecurityexample.models.Cart;
import ru.kors.springsecurityexample.models.CartItems;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Integer> {
    Optional<CartItems> findByCartAndBook(Cart cart, Books book);

    List<CartItems> findAllByCart(Cart cart);

    void deleteAllByCart(Cart cart);

    void deleteByCartAndBook(Cart cart, Books book);
}
