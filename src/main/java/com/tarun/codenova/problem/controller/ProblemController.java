package com.tarun.codenova.problem.controller;

import com.tarun.codenova.problem.dto.ProblemDto;
import com.tarun.codenova.problem.dto.ProblemExampleDto;
import com.tarun.codenova.problem.dto.ProblemPageResponseDto;
import com.tarun.codenova.problem.enums.Difficulty;
import com.tarun.codenova.problem.service.ProblemService;
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
public class ProblemController {

    private final ProblemService problemService;

    // =========================================================
    // CREATE PROBLEM
    // =========================================================

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemDto> createProblem(
            @Valid @RequestBody ProblemDto problemDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(problemService.createProblem(problemDto));
    }


    // =========================================================
    // GET PROBLEM BY ID
    // =========================================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ProblemDto> getProblemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                problemService.getProblemById(id)
        );
    }


    // =========================================================
    // GET / SEARCH ALL PROBLEMS
    // =========================================================

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ProblemPageResponseDto> searchProblems(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            com.tarun.codenova.problem.enums.Difficulty difficulty,

            @RequestParam(defaultValue = "0")
            int page,

            /*
             * Default changed from 10 -> 50.
             *
             * This means:
             *
             * GET /api/problems
             *
             * returns the first 50 problems.
             */
            @RequestParam(defaultValue = "50")
            int size
    ) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page cannot be negative"
            );
        }

        if (size < 1 || size > 50) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 50"
            );
        }

        return ResponseEntity.ok(
                problemService.searchProblems(
                        search,
                        difficulty,
                        page,
                        size
                )
        );
    }


    // =========================================================
    // UPDATE PROBLEM
    // =========================================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemDto> updateProblem(
            @PathVariable Long id,
            @Valid @RequestBody ProblemDto problemDto) {

        return ResponseEntity.ok(
                problemService.updateProblem(
                        id,
                        problemDto
                )
        );
    }


    // =========================================================
    // DELETE PROBLEM
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProblem(
            @PathVariable Long id) {

        problemService.deleteProblem(id);

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // ADD EXAMPLE
    // =========================================================

    @PostMapping("/{problemId}/examples")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemExampleDto> addExample(
            @PathVariable Long problemId,
            @Valid @RequestBody ProblemExampleDto problemExampleDto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        problemService.addExample(
                                problemId,
                                problemExampleDto
                        )
                );
    }


    // =========================================================
    // UPDATE EXAMPLE
    // =========================================================

    @PutMapping("/{problemId}/examples/{exampleNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemExampleDto> updateExample(
            @PathVariable Long problemId,
            @PathVariable Integer exampleNumber,
            @Valid @RequestBody ProblemExampleDto exampleDto) {

        return ResponseEntity.ok(
                problemService.updateExample(
                        problemId,
                        exampleNumber,
                        exampleDto
                )
        );
    }


    // =========================================================
    // DELETE EXAMPLE
    // =========================================================

    @DeleteMapping("/{problemId}/examples/{exampleNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExample(
            @PathVariable Long problemId,
            @PathVariable Integer exampleNumber) {

        problemService.deleteExample(
                problemId,
                exampleNumber
        );

        return ResponseEntity.noContent().build();
    }
}