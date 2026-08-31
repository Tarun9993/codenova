package com.tarun.codenova.submission.service;

import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemExecutionConfig;
import com.tarun.codenova.problem.entity.ProblemTestCase;
import com.tarun.codenova.problem.enums.EvaluationStrategy;
import com.tarun.codenova.problem.repository.ProblemExecutionConfigRepository;
import com.tarun.codenova.problem.repository.ProblemTestCaseRepository;
import com.tarun.codenova.submission.entity.Submission;
import com.tarun.codenova.submission.enums.SubmissionStatus;
import com.tarun.codenova.submission.judge0.dto.Judge0ResultResponse;
import com.tarun.codenova.submission.judge0.dto.SubmissionJudgeResultDto;
import com.tarun.codenova.submission.judge0.dto.TestCaseResultDto;
import com.tarun.codenova.submission.judge0.service.Judge0Service;
import com.tarun.codenova.submission.judge0.service.JavaCodeGeneratorService;
import com.tarun.codenova.submission.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JudgeService {

    private final SubmissionRepository submissionRepository;

    private final ProblemTestCaseRepository testCaseRepository;

    private final ProblemExecutionConfigRepository executionConfigRepository;

    private final Judge0Service judge0Service;

    private final JavaCodeGeneratorService javaCodeGeneratorService;


    // ============================================================
    // JUDGE SUBMISSION
    // ============================================================

    public SubmissionJudgeResultDto judge(
            Submission submission) {

        /*
         * Mark submission as RUNNING
         */

        submission.setStatus(
                SubmissionStatus.RUNNING
        );

        submissionRepository.save(
                submission
        );


        /*
         * Get problem
         */

        Problem problem =
                submission.getProblem();


        /*
         * Get execution configuration
         */

        ProblemExecutionConfig config =
                executionConfigRepository
                        .findByProblem(problem)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Execution config not found for problem id: "
                                                + problem.getId()
                                )
                        );


        /*
         * Get all test cases.
         *
         * This includes both visible and hidden
         * test cases.
         */

        List<ProblemTestCase> testCases =
                testCaseRepository.findByProblem(
                        problem
                );


        if (testCases.isEmpty()) {

            String error =
                    "No test cases found for problem.";

            updateSubmission(
                    submission,
                    SubmissionStatus.RUNTIME_ERROR,
                    0,
                    0,
                    error,
                    0,
                    0
            );

            return buildResult(
                    SubmissionStatus.RUNTIME_ERROR,
                    0,
                    0,
                    error,
                    0,
                    0,
                    new ArrayList<>()
            );
        }


        /*
         * Execution statistics
         */

        long maxExecutionTime = 0;

        long maxMemoryUsed = 0;

        int passedTestCases = 0;


        List<TestCaseResultDto> testCaseResults =
                new ArrayList<>();


        /*
         * ========================================================
         * RUN ALL TEST CASES
         * ========================================================
         */

        for (
                int i = 0;
                i < testCases.size();
                i++
        ) {

            ProblemTestCase testCase =
                    testCases.get(i);

            int testCaseNumber =
                    i + 1;


            // ====================================================
            // GENERATE JAVA CODE
            // ====================================================

            String generatedCode;

            try {

                generatedCode =
                        javaCodeGeneratorService.generate(
                                submission.getSourceCode(),
                                config,
                                testCase
                        );

            } catch (Exception e) {

                String error =
                        "Code generation failed: "
                                + e.getMessage();

                testCaseResults.add(
                        createFailedTestCase(
                                testCase,
                                testCaseNumber,
                                error
                        )
                );


                updateSubmission(
                        submission,
                        SubmissionStatus.COMPILATION_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases
                );


                return buildResult(
                        SubmissionStatus.COMPILATION_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases,
                        testCaseResults
                );
            }


            // ====================================================
            // EXECUTE WITH JUDGE0
            // ====================================================

            Judge0ResultResponse result;

            try {

                result =
                        judge0Service.execute(
                                generatedCode,
                                ""
                        );

            } catch (Exception e) {

                String error =
                        "Judge execution failed: "
                                + e.getMessage();


                testCaseResults.add(
                        createFailedTestCase(
                                testCase,
                                testCaseNumber,
                                error
                        )
                );


                updateSubmission(
                        submission,
                        SubmissionStatus.RUNTIME_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases
                );


                return buildResult(
                        SubmissionStatus.RUNTIME_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases,
                        testCaseResults
                );
            }


            // ====================================================
            // EXECUTION TIME
            // ====================================================

            long executionTime =
                    extractExecutionTime(
                            result
                    );


            maxExecutionTime =
                    Math.max(
                            maxExecutionTime,
                            executionTime
                    );


            // ====================================================
            // MEMORY
            // ====================================================

            long memoryUsed =
                    result.getMemory() != null
                            ? result.getMemory()
                            : 0;


            maxMemoryUsed =
                    Math.max(
                            maxMemoryUsed,
                            memoryUsed
                    );


            // ====================================================
            // JUDGE0 STATUS
            // ====================================================

            int statusId =
                    result.getStatus() != null
                            ? result.getStatus().getId()
                            : -1;


            // ====================================================
            // COMPILATION ERROR
            // ====================================================

            if (statusId == 6) {

                String error =
                        result.getCompile_output();


                if (
                        error == null ||
                                error.isBlank()
                ) {

                    error =
                            "Compilation error";
                }


                testCaseResults.add(
                        TestCaseResultDto.builder()
                                .testCaseNumber(
                                        testCaseNumber
                                )
                                .passed(false)
                                .output(null)
                                .expectedOutput(
                                        visibleExpected(
                                                testCase
                                        )
                                )
                                .error(error)
                                .executionTime(
                                        executionTime
                                )
                                .build()
                );


                updateSubmission(
                        submission,
                        SubmissionStatus.COMPILATION_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases
                );


                return buildResult(
                        SubmissionStatus.COMPILATION_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases,
                        testCaseResults
                );
            }


            // ====================================================
            // TIME LIMIT EXCEEDED
            // ====================================================

            if (statusId == 5) {

                String error =
                        result.getStderr();


                if (
                        error == null ||
                                error.isBlank()
                ) {

                    error =
                            "Time limit exceeded";
                }


                testCaseResults.add(
                        TestCaseResultDto.builder()
                                .testCaseNumber(
                                        testCaseNumber
                                )
                                .passed(false)
                                .output(null)
                                .expectedOutput(
                                        visibleExpected(
                                                testCase
                                        )
                                )
                                .error(error)
                                .executionTime(
                                        executionTime
                                )
                                .build()
                );


                updateSubmission(
                        submission,
                        SubmissionStatus.TIME_LIMIT_EXCEEDED,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases
                );


                return buildResult(
                        SubmissionStatus.TIME_LIMIT_EXCEEDED,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases,
                        testCaseResults
                );
            }


            // ====================================================
            // RUNTIME ERROR
            // ====================================================

            if (statusId >= 7) {

                String error =
                        result.getStderr();


                if (
                        error == null ||
                                error.isBlank()
                ) {

                    error =
                            "Runtime error";
                }


                testCaseResults.add(
                        TestCaseResultDto.builder()
                                .testCaseNumber(
                                        testCaseNumber
                                )
                                .passed(false)
                                .output(null)
                                .expectedOutput(
                                        visibleExpected(
                                                testCase
                                        )
                                )
                                .error(error)
                                .executionTime(
                                        executionTime
                                )
                                .build()
                );


                updateSubmission(
                        submission,
                        SubmissionStatus.RUNTIME_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases
                );


                return buildResult(
                        SubmissionStatus.RUNTIME_ERROR,
                        maxExecutionTime,
                        maxMemoryUsed,
                        error,
                        testCases.size(),
                        passedTestCases,
                        testCaseResults
                );
            }


            // ====================================================
            // NORMAL EXECUTION
            // ====================================================

            String actualOutput =
                    normalizeOutput(
                            result.getStdout()
                    );


            String expectedOutput =
                    normalizeOutput(
                            testCase.getExpectedOutput()
                    );


            // ====================================================
            // EVALUATE
            // ====================================================

            boolean passed =
                    evaluate(
                            actualOutput,
                            expectedOutput,
                            config
                    );


            // ====================================================
            // TEST CASE RESULT
            // ====================================================

            testCaseResults.add(
                    TestCaseResultDto.builder()
                            .testCaseNumber(
                                    testCaseNumber
                            )
                            .passed(passed)
                            .output(
                                    testCase.isHidden()
                                            ? null
                                            : actualOutput
                            )
                            .expectedOutput(
                                    testCase.isHidden()
                                            ? null
                                            : expectedOutput
                            )
                            .error(
                                    passed
                                            ? null
                                            : result.getStderr()
                            )
                            .executionTime(
                                    executionTime
                            )
                            .build()
            );


            // ====================================================
            // WRONG ANSWER
            // ====================================================

            if (!passed) {

                updateSubmission(
                        submission,
                        SubmissionStatus.WRONG_ANSWER,
                        maxExecutionTime,
                        maxMemoryUsed,
                        null,
                        testCases.size(),
                        passedTestCases
                );


                return buildResult(
                        SubmissionStatus.WRONG_ANSWER,
                        maxExecutionTime,
                        maxMemoryUsed,
                        null,
                        testCases.size(),
                        passedTestCases,
                        testCaseResults
                );
            }


            // ====================================================
            // PASSED
            // ====================================================

            passedTestCases++;
        }


        // ========================================================
        // ALL PASSED
        // ========================================================

        updateSubmission(
                submission,
                SubmissionStatus.ACCEPTED,
                maxExecutionTime,
                maxMemoryUsed,
                null,
                testCases.size(),
                passedTestCases
        );


        return buildResult(
                SubmissionStatus.ACCEPTED,
                maxExecutionTime,
                maxMemoryUsed,
                null,
                testCases.size(),
                passedTestCases,
                testCaseResults
        );
    }


    // ============================================================
    // EVALUATION DISPATCHER
    // ============================================================

    private boolean evaluate(
            String actualOutput,
            String expectedOutput,
            ProblemExecutionConfig config) {

        EvaluationStrategy strategy =
                config.getEvaluationStrategy();


        /*
         * Backward compatibility:
         *
         * Existing database records that do not have
         * evaluationStrategy will behave as RETURN_VALUE.
         */

        if (strategy == null) {

            strategy =
                    EvaluationStrategy.RETURN_VALUE;
        }


        return switch (strategy) {

            case RETURN_VALUE ->
                    compareReturnValue(
                            actualOutput,
                            expectedOutput,
                            config.getReturnType()
                    );


            case MUTATED_PREFIX ->
                    compareMutatedPrefix(
                            actualOutput,
                            expectedOutput
                    );


            case MUTATED_ARRAY ->
                    compareMutatedArray(
                            actualOutput,
                            expectedOutput
                    );
        };
    }


    // ============================================================
    // RETURN VALUE
    // ============================================================

    private boolean compareReturnValue(
            String actualOutput,
            String expectedOutput,
            String returnType) {

        actualOutput =
                normalizeOutput(
                        actualOutput
                );


        expectedOutput =
                normalizeOutput(
                        expectedOutput
                );


        /*
         * String
         */

        if ("String".equals(returnType)) {

            return normalizeText(
                    actualOutput
            ).equals(
                    normalizeText(
                            expectedOutput
                    )
            );
        }


        /*
         * Array
         */

        if (
                returnType != null &&
                        returnType.endsWith("[]")
        ) {

            return normalizeArrayOutput(
                    actualOutput
            ).equals(
                    normalizeArrayOutput(
                            expectedOutput
                    )
            );
        }


        /*
         * Primitive values
         */

        return normalizeScalar(
                actualOutput
        ).equals(
                normalizeScalar(
                        expectedOutput
                )
        );
    }


    // ============================================================
    // MUTATED PREFIX
    // ============================================================

    private boolean compareMutatedPrefix(
            String actualOutput,
            String expectedOutput) {

        /*
         * Generated output:
         *
         * k
         * [valid prefix]
         *
         * Example:
         *
         * 2
         * [1, 2]
         *
         * Expected test case must use the
         * same representation.
         */

        return normalizeStructuredOutput(
                actualOutput
        ).equals(
                normalizeStructuredOutput(
                        expectedOutput
                )
        );
    }


    // ============================================================
    // MUTATED ARRAY
    // ============================================================

    private boolean compareMutatedArray(
            String actualOutput,
            String expectedOutput) {

        /*
         * Generated output:
         *
         * [0, 0, 1, 1, 2, 2]
         *
         * Expected output:
         *
         * [0, 0, 1, 1, 2, 2]
         */

        return normalizeArrayOutput(
                actualOutput
        ).equals(
                normalizeArrayOutput(
                        expectedOutput
                )
        );
    }


    // ============================================================
    // OUTPUT NORMALIZATION
    // ============================================================

    private String normalizeOutput(
            String output) {

        if (output == null) {
            return "";
        }


        return output
                .replace(
                        "\r\n",
                        "\n"
                )
                .replace(
                        "\r",
                        "\n"
                )
                .trim();
    }


    // ============================================================
    // TEXT NORMALIZATION
    // ============================================================

    private String normalizeText(
            String output) {

        return normalizeOutput(
                output
        );
    }


    // ============================================================
    // SCALAR NORMALIZATION
    // ============================================================

    private String normalizeScalar(
            String output) {

        return normalizeOutput(
                output
        ).replaceAll(
                "\\s+",
                ""
        );
    }


    // ============================================================
    // ARRAY NORMALIZATION
    // ============================================================

    private String normalizeArrayOutput(
            String output) {

        return normalizeOutput(
                output
        ).replaceAll(
                "\\s+",
                ""
        );
    }


    // ============================================================
    // STRUCTURED OUTPUT NORMALIZATION
    // ============================================================

    private String normalizeStructuredOutput(
            String output) {

        return normalizeOutput(
                output
        ).replaceAll(
                "\\s+",
                ""
        );
    }


    // ============================================================
    // HIDDEN TEST EXPECTED OUTPUT
    // ============================================================

    private String visibleExpected(
            ProblemTestCase testCase) {

        if (testCase.isHidden()) {

            return null;
        }


        return normalizeOutput(
                testCase.getExpectedOutput()
        );
    }


    // ============================================================
    // EXECUTION TIME
    // ============================================================

    private long extractExecutionTime(
            Judge0ResultResponse result) {

        if (result.getTime() == null) {

            return 0;
        }


        try {

            double seconds =
                    Double.parseDouble(
                            result.getTime()
                    );


            return (long)
                    (seconds * 1000);


        } catch (NumberFormatException e) {

            return 0;
        }
    }


    // ============================================================
    // FAILED TEST CASE
    // ============================================================

    private TestCaseResultDto createFailedTestCase(
            ProblemTestCase testCase,
            int testCaseNumber,
            String error) {

        return TestCaseResultDto.builder()
                .testCaseNumber(
                        testCaseNumber
                )
                .passed(false)
                .output(null)
                .expectedOutput(
                        visibleExpected(
                                testCase
                        )
                )
                .error(error)
                .executionTime(0)
                .build();
    }


    // ============================================================
    // BUILD RESULT
    // ============================================================

    private SubmissionJudgeResultDto buildResult(
            SubmissionStatus status,
            long executionTime,
            long memoryUsed,
            String errorMessage,
            int totalTestCases,
            int passedTestCases,
            List<TestCaseResultDto> testCases) {

        return SubmissionJudgeResultDto.builder()
                .status(status)
                .executionTime(executionTime)
                .memoryUsed(memoryUsed)
                .errorMessage(errorMessage)
                .totalTestCases(totalTestCases)
                .passedTestCases(passedTestCases)
                .testCases(testCases)
                .build();
    }


    // ============================================================
    // UPDATE SUBMISSION
    // ============================================================

    private void updateSubmission(
            Submission submission,
            SubmissionStatus status,
            long executionTime,
            long memoryUsed,
            String errorMessage,
            int totalTestCases,
            int passedTestCases) {

        submission.setStatus(
                status
        );

        submission.setExecutionTime(
                executionTime
        );

        submission.setMemoryUsed(
                memoryUsed
        );

        submission.setErrorMessage(
                errorMessage
        );

        submission.setTotalTestCases(
                totalTestCases
        );

        submission.setPassedTestCases(
                passedTestCases
        );

        submissionRepository.save(
                submission
        );
    }
}