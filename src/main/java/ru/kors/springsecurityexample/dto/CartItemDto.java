package ru.kors.springsecurityexample.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private Integer bookId;
    private String bookTitle;
    private String bookAuthor;  // Добавляем это поле
    private BigDecimal bookPrice;
    private Integer quantity;
    private BigDecimal subtotal;

    public CartItemDto(Integer bookId, String bookTitle, String bookAuthor, BigDecimal bookPrice,
                       Integer quantity, BigDecimal subtotal) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;  // Добавляем в конструктор
        this.bookPrice = bookPrice;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
}