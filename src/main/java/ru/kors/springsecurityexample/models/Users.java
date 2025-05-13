package ru.kors.springsecurityexample.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;
    @Column(unique = true)
    private String login;
    @Column(unique = true)
    private String email;
    @Column(name = "password_hash")
    private String password;
    private String role;
}
