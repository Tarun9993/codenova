package com.tarun.codenova.common.config;

import com.tarun.codenova.common.enums.Roles;
import com.tarun.codenova.user.entity.User;
import com.tarun.codenova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    //CommandLineRunner is a Spring Boot interface that lets you execute some code automatically once
    // when the application starts successfully.

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.existsByEmail("admin@codenova.com")) {
            return;
        }
        User admin = User.builder()
                .email("admin@codenova.com")
                .username("CodeNova Admin")
                .password(passwordEncoder.encode("Admin@123"))
                .roles(Roles.ADMIN)
                .build();

        userRepository.save(admin);

        System.out.println("Application started!");
    }
}
