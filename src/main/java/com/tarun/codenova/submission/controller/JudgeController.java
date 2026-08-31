package com.tarun.codenova.submission.controller;

import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemExecutionConfig;
import com.tarun.codenova.problem.entity.ProblemTestCase;
import com.tarun.codenova.problem.repository.ProblemExecutionConfigRepository;
import com.tarun.codenova.problem.repository.ProblemRepository;
import com.tarun.codenova.problem.repository.ProblemTestCaseRepository;
import com.tarun.codenova.submission.judge0.dto.ExecutionResultDto;
import com.tarun.codenova.submission.judge0.dto.Judge0ResultResponse;
import com.tarun.codenova.submission.judge0.dto.JudgeRunRequestDto;
import com.tarun.codenova.submission.judge0.dto.TestCaseResultDto;
import com.tarun.codenova.submission.judge0.service.Judge0Service;
import com.tarun.codenova.submission.judge0.service.JavaCodeGeneratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/judge")
@RequiredArgsConstructor
public class JudgeController {

    private final Judge0Service judge0Service;

    private final JavaCodeGeneratorService javaCodeGeneratorService;

    private final ProblemRepository problemRepository;

    private final ProblemExecutionConfigRepository executionConfigRepository;

    private final ProblemTestCaseRepository testCaseRepository;


    @PostMapping("/run")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ExecutionResultDto> runCode(
            @Valid @RequestBody JudgeRunRequestDto request) {

        /*
         * 1. Get problem
         */

        Problem problem =
                problemRepository.findById(request.getProblemId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem not found with id: "
                                                + request.getProblemId()
                                )
                        );


        /*
         * 2. Get execution configuration
         */

        ProblemExecutionConfig config =
                executionConfigRepository
                        .findByProblem(problem)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Execution config not found for problem id: "
                                                + problem.getId()
                                )
                        );


        /*
         * 3. Get all visible test cases
         *
         * RUN executes only example/visible test cases.
         */

        List<ProblemTestCase> testCases =
                testCaseRepository
                        .findByProblemAndHiddenFalse(problem);


        if (testCases.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No visible test cases found for problem id: "
                            + problem.getId()
            );
        }


        /*
         * 4. Store test case results
         */

        List<TestCaseResultDto> testCaseResults =
                new ArrayList<>();

        int passedTestCases = 0;

        long totalExecutionTime = 0;

        boolean compiled = true;

        boolean executed = true;

        int exitCode = 0;


        /*
         * 5. Execute every visible test case
         */

        for (int i = 0; i < testCases.size(); i++) {

            ProblemTestCase testCase =
                    testCases.get(i);


            /*
             * Generate complete Java program
             */

            String generatedCode =
                    javaCodeGeneratorService.generate(
                            request.getSourceCode(),
                            config,
                            testCase
                    );


            /*
             * Execute through Judge0
             */

            Judge0ResultResponse judgeResult =
                    judge0Service.execute(
                            generatedCode,
                            ""
                    );


            /*
             * Get status
             */

            Integer statusId =
                    judgeResult.getStatus() != null
                            ? judgeResult.getStatus().getId()
                            : null;


            /*
             * Compilation error
             */

            if (statusId != null && statusId == 6) {

                compiled = false;

                executed = false;

                exitCode =
                        judgeResult.getExit_code() != null
                                ? judgeResult.getExit_code()
                                : -1;


                String compileError =
                        judgeResult.getCompile_output() != null
                                ? judgeResult.getCompile_output()
                                : "Compilation failed.";


                testCaseResults.add(
                        TestCaseResultDto.builder()
                                .testCaseNumber(i + 1)
                                .passed(false)
                                .output("")
                                .expectedOutput(
                                        testCase.getExpectedOutput()
                                )
                                .error(compileError)
                                .executionTime(0)
                                .build()
                );

                /*
                 * No point executing remaining test cases
                 * when compilation failed.
                 */

                break;
            }


            /*
             * Execution time
             */

            long executionTime = 0;

            if (judgeResult.getTime() != null) {

                double seconds =
                        Double.parseDouble(
                                judgeResult.getTime()
                        );

                executionTime =
                        (long) (seconds * 1000);
            }

            totalExecutionTime += executionTime;


            /*
             * Actual output
             */

            String actualOutput =
                    judgeResult.getStdout() != null
                            ? judgeResult.getStdout().trim()
                            : "";


            /*
             * Expected output
             */

            String expectedOutput =
                    testCase.getExpectedOutput() != null
                            ? testCase.getExpectedOutput().trim()
                            : "";


            /*
             * Compare output
             */

            boolean passed =
                    actualOutput.equals(expectedOutput);


            if (passed) {

                passedTestCases++;
            }


            /*
             * Execution status
             */

            if (statusId == null || statusId != 3) {

                executed = false;
            }


            /*
             * Exit code
             */

            if (judgeResult.getExit_code() != null) {

                exitCode =
                        judgeResult.getExit_code();
            }


            /*
             * Error
             */

            String error =
                    judgeResult.getStderr() != null
                            ? judgeResult.getStderr()
                            : "";


            /*
             * Store test case result
             */

            testCaseResults.add(
                    TestCaseResultDto.builder()
                            .testCaseNumber(i + 1)
                            .passed(passed)
                            .output(actualOutput)
                            .expectedOutput(expectedOutput)
                            .error(error)
                            .executionTime(executionTime)
                            .build()
            );
        }


        /*
         * 6. Determine final status
         */

        String status;

        if (!compiled) {

            status = "Compilation Error";

        } else if (!executed) {

            status = "Runtime Error";

        } else if (passedTestCases == testCases.size()) {

            status = "Accepted";

        } else {

            status = "Wrong Answer";
        }


        /*
         * 7. Overall output
         *
         * Keep the first output in the existing
         * output field for frontend compatibility.
         */

        String output = "";

        String error = "";

        if (!testCaseResults.isEmpty()) {

            TestCaseResultDto firstResult =
                    testCaseResults.get(0);

            output =
                    firstResult.getOutput() != null
                            ? firstResult.getOutput()
                            : "";

            error =
                    firstResult.getError() != null
                            ? firstResult.getError()
                            : "";
        }


        /*
         * 8. Build existing ExecutionResultDto
         */

        ExecutionResultDto result =
                ExecutionResultDto.builder()
                        .compiled(compiled)
                        .executed(executed)
                        .output(output)
                        .error(error)
                        .executionTime(totalExecutionTime)
                        .exitCode(exitCode)
                        .totalTestCases(testCases.size())
                        .passedTestCases(passedTestCases)
                        .status(status)
                        .testCases(testCaseResults)
                        .build();


        return ResponseEntity.ok(result);
    }
}