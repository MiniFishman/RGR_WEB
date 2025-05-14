package ru.kors.springsecurityexample.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kors.springsecurityexample.models.Books;
import ru.kors.springsecurityexample.models.Cart;
import ru.kors.springsecurityexample.models.CartItems;
import ru.kors.springsecurityexample.models.Users;
import ru.kors.springsecurityexample.repository.CartItemRepository;
import ru.kors.springsecurityexample.repository.CartRepository;
import ru.kors.springsecurityexample.repository.UserRepository;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    private Users getCurrentUser() {
        String username = authenticationFacade.getAuthentication().getName();
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("Текущий пользователь не найден"));
    }

    public Cart createCartForCurrentUser() {
        Users user = getCurrentUser();

        if (cartRepository.existsByUser(user)) {
            throw new RuntimeException("Пользователь уже имеет корзину");
        }

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalAmount(BigDecimal.ZERO);

        return cartRepository.save(cart);
    }

    public void removeCartForCurrentUser() {
        Users user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Корзина не найдена"));
        cartRepository.delete(cart);
    }

    /**
     * Добавляет товар в корзину (количество по умолчанию = 1)
     */
    public void addItemToCartForCurrentUser(Books book) {
        Users user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        cartItemRepository.findByCartAndBook(cart, book)
                .ifPresentOrElse(
                        existingItem -> {
                            existingItem.setQuantity(existingItem.getQuantity() + 1);
                            existingItem.setSubtotal(calculateSubtotal(existingItem));
                            cartItemRepository.save(existingItem);
                        },
                        () -> {
                            CartItems newItem = CartItems.builder()
                                    .cart(cart)
                                    .book(book)
                                    .quantity(1)
                                    .subtotal(calculateSubtotal(book, 1))
                                    .build();
                            cartItemRepository.save(newItem);
                        }
                );

        recalculateCartTotal(cart);
    }

    public void removeItemFromCartForCurrentUser(Books book, Integer quantityToRemove) {
        if (quantityToRemove <= 0) {
            throw new IllegalArgumentException("Количество для удаления должно быть положительным");
        }

        Users user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Корзина не найдена"));

        CartItems cartItem = cartItemRepository.findByCartAndBook(cart, book)
                .orElseThrow(() -> new RuntimeException("Товар не найден в корзине"));

        if (quantityToRemove >= cartItem.getQuantity()) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - quantityToRemove);
            cartItem.setSubtotal(calculateSubtotal(cartItem));
            cartItemRepository.save(cartItem);
        }

        recalculateCartTotal(cart);
    }

    public void removeItemCompletelyFromCartForCurrentUser(Books book) {
        Users user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Корзина не найдена"));

        cartItemRepository.deleteByCartAndBook(cart, book);
        recalculateCartTotal(cart);
    }

    public void clearCartForCurrentUser() {
        Users user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Корзина не найдена"));

        cartItemRepository.deleteAllByCart(cart);
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public Cart getCartForCurrentUser() {
        Users user = getCurrentUser();
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Корзина не найдена"));
    }

    private Cart getOrCreateCart(Users user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalAmount(BigDecimal.ZERO);
            return cartRepository.save(newCart);
        });
    }

    private BigDecimal calculateSubtotal(CartItems item) {
        return item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    private BigDecimal calculateSubtotal(Books book, Integer quantity) {
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    private void recalculateCartTotal(Cart cart) {
        BigDecimal total = cartItemRepository.findAllByCart(cart).stream()
                .map(CartItems::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
        cartRepository.save(cart);
    }
}