package com.tarun.codenova.submission.service;

import com.tarun.codenova.cohort.entity.Cohort;
import com.tarun.codenova.cohort.repository.CohortRepository;
import com.tarun.codenova.common.enums.Roles;
import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.problem.dto.ProblemSummaryDto;
import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.enums.Difficulty;
import com.tarun.codenova.problem.mapper.ProblemMapper;
import com.tarun.codenova.problem.repository.ProblemRepository;
import com.tarun.codenova.submission.dto.LeaderboardDto;
import com.tarun.codenova.submission.dto.SubmissionRequestDto;
import com.tarun.codenova.submission.dto.SubmissionResponseDto;
import com.tarun.codenova.submission.dto.UserStatsDto;
import com.tarun.codenova.submission.entity.Submission;
import com.tarun.codenova.submission.enums.SubmissionStatus;
import com.tarun.codenova.submission.judge0.dto.SubmissionJudgeResultDto;
import com.tarun.codenova.submission.mapper.SubmissionMapper;
import com.tarun.codenova.submission.repository.SubmissionRepository;
import com.tarun.codenova.user.entity.User;
import com.tarun.codenova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ProblemRepository problemRepository;

    private final SubmissionRepository submissionRepository;

    private final SubmissionMapper mapper;

    private final UserRepository userRepository;

    private final JudgeService judgeService;

    private final ProblemMapper problemMapper;

    private final CohortRepository cohortRepository;


    // ============================================================
    // CREATE SUBMISSION
    // ============================================================

    public SubmissionResponseDto createSubmission(
            SubmissionRequestDto requestDto) {

        /*
         * 1. Get problem
         */

        Problem problem =
                problemRepository.findById(
                                requestDto.getProblemId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem not found id : "
                                                + requestDto.getProblemId()
                                )
                        );


        /*
         * 2. Get authenticated user
         */

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        /*
         * 3. Get user
         */

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + email
                                )
                        );


        /*
         * 4. Create submission entity
         */

        Submission submission =
                mapper.toEntity(requestDto);

        submission.setProblem(problem);

        submission.setUser(user);

        submission.setSubmittedAt(
                LocalDateTime.now()
        );

        submission.setStatus(
                SubmissionStatus.PENDING
        );


        /*
         * 5. Save initial submission
         */

        Submission savedSubmission =
                submissionRepository.save(
                        submission
                );


        /*
         * 6. Judge submission
         */

        SubmissionJudgeResultDto judgeResult =
                judgeService.judge(
                        savedSubmission
                );


        /*
         * 7. Store judge result inside Submission
         *
         * This is the important change.
         *
         * Previously these values only existed in the
         * response DTO and were lost after the request.
         */

        savedSubmission.setTotalTestCases(
                judgeResult.getTotalTestCases()
        );

        savedSubmission.setPassedTestCases(
                judgeResult.getPassedTestCases()
        );


        /*
         * If JudgeService updates these fields on the
         * Submission itself, they remain untouched.
         *
         * We don't overwrite executionTime/memory/status
         * here because your existing JudgeService is already
         * responsible for the submission result.
         */


        /*
         * 8. Save submission again
         */

        savedSubmission =
                submissionRepository.save(
                        savedSubmission
                );


        /*
         * 9. Convert to response
         */

        SubmissionResponseDto response =
                mapper.toResponseDto(
                        savedSubmission
                );


        /*
         * 10. Add individual judge test-case details
         *
         * These are returned for the current submit request.
         */

        response.setTotalTestCases(
                judgeResult.getTotalTestCases()
        );

        response.setPassedTestCases(
                judgeResult.getPassedTestCases()
        );

        response.setTestCases(
                judgeResult.getTestCases()
        );


        /*
         * 11. Return final submission result
         */

        return response;
    }


    // ============================================================
    // MY SUBMISSIONS
    // ============================================================

    public List<SubmissionResponseDto> getMySubmissions() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + email
                                )
                        );


        return submissionRepository
                .findByUserOrderBySubmittedAtDesc(user)
                .stream()
                .map(mapper::toResponseDto)
                .toList();
    }


    // ============================================================
    // SOLVED PROBLEMS
    // ============================================================

    public List<ProblemSummaryDto> getSolvedProblems() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + email
                                )
                        );


        List<Submission> acceptedSubmissions =
                submissionRepository.findByUserAndStatus(
                        user,
                        SubmissionStatus.ACCEPTED
                );


        return acceptedSubmissions
                .stream()
                .map(Submission::getProblem)
                .collect(
                        Collectors.toMap(
                                Problem::getId,
                                problem -> problem,
                                (existing, duplicate) ->
                                        existing
                        )
                )
                .values()
                .stream()
                .map(problemMapper::toSummaryDto)
                .toList();
    }


    // ============================================================
    // USER STATISTICS
    // ============================================================

    public UserStatsDto getMyStats() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + email
                                )
                        );


        long totalSubmissions =
                submissionRepository.countByUser(user);


        List<Submission> acceptedSubmissions =
                submissionRepository.findByUserAndStatus(
                        user,
                        SubmissionStatus.ACCEPTED
                );


        Map<Long, Problem> solvedProblems =
                acceptedSubmissions
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        submission ->
                                                submission
                                                        .getProblem()
                                                        .getId(),

                                        Submission::getProblem,

                                        (existing, duplicate) ->
                                                existing
                                )
                        );


        long totalSolved =
                solvedProblems.size();


        long acceptedSubmissionCount =
                submissionRepository.countByUserAndStatus(
                        user,
                        SubmissionStatus.ACCEPTED
                );


        double acceptanceRate =
                0.0;


        if (totalSubmissions > 0) {

            acceptanceRate =
                    ((double) acceptedSubmissionCount
                            / totalSubmissions) * 100;
        }


        long easySolved =
                solvedProblems
                        .values()
                        .stream()
                        .filter(problem ->
                                problem.getDifficulty()
                                        == Difficulty.EASY
                        )
                        .count();


        long mediumSolved =
                solvedProblems
                        .values()
                        .stream()
                        .filter(problem ->
                                problem.getDifficulty()
                                        == Difficulty.MEDIUM
                        )
                        .count();


        long hardSolved =
                solvedProblems
                        .values()
                        .stream()
                        .filter(problem ->
                                problem.getDifficulty()
                                        == Difficulty.HARD
                        )
                        .count();


        return new UserStatsDto(
                totalSubmissions,
                totalSolved,
                easySolved,
                mediumSolved,
                hardSolved,
                acceptanceRate
        );
    }


    // ============================================================
    // COHORT LEADERBOARD
    // ============================================================

    public List<LeaderboardDto> getCohortLeaderboard(
            Long cohortId) {

        /*
         * 1. Get authenticated user
         */

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();


        User currentUser =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + email
                                )
                        );


        /*
         * 2. Find cohort
         */

        Cohort cohort =
                cohortRepository.findById(cohortId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cohort not found with id: "
                                                + cohortId
                                )
                        );


        /*
         * 3. Check access
         */

        if (currentUser.getRoles() == Roles.TRAINER) {

            if (!cohort.getTrainer()
                    .getId()
                    .equals(currentUser.getId())) {

                throw new IllegalStateException(
                        "You can only view the leaderboard of your own cohort"
                );
            }

        } else if (currentUser.getRoles() == Roles.USER) {

            if (currentUser.getCohort() == null ||
                    !currentUser.getCohort()
                            .getId()
                            .equals(cohortId)) {

                throw new IllegalStateException(
                        "You can only view the leaderboard of your own cohort"
                );
            }

        } else {

            throw new IllegalStateException(
                    "You are not allowed to view the leaderboard"
            );
        }


        /*
         * 4. Get users
         */

        List<User> users =
                userRepository.findByCohortId(
                        cohortId
                );


        /*
         * 5. Calculate leaderboard
         */

        List<LeaderboardDto> leaderboard =
                users.stream()
                        .map(user -> {

                            List<Submission> acceptedSubmissions =
                                    submissionRepository
                                            .findByUserAndStatus(
                                                    user,
                                                    SubmissionStatus.ACCEPTED
                                            );


                            Map<Long, Problem> solvedProblems =
                                    acceptedSubmissions
                                            .stream()
                                            .collect(
                                                    Collectors.toMap(
                                                            submission ->
                                                                    submission
                                                                            .getProblem()
                                                                            .getId(),

                                                            Submission::getProblem,

                                                            (existing, duplicate) ->
                                                                    existing
                                                    )
                                            );


                            long easySolved =
                                    solvedProblems
                                            .values()
                                            .stream()
                                            .filter(problem ->
                                                    problem.getDifficulty()
                                                            == Difficulty.EASY
                                            )
                                            .count();


                            long mediumSolved =
                                    solvedProblems
                                            .values()
                                            .stream()
                                            .filter(problem ->
                                                    problem.getDifficulty()
                                                            == Difficulty.MEDIUM
                                            )
                                            .count();


                            long hardSolved =
                                    solvedProblems
                                            .values()
                                            .stream()
                                            .filter(problem ->
                                                    problem.getDifficulty()
                                                            == Difficulty.HARD
                                            )
                                            .count();


                            long totalSolved =
                                    solvedProblems.size();


                            long totalSubmissions =
                                    submissionRepository
                                            .countByUser(user);


                            long acceptedCount =
                                    acceptedSubmissions.size();


                            double acceptanceRate =
                                    0.0;


                            if (totalSubmissions > 0) {

                                acceptanceRate =
                                        ((double) acceptedCount
                                                / totalSubmissions) * 100;
                            }


                            long score =
                                    (easySolved * 1)
                                            + (mediumSolved * 3)
                                            + (hardSolved * 5);


                            return LeaderboardDto.builder()
                                    .userId(
                                            user.getId()
                                    )
                                    .username(
                                            user.getUsername()
                                    )
                                    .totalSolved(
                                            totalSolved
                                    )
                                    .easySolved(
                                            easySolved
                                    )
                                    .mediumSolved(
                                            mediumSolved
                                    )
                                    .hardSolved(
                                            hardSolved
                                    )
                                    .acceptanceRate(
                                            acceptanceRate
                                    )
                                    .score(
                                            score
                                    )
                                    .build();

                        })
                        .sorted(
                                Comparator
                                        .comparingLong(
                                                LeaderboardDto::getScore
                                        )
                                        .reversed()

                                        .thenComparing(
                                                Comparator
                                                        .comparingLong(
                                                                LeaderboardDto
                                                                        ::getTotalSolved
                                                        )
                                                        .reversed()
                                        )

                                        .thenComparing(
                                                Comparator
                                                        .comparingDouble(
                                                                LeaderboardDto
                                                                        ::getAcceptanceRate
                                                        )
                                                        .reversed()
                                        )
                        )
                        .toList();


        /*
         * 6. Assign rank
         */

        for (
                int i = 0;
                i < leaderboard.size();
                i++
        ) {

            leaderboard
                    .get(i)
                    .setRank(i + 1);
        }


        return leaderboard;
    }
}