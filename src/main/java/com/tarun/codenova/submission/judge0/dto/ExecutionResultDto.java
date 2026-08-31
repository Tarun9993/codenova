package com.tarun.codenova.submission.judge0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResultDto {

    private boolean compiled;

    private boolean executed;

    private String output;

    private String error;

    private long executionTime;

    private int exitCode;

    private int totalTestCases;

    private int passedTestCases;

    private String status;

    private List<TestCaseResultDto> testCases;
}