package com.tarun.codenova.submission.dto;

import com.tarun.codenova.submission.enums.ProgrammingLanguage;
import com.tarun.codenova.submission.enums.SubmissionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class SubmissionRequestDto {
    @NotNull
    private Long problemId;

    @NotBlank
    private String sourceCode;

    @NotNull
    private ProgrammingLanguage language;
}
