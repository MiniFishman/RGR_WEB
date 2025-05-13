package ru.kors.springsecurityexample.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    private String price;

    @OneToMany
    @JoinColumn(name = "category_id")
    private List<Categories> categories;

    @Column(name = "cover_image")
    private byte[] image;
}
