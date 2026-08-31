package com.tarun.codenova.submission.controller;

import com.tarun.codenova.problem.dto.ProblemSummaryDto;
import com.tarun.codenova.submission.dto.LeaderboardDto;
import com.tarun.codenova.submission.dto.SubmissionRequestDto;
import com.tarun.codenova.submission.dto.SubmissionResponseDto;
import com.tarun.codenova.submission.dto.UserStatsDto;
import com.tarun.codenova.submission.service.SubmissionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<SubmissionResponseDto> createSubmission(@Valid @RequestBody SubmissionRequestDto requestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(submissionService.createSubmission(requestDto));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SubmissionResponseDto>> getMySubmissions(){
        return ResponseEntity.status(HttpStatus.OK).body(submissionService.getMySubmissions());
    }
    @GetMapping("/my/solved")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ProblemSummaryDto>> getSolvedProblems() {

        return ResponseEntity.ok(
                submissionService.getSolvedProblems()
        );
    }
    @GetMapping("/my/stats")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserStatsDto> getMyStats() {

        return ResponseEntity.ok(
                submissionService.getMyStats()
        );
    }
    @GetMapping("/{cohortId}/leaderboard")
    @PreAuthorize("hasAnyRole('TRAINER', 'USER')")
    public ResponseEntity<List<LeaderboardDto>> getCohortLeaderboard(
            @PathVariable Long cohortId) {

        return ResponseEntity.ok(
                submissionService.getCohortLeaderboard(cohortId)
        );
    }
}
