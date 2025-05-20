package ru.kors.springsecurityexample.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kors.springsecurityexample.dto.CartItemDto;
import ru.kors.springsecurityexample.models.Books;
import ru.kors.springsecurityexample.models.Cart;
import ru.kors.springsecurityexample.models.CartItems;
import ru.kors.springsecurityexample.repository.BookRepository;
import ru.kors.springsecurityexample.services.BookService;
import ru.kors.springsecurityexample.services.CartService;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final BookRepository bookRepository;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public Cart createCart() {
        return cartService.createCartForCurrentUser();
    }

    @DeleteMapping("/delete-cart")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public void deleteCart() {
        cartService.removeCartForCurrentUser();
    }

    @PostMapping("/add-item-to-cart")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public void addItemToCart(@RequestParam Integer bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        cartService.addItemToCartForCurrentUser(book);
    }

    @DeleteMapping("/delete-item")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteItem(@RequestParam Integer bookId,
                                           @RequestParam(required = false, defaultValue = "1") Integer quantityToRemove) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        cartService.removeItemFromCartForCurrentUser(book, quantityToRemove);
        return ResponseEntity.noContent().build(); // Возвращаем 204 No Content
    }

    @DeleteMapping("/delete-full-one-item")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteItemCompletelyFromCart(@RequestParam Integer bookId) {
        Books book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        cartService.removeItemCompletelyFromCartForCurrentUser(book);
        return ResponseEntity.noContent().build(); // Возвращаем 204 No Content
    }

    @PostMapping("/clear")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public void clearCart() {
        cartService.clearCartForCurrentUser();
    }

    @GetMapping("/items")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public List<CartItemDto> getCartItems() {
        List<CartItems> cartItems = cartService.getCartItemsForCurrentUser();

        return cartItems.stream().map(item -> new CartItemDto(
                item.getBook().getId(),
                item.getBook().getTitle(),
                item.getBook().getAuthor(),
                item.getBook().getPrice(),
                item.getQuantity(),
                item.getSubtotal()
        )).toList();
    }
}
