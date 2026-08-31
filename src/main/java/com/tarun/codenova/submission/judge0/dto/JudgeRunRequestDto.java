package com.tarun.codenova.submission.judge0.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JudgeRunRequestDto {
    @NotNull
    private Long problemId;

    @NotBlank
    private String language;

    @NotBlank
    private String sourceCode;

    private String input;
}
