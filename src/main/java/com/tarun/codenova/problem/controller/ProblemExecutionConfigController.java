package com.tarun.codenova.problem.controller;

import com.tarun.codenova.problem.dto.ProblemExecutionConfigDto;
import com.tarun.codenova.problem.service.ProblemExecutionConfigService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProblemExecutionConfigController {

    private final ProblemExecutionConfigService configService;

    @PostMapping("/{problemId}/execution-config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemExecutionConfigDto> createConfig(
            @PathVariable Long problemId,
            @Valid @RequestBody ProblemExecutionConfigDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(configService.createConfig(problemId, dto));
    }

    @GetMapping("/{problemId}/execution-config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemExecutionConfigDto> getConfig(
            @PathVariable Long problemId) {

        return ResponseEntity.ok(
                configService.getConfig(problemId)
        );
    }

    @PutMapping("/{problemId}/execution-config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemExecutionConfigDto> updateConfig(
            @PathVariable Long problemId,
            @Valid @RequestBody ProblemExecutionConfigDto dto) {

        return ResponseEntity.ok(
                configService.updateConfig(problemId, dto)
        );
    }
}
