package com.tarun.codenova.submission.judge0.dto;

import lombok.Data;

@Data
public class Judge0ResultResponse {

    private String stdout;

    private String stderr;

    private String compile_output;

    private String message;

    private String time;

    private Long memory;

    private Integer exit_code;

    private Integer exit_signal;

    private Judge0Status status;

    @Data
    public static class Judge0Status {

        private Integer id;

        private String description;
    }
}