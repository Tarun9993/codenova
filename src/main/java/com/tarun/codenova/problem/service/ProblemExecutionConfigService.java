package com.tarun.codenova.problem.service;

import com.tarun.codenova.common.exception.ProblemAlreadyExistsException;
import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.problem.dto.ProblemExecutionConfigDto;
import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemExecutionConfig;
import com.tarun.codenova.problem.repository.ProblemExecutionConfigRepository;
import com.tarun.codenova.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemExecutionConfigService {

    private final ProblemRepository problemRepository;
    private final ProblemExecutionConfigRepository configRepository;

    // CREATE
    public ProblemExecutionConfigDto createConfig(Long problemId, ProblemExecutionConfigDto dto) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with id: " + problemId));

        if (configRepository.existsByProblem(problem)) {
            throw new ProblemAlreadyExistsException("Execution config already exists for problem id: " + problemId);
        }

        ProblemExecutionConfig config =
                ProblemExecutionConfig.builder()
                        .methodName(dto.getMethodName())
                        .parameterTypes(dto.getParameterTypes())
                        .returnType(dto.getReturnType())
                        .problem(problem)
                        .build();

        ProblemExecutionConfig saved = configRepository.save(config);
        return toDto(saved);
    }

    // GET
    public ProblemExecutionConfigDto getConfig(Long problemId) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with id: " + problemId));

        ProblemExecutionConfig config =
                configRepository.findByProblem(problem).orElseThrow(() ->
                                new ResourceNotFoundException("Execution config not found for problem id: " + problemId));

        return toDto(config);
    }

    // UPDATE
    public ProblemExecutionConfigDto updateConfig(Long problemId, ProblemExecutionConfigDto dto) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found with id: " + problemId));

        ProblemExecutionConfig config = configRepository.findByProblem(problem)
                        .orElseThrow(() -> new ResourceNotFoundException("Execution config not found for problem id: " + problemId));

        config.setMethodName(dto.getMethodName());
        config.setParameterTypes(dto.getParameterTypes());
        config.setReturnType(dto.getReturnType());

        ProblemExecutionConfig updated =
                configRepository.save(config);

        return toDto(updated);
    }

    // ENTITY → DTO
    private ProblemExecutionConfigDto toDto(
            ProblemExecutionConfig config) {

        return ProblemExecutionConfigDto.builder()
                .methodName(config.getMethodName())
                .parameterTypes(config.getParameterTypes())
                .returnType(config.getReturnType())
                .build();
    }
}