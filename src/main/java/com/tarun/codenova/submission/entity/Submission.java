package com.tarun.codenova.submission.entity;

import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.submission.enums.ProgrammingLanguage;
import com.tarun.codenova.submission.enums.SubmissionStatus;
import com.tarun.codenova.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgrammingLanguage language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionStatus status;

    private LocalDateTime submittedAt;

    private Long executionTime;

    private Long memoryUsed;

    private Integer totalTestCases;

    private Integer passedTestCases;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}