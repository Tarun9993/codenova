package com.tarun.codenova.submission.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderboardDto {

    private int rank;

    private Long userId;

    private String username;

    private long totalSolved;

    private long easySolved;

    private long mediumSolved;

    private long hardSolved;

    private double acceptanceRate;

    private long score;
}
