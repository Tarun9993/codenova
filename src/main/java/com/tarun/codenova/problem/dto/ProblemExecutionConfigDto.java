package com.tarun.codenova.problem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemExecutionConfigDto {
    @NotBlank
    private String methodName;

    @NotBlank
    private String parameterTypes;

    @NotBlank
    private String returnType;
}
