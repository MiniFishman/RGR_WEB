package ru.kors.springsecurityexample.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookDto {
    private Integer id;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private CategoryDto categories;

    @Data
    public static class CategoryDto {
        private Integer id;
        private String categoryName;
        private String description;
    }
}