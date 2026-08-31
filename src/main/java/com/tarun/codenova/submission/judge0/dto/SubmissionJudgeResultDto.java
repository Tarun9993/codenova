package com.tarun.codenova.submission.judge0.dto;


import com.tarun.codenova.submission.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionJudgeResultDto {

    private SubmissionStatus status;

    private long executionTime;

    private long memoryUsed;

    private String errorMessage;

    private int totalTestCases;

    private int passedTestCases;

    private List<TestCaseResultDto> testCases;
}