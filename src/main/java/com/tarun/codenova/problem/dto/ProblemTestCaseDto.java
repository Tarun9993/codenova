package com.tarun.codenova.problem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemTestCaseDto {

    @NotBlank
    private String input;

    @NotBlank
    private String expectedOutput;

    private boolean hidden;
}