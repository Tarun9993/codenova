package com.tarun.codenova.problem.entity;

import com.tarun.codenova.problem.enums.EvaluationStrategy;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemExecutionConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String methodName;

    /*
     * Examples:
     *
     * int
     * int[]
     * int[],int
     * String
     * String,String
     * int[][]
     */
    @Column(nullable = false)
    private String parameterTypes;

    /*
     * Examples:
     *
     * int
     * long
     * boolean
     * String
     * int[]
     * String[]
     * int[][]
     */
    @Column(nullable = false)
    private String returnType;

    /*
     * Defines how JudgeService evaluates
     * the execution result.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EvaluationStrategy evaluationStrategy =
            EvaluationStrategy.RETURN_VALUE;

    @OneToOne
    @JoinColumn(
            name = "problem_id",
            nullable = false,
            unique = true
    )
    private Problem problem;
}