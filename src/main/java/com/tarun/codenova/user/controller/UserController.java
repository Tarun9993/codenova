package com.tarun.codenova.user.controller;

import com.tarun.codenova.auth.dto.ChangePasswordDto;
import com.tarun.codenova.user.dto.RegistrationDto;
import com.tarun.codenova.user.dto.ResponseDto;
import com.tarun.codenova.user.dto.UpdateProfileDto;
import com.tarun.codenova.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;


    // =========================================================
    // REGISTER USER
    // =========================================================

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(
            @Valid @RequestBody RegistrationDto registrationDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(registrationDto));
    }


    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordDto requestDto) {

        userService.changePassword(requestDto);

        return ResponseEntity.ok(
                "Password changed successfully"
        );
    }


    // =========================================================
    // GET MY PROFILE
    // USER + TRAINER + ADMIN
    // =========================================================

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto> getMyProfile() {

        return ResponseEntity.ok(
                userService.getMyProfile()
        );
    }


    // =========================================================
    // UPDATE MY PROFILE
    // USER + TRAINER + ADMIN
    // =========================================================

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDto> updateMyProfile(
            @Valid @RequestBody UpdateProfileDto dto) {

        return ResponseEntity.ok(
                userService.updateMyProfile(dto)
        );
    }


    // =========================================================
    // TEST
    // =========================================================

    @GetMapping("/get")
    public String get() {

        return "Hello";
    }
}