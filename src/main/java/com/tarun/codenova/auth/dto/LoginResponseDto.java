package com.tarun.codenova.auth.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String username;
    private String token;
    private boolean mustChangePassword;
    private String role;
}