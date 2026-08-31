package com.tarun.codenova.auth.service;

import com.tarun.codenova.auth.dto.LoginDto;
import com.tarun.codenova.auth.dto.LoginResponseDto;
import com.tarun.codenova.auth.jwt.JwtService;
import com.tarun.codenova.user.entity.User;
import com.tarun.codenova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public LoginResponseDto login(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                );

        Authentication authentication =
                authenticationManager.authenticate(
                        authenticationToken
                );

        String role = authentication.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        String jwt =
                jwtService.generateToken(
                        loginDto.getEmail(),
                        role
                );

        User user =
                userRepository.findByEmail(
                        loginDto.getEmail()
                ).orElseThrow();

        LoginResponseDto responseDto =
                new LoginResponseDto();

        responseDto.setToken(jwt);
        responseDto.setUsername(user.getUsername());
        responseDto.setMustChangePassword(
                user.isMustChangePassword()
        );
        responseDto.setRole(role);

        return responseDto;
    }
}