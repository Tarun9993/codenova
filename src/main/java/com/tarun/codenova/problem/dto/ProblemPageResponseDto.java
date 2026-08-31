package com.tarun.codenova.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProblemPageResponseDto {

    private List<ProblemSummaryDto> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}
