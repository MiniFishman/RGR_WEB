package ru.kors.springsecurityexample.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer id;

    private String title;

    private String author;

    private String description;

    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Categories categories;

    @Column(name = "cover_image")
    private byte[] image;
}
