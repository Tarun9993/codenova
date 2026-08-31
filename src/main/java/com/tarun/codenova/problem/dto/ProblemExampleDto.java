package com.tarun.codenova.problem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProblemExampleDto {

    private Integer exampleNumber;

    @NotBlank
    private String input;

    @NotBlank
    private String output;

    @NotBlank
    private String explanation;
}
