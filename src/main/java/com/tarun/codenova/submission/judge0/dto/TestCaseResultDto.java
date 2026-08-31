package com.tarun.codenova.submission.judge0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResultDto {

    private int testCaseNumber;

    private boolean passed;

    private String output;

    private String expectedOutput;

    private String error;

    private long executionTime;
}