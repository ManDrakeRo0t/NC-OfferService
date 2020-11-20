package ru.bogatov.offerservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
