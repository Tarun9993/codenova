package com.tarun.codenova.user.controller;

import com.tarun.codenova.user.dto.RegistrationDto;
import com.tarun.codenova.user.dto.ResponseDto;
import com.tarun.codenova.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final UserService userService;

    @PostMapping("/trainers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto> registerTrainer(
            @Valid @RequestBody RegistrationDto registrationDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerTrainer(registrationDto));
    }
}