package com.tarun.codenova.problem.controller;


import com.tarun.codenova.problem.dto.ProblemTestCaseDto;
import com.tarun.codenova.problem.service.ProblemTestCaseService;
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
public class ProblemTestCaseController {

    private final ProblemTestCaseService problemTestCaseService;

    @PostMapping("/{problemId}/test-cases")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemTestCaseDto> createTestCase(@PathVariable Long problemId,@Valid @RequestBody ProblemTestCaseDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(problemTestCaseService.addTestCase(problemId,dto));
    }
    @PutMapping("/{problemId}/test-cases/{testCaseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProblemTestCaseDto> updateTestCase(
            @PathVariable Long problemId,
            @PathVariable Long testCaseId,
            @Valid @RequestBody ProblemTestCaseDto dto) {

        return ResponseEntity.ok(
                problemTestCaseService.updateTestCase(
                        problemId,
                        testCaseId,
                        dto
                )
        );
    }
    @DeleteMapping("/{problemId}/test-cases/{testCaseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTestCase(
            @PathVariable Long problemId,
            @PathVariable Long testCaseId) {

        problemTestCaseService.deleteTestCase(
                problemId,
                testCaseId
        );

        return ResponseEntity.noContent().build();
    }
}
