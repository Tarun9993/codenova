package com.tarun.codenova.cohort.controller;

import com.tarun.codenova.cohort.dto.CohortMemberDto;
import com.tarun.codenova.cohort.dto.CohortRequestDto;
import com.tarun.codenova.cohort.dto.CohortResponseDto;
import com.tarun.codenova.cohort.service.CohortService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cohorts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CohortController {

    private final CohortService cohortService;


    /*
     * =========================================================
     * CREATE
     * =========================================================
     */

    @PostMapping
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<CohortResponseDto> createCohort(
            @Valid @RequestBody CohortRequestDto requestDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        cohortService.createCohort(
                                requestDto
                        )
                );
    }


    /*
     * =========================================================
     * GET MY COHORTS
     * =========================================================
     */

    @GetMapping("/my")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<CohortResponseDto>> getMyCohorts() {

        return ResponseEntity.ok(
                cohortService.getMyCohorts()
        );
    }


    /*
     * =========================================================
     * GET COHORT MEMBERS
     * =========================================================
     */

    @GetMapping("/{cohortId}/users")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<List<CohortMemberDto>> getCohortMembers(
            @PathVariable Long cohortId) {

        return ResponseEntity.ok(
                cohortService.getCohortMembers(
                        cohortId
                )
        );
    }


    /*
     * =========================================================
     * UPDATE COHORT
     * =========================================================
     */

    @PutMapping("/{cohortId}")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<CohortResponseDto> updateCohort(
            @PathVariable Long cohortId,
            @Valid @RequestBody CohortRequestDto requestDto) {

        return ResponseEntity.ok(
                cohortService.updateCohort(
                        cohortId,
                        requestDto
                )
        );
    }


    /*
     * =========================================================
     * ARCHIVE COHORT
     * =========================================================
     */

    @PatchMapping("/{cohortId}/archive")
    @PreAuthorize("hasRole('TRAINER')")
    public ResponseEntity<CohortResponseDto> archiveCohort(
            @PathVariable Long cohortId) {

        return ResponseEntity.ok(
                cohortService.archiveCohort(
                        cohortId
                )
        );
    }

}