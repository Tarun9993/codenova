package com.tarun.codenova.problem.dto;

import com.tarun.codenova.problem.enums.Difficulty;
import lombok.Data;

@Data
public class ProblemSummaryDto {

    private Long id;
    private String title;
    private Difficulty difficulty;
}
