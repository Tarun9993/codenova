package com.tarun.codenova.submission.judge0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Judge0SubmissionRequest {

    private String source_code;

    private Integer language_id;

    private String stdin;

    private String expected_output;
}