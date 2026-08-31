package com.tarun.codenova.submission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatsDto {

    private long totalSubmissions;
    private long totalSolved;
    private long easySolved;
    private long mediumSolved;
    private long hardSolved;
    private double acceptanceRate;
}