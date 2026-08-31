package com.tarun.codenova.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponseDto {

    private Long userId;

    private String username;

    private String email;

    private Long cohortId;

    private String cohortName;

    private long totalSubmissions;

    private long totalSolved;

    private long easySolved;

    private long mediumSolved;

    private long hardSolved;

    private double acceptanceRate;

    private long score;

    private int rank;
}