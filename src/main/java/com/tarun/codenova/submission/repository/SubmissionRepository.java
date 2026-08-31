package com.tarun.codenova.submission.repository;

import com.tarun.codenova.problem.enums.Difficulty;
import com.tarun.codenova.submission.entity.Submission;
import com.tarun.codenova.submission.enums.SubmissionStatus;
import com.tarun.codenova.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository
        extends JpaRepository<Submission, Long> {

    List<Submission> findByUserOrderBySubmittedAtDesc(
            User user
    );

    List<Submission> findByUserAndStatus(
            User user,
            SubmissionStatus status
    );

    long countByUser(User user);

    long countByUserAndStatus(
            User user,
            SubmissionStatus status
    );

    long countByUserAndStatusAndProblemDifficulty(
            User user,
            SubmissionStatus status,
            Difficulty difficulty
    );
}