package com.tarun.codenova.problem.service;

import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.problem.dto.ProblemTestCaseDto;
import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemTestCase;
import com.tarun.codenova.problem.repository.ProblemRepository;
import com.tarun.codenova.problem.repository.ProblemTestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemTestCaseService {

    private final ProblemTestCaseRepository repository;

    private final ProblemRepository problemRepository;


    public ProblemTestCaseDto addTestCase(
            Long problemId,
            ProblemTestCaseDto dto) {

        Problem problem =
                problemRepository.findById(problemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem is not found with id : "
                                                + problemId
                                )
                        );


        ProblemTestCase testCase =
                ProblemTestCase.builder()
                        .input(dto.getInput())
                        .expectedOutput(dto.getExpectedOutput())
                        .hidden(dto.isHidden())
                        .problem(problem)
                        .build();


        ProblemTestCase saved =
                repository.save(testCase);


        return toDto(saved);
    }


    public ProblemTestCaseDto updateTestCase(
            Long problemId,
            Long testCaseId,
            ProblemTestCaseDto dto) {

        Problem problem =
                problemRepository.findById(problemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem is not found with id : "
                                                + problemId
                                )
                        );


        ProblemTestCase testCase =
                repository.findById(testCaseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Test case is not found with id : "
                                                + testCaseId
                                ));


        if (!testCase.getProblem()
                .getId()
                .equals(problem.getId())) {

            throw new ResourceNotFoundException(
                    "Test case does not belong to this problem"
            );
        }


        testCase.setInput(dto.getInput());

        testCase.setExpectedOutput(
                dto.getExpectedOutput()
        );

        testCase.setHidden(
                dto.isHidden()
        );


        ProblemTestCase saved =
                repository.save(testCase);


        return toDto(saved);
    }


    public void deleteTestCase(
            Long problemId,
            Long testCaseId) {

        Problem problem =
                problemRepository.findById(problemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem is not found with id : "
                                                + problemId
                                )
                        );


        ProblemTestCase testCase =
                repository.findById(testCaseId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Test case is not found with id : "
                                                + testCaseId
                                ));


        if (!testCase.getProblem()
                .getId()
                .equals(problem.getId())) {

            throw new ResourceNotFoundException(
                    "Test case does not belong to this problem"
            );
        }


        repository.delete(testCase);
    }


    private ProblemTestCaseDto toDto(
            ProblemTestCase testCase) {

        return ProblemTestCaseDto.builder()
                .input(testCase.getInput())
                .expectedOutput(
                        testCase.getExpectedOutput()
                )
                .hidden(testCase.isHidden())
                .build();
    }
}