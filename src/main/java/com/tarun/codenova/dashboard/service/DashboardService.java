package com.tarun.codenova.dashboard.service;

import com.tarun.codenova.cohort.entity.Cohort;
import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.dashboard.dto.DashboardResponseDto;
import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.enums.Difficulty;
import com.tarun.codenova.submission.entity.Submission;
import com.tarun.codenova.submission.enums.SubmissionStatus;
import com.tarun.codenova.submission.repository.SubmissionRepository;
import com.tarun.codenova.user.entity.User;
import com.tarun.codenova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final SubmissionRepository submissionRepository;

    public DashboardResponseDto getMyDashboard() {

        // 1. Get authenticated user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        // 2. Get all accepted submissions
        List<Submission> acceptedSubmissions =
                submissionRepository.findByUserAndStatus(
                        user,
                        SubmissionStatus.ACCEPTED
                );

        // 3. Get total submissions
        long totalSubmissions =
                submissionRepository.countByUser(user);

        // 4. Remove duplicate solved problems
        Map<Long, Problem> solvedProblems =
                acceptedSubmissions.stream()
                        .collect(Collectors.toMap(
                                submission ->
                                        submission.getProblem().getId(),
                                Submission::getProblem,
                                (existing, duplicate) -> existing
                        ));

        // 5. Total solved
        long totalSolved =
                solvedProblems.size();

        // 6. Easy solved
        long easySolved =
                solvedProblems.values()
                        .stream()
                        .filter(problem ->
                                problem.getDifficulty()
                                        == Difficulty.EASY)
                        .count();

        // 7. Medium solved
        long mediumSolved =
                solvedProblems.values()
                        .stream()
                        .filter(problem ->
                                problem.getDifficulty()
                                        == Difficulty.MEDIUM)
                        .count();

        // 8. Hard solved
        long hardSolved =
                solvedProblems.values()
                        .stream()
                        .filter(problem ->
                                problem.getDifficulty()
                                        == Difficulty.HARD)
                        .count();

        // 9. Acceptance rate
        long acceptedCount =
                acceptedSubmissions.size();

        double acceptanceRate = 0.0;

        if (totalSubmissions > 0) {
            acceptanceRate =
                    ((double) acceptedCount
                            / totalSubmissions) * 100;
        }

        // 10. Calculate score
        long score =
                (easySolved * 1)
                        + (mediumSolved * 3)
                        + (hardSolved * 5);

        // 11. Default values for public users
        Long cohortId = null;
        String cohortName = null;
        int rank = 0;

        // 12. Check whether user belongs to a cohort
        Cohort cohort = user.getCohort();

        if (cohort != null) {

            cohortId = cohort.getId();
            cohortName = cohort.getName();

            /*
             * Find all users in the same cohort.
             */
            List<User> cohortUsers =
                    userRepository.findByCohortId(
                            cohort.getId()
                    );

            /*
             * Calculate the score of every user
             * in the cohort.
             */
            List<Long> scores =
                    cohortUsers.stream()
                            .map(cohortUser -> {

                                List<Submission> accepted =
                                        submissionRepository
                                                .findByUserAndStatus(
                                                        cohortUser,
                                                        SubmissionStatus.ACCEPTED
                                                );

                                Map<Long, Problem> solved =
                                        accepted.stream()
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

                                long easy =
                                        solved.values()
                                                .stream()
                                                .filter(problem ->
                                                        problem.getDifficulty()
                                                                == Difficulty.EASY)
                                                .count();

                                long medium =
                                        solved.values()
                                                .stream()
                                                .filter(problem ->
                                                        problem.getDifficulty()
                                                                == Difficulty.MEDIUM)
                                                .count();

                                long hard =
                                        solved.values()
                                                .stream()
                                                .filter(problem ->
                                                        problem.getDifficulty()
                                                                == Difficulty.HARD)
                                                .count();

                                return (easy * 1)
                                        + (medium * 3)
                                        + (hard * 5);
                            })
                            .toList();

            /*
             * Competition ranking:
             *
             * If three users have:
             *
             * 50
             * 40
             * 40
             * 30
             *
             * Their ranks are:
             *
             * 1
             * 2
             * 2
             * 4
             */
            rank = 1;

            for (Long userScore : scores) {

                if (userScore > score) {
                    rank++;
                }
            }
        }

        // 13. Build dashboard response
        return DashboardResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .cohortId(cohortId)
                .cohortName(cohortName)
                .totalSubmissions(totalSubmissions)
                .totalSolved(totalSolved)
                .easySolved(easySolved)
                .mediumSolved(mediumSolved)
                .hardSolved(hardSolved)
                .acceptanceRate(acceptanceRate)
                .score(score)
                .rank(rank)
                .build();
    }
}