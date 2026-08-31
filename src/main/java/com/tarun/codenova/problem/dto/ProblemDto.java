package com.tarun.codenova.problem.dto;


import com.tarun.codenova.problem.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProblemDto {

    @NotBlank

    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Difficulty difficulty;

    @NotBlank
    private String constraints;

    @NotBlank
    private String inputFormat;

    @NotBlank
    private String outputFormat;
    private List<ProblemExampleDto> examples;

    private String starterCode;
}
