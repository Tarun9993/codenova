package com.tarun.codenova.submission.dto;

import com.tarun.codenova.submission.enums.ProgrammingLanguage;
import com.tarun.codenova.submission.enums.SubmissionStatus;
import com.tarun.codenova.submission.judge0.dto.TestCaseResultDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SubmissionResponseDto {

    private Long id;

    private Long problemId;

    private ProgrammingLanguage language;

    private SubmissionStatus status;

    private LocalDateTime submittedAt;

    private Long executionTime;

    private Long memoryUsed;

    private String errorMessage;

    private int totalTestCases;

    private int passedTestCases;

    private List<TestCaseResultDto> testCases;
}