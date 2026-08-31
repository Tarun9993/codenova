package com.tarun.codenova.problem.service;

import com.tarun.codenova.common.exception.ProblemAlreadyExistsException;
import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.problem.dto.ProblemDto;
import com.tarun.codenova.problem.dto.ProblemExampleDto;
import com.tarun.codenova.problem.dto.ProblemPageResponseDto;
import com.tarun.codenova.problem.dto.ProblemSummaryDto;
import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemExample;
import com.tarun.codenova.problem.enums.Difficulty;
import com.tarun.codenova.problem.mapper.ProblemMapper;
import com.tarun.codenova.problem.repository.ProblemExampleRepository;
import com.tarun.codenova.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;
    private final ProblemExampleRepository problemExampleRepository;

    // CREATE PROBLEM
    public ProblemDto createProblem(ProblemDto problemDto) {

        if (problemRepository.existsByTitle(problemDto.getTitle())) {
            throw new ProblemAlreadyExistsException(
                    "Problem already exists");
        }

        Problem problem = problemMapper.toEntity(problemDto);

        LocalDateTime now = LocalDateTime.now();

        problem.setCreatedAt(now);
        problem.setUpdatedAt(now);

        problemRepository.save(problem);

        return problemMapper.toDto(problem);
    }

    // GET PROBLEM BY ID
    public ProblemDto getProblemById(Long id) {

        Problem problem = problemRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Problem not found with id: " + id));

        return problemMapper.toDto(problem);
    }

    // GET ALL PROBLEMS
//    public List<ProblemSummaryDto> getAllProblems() {
//
//        return problemRepository.findAll()
//                .stream()
//                .map(problemMapper::toSummaryDto)
//                .toList();
//    }

    // UPDATE PROBLEM
    public ProblemDto updateProblem(
            Long id,
            ProblemDto problemDto) {

        Problem existingProblem =
                problemRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem not found with id: " + id));

        // Check duplicate title
        if (!existingProblem.getTitle().equals(problemDto.getTitle())
                && problemRepository.existsByTitle(
                problemDto.getTitle())) {

            throw new ProblemAlreadyExistsException(
                    "Problem already exists with title: "
                            + problemDto.getTitle());
        }

        existingProblem.setTitle(problemDto.getTitle());
        existingProblem.setDescription(problemDto.getDescription());
        existingProblem.setDifficulty(problemDto.getDifficulty());
        existingProblem.setConstraints(problemDto.getConstraints());
        existingProblem.setInputFormat(problemDto.getInputFormat());
        existingProblem.setOutputFormat(problemDto.getOutputFormat());

        // Remove old examples
        existingProblem.getExamples().clear();

        // Add new examples with sequential example numbers
        if (problemDto.getExamples() != null) {

            for (int i = 0;
                 i < problemDto.getExamples().size();
                 i++) {

                ProblemExampleDto exampleDto =
                        problemDto.getExamples().get(i);

                ProblemExample example =
                        ProblemExample.builder()
                                .exampleNumber(i + 1)
                                .input(exampleDto.getInput())
                                .output(exampleDto.getOutput())
                                .explanation(exampleDto.getExplanation())
                                .problem(existingProblem)
                                .build();

                existingProblem.getExamples().add(example);
            }
        }

        existingProblem.setUpdatedAt(LocalDateTime.now());

        Problem updatedProblem =
                problemRepository.save(existingProblem);

        return problemMapper.toDto(updatedProblem);
    }

    // DELETE PROBLEM
    public void deleteProblem(Long id) {

        Problem problem =
                problemRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem not found with id: " + id));

        problemRepository.delete(problem);
    }

    // ADD EXAMPLE
    public ProblemExampleDto addExample(
            Long problemId,
            ProblemExampleDto exampleDto) {

        Problem problem =
                problemRepository.findById(problemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem not found with id: "
                                                + problemId));

        // Calculate next example number
        int nextExampleNumber =
                problem.getExamples().size() + 1;

        ProblemExample example =
                ProblemExample.builder()
                        .exampleNumber(nextExampleNumber)
                        .input(exampleDto.getInput())
                        .output(exampleDto.getOutput())
                        .explanation(exampleDto.getExplanation())
                        .problem(problem)
                        .build();

        problemExampleRepository.save(example);

        return problemMapper.toExampleDto(example);
    }
    // UPDATE EXAMPLE
    public ProblemExampleDto updateExample(
            Long problemId,
            Integer exampleNumber,
            ProblemExampleDto exampleDto) {

        // Check whether the problem exists
        Problem problem =
                problemRepository.findById(problemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem not found with id: "
                                                + problemId));

        // Find example using problemId + exampleNumber
        ProblemExample example =
                problemExampleRepository
                        .findByProblemIdAndExampleNumber(
                                problemId,
                                exampleNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Example " + exampleNumber
                                                + " not found for problem "
                                                + problemId));

        // Update only content
        // Example number remains unchanged
        example.setInput(exampleDto.getInput());
        example.setOutput(exampleDto.getOutput());
        example.setExplanation(exampleDto.getExplanation());

        ProblemExample updatedExample =
                problemExampleRepository.save(example);

        return problemMapper.toExampleDto(updatedExample);
    }
    // DELETE EXAMPLE
    public void deleteExample(
            Long problemId,
            Integer exampleNumber) {

        // Check whether the problem exists
        Problem problem =
                problemRepository.findById(problemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Problem not found with id: "
                                                + problemId));

        // Find example using problemId + exampleNumber
        ProblemExample example =
                problemExampleRepository
                        .findByProblemIdAndExampleNumber(
                                problemId,
                                exampleNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Example " + exampleNumber
                                                + " not found for problem "
                                                + problemId));

        // Remove from problem collection
        problem.getExamples().remove(example);

        // Delete actual database record
        problemExampleRepository.delete(example);

        // Renumber remaining examples
        List<ProblemExample> remainingExamples =
                problem.getExamples();

        for (int i = 0; i < remainingExamples.size(); i++) {

            remainingExamples
                    .get(i)
                    .setExampleNumber(i + 1);
        }

        problemRepository.save(problem);
    }

    public ProblemPageResponseDto searchProblems(
            String search,
            Difficulty difficulty,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<Problem> problemPage =
                problemRepository.searchProblems(
                        search,
                        difficulty,
                        pageable
                );

        List<ProblemSummaryDto> problems =
                problemPage.getContent()
                        .stream()
                        .map(problemMapper::toSummaryDto)
                        .toList();

        return new ProblemPageResponseDto(
                problems,
                problemPage.getNumber(),
                problemPage.getSize(),
                problemPage.getTotalElements(),
                problemPage.getTotalPages()
        );
    }
}

