package ru.kors.springsecurityexample.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class LoginRequest {
    private String login;
    private String password;
}
