package com.tarun.codenova.dashboard.controller;

import com.tarun.codenova.dashboard.dto.DashboardResponseDto;
import com.tarun.codenova.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DashboardResponseDto> getMyDashboard() {

        return ResponseEntity.ok(
                dashboardService.getMyDashboard()
        );
    }
}
