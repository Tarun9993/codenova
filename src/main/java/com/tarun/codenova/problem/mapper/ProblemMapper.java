 package com.tarun.codenova.problem.mapper;

import com.tarun.codenova.problem.dto.ProblemDto;
import com.tarun.codenova.problem.dto.ProblemExampleDto;
import com.tarun.codenova.problem.dto.ProblemSummaryDto;
import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemExample;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProblemMapper {

    public Problem toEntity(ProblemDto dto) {

        Problem problem = Problem.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .difficulty(dto.getDifficulty())
                .constraints(dto.getConstraints())
                .inputFormat(dto.getInputFormat())
                .outputFormat(dto.getOutputFormat())
                .build();

        if (dto.getExamples() != null) {

            List<ProblemExample> examples = new ArrayList<>();

            for (int i = 0; i < dto.getExamples().size(); i++) {

                ProblemExample example =
                        toExampleEntity(
                                dto.getExamples().get(i),
                                problem,
                                i + 1
                        );

                examples.add(example);
            }

            problem.setExamples(examples);
        }

        return problem;
    }

    private ProblemExample toExampleEntity(
            ProblemExampleDto dto,
            Problem problem,
            int exampleNumber) {

        return ProblemExample.builder()
                .exampleNumber(exampleNumber)
                .input(dto.getInput())
                .output(dto.getOutput())
                .explanation(dto.getExplanation())
                .problem(problem)
                .build();
    }

    public ProblemDto toDto(Problem problem) {

        ProblemDto dto = new ProblemDto();

        dto.setTitle(problem.getTitle());
        dto.setDescription(problem.getDescription());
        dto.setDifficulty(problem.getDifficulty());
        dto.setConstraints(problem.getConstraints());
        dto.setInputFormat(problem.getInputFormat());
        dto.setOutputFormat(problem.getOutputFormat());

        if (problem.getExamples() != null) {

            List<ProblemExampleDto> examples =
                    problem.getExamples()
                            .stream()
                            .map(this::toExampleDto)
                            .toList();

            dto.setExamples(examples);
            dto.setStarterCode(problem.getStarterCode());
        }

        return dto;
    }

    public ProblemExampleDto toExampleDto(ProblemExample example) {

        ProblemExampleDto dto = new ProblemExampleDto();

        dto.setExampleNumber(example.getExampleNumber());
        dto.setInput(example.getInput());
        dto.setOutput(example.getOutput());
        dto.setExplanation(example.getExplanation());

        return dto;
    }

    public ProblemSummaryDto toSummaryDto(Problem problem) {

        ProblemSummaryDto dto = new ProblemSummaryDto();

        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setDifficulty(problem.getDifficulty());

        return dto;
    }
}

