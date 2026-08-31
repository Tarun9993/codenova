package com.tarun.codenova.user.controller;

import com.tarun.codenova.user.dto.ResponseDto;
import com.tarun.codenova.user.dto.TrainerUserRequestDto;
import com.tarun.codenova.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainer/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TrainerUserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<ResponseDto> registerUser(
            @Valid @RequestBody TrainerUserRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        userService.registerUserForCohort(
                                requestDto
                        )
                );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<ResponseDto> getTrainerUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                userService.getTrainerUser(userId)
        );
    }
}