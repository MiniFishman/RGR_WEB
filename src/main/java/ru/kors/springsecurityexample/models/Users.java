package ru.kors.springsecurityexample.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private String role = "ROLE_USER";
}

