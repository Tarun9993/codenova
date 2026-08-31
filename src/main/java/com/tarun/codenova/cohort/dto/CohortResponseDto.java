package com.tarun.codenova.cohort.dto;

import com.tarun.codenova.cohort.enums.CohortStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CohortResponseDto {

    private Long id;
    private String name;
    private String description;
    private Long trainerId;
    private String trainerUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CohortStatus status;
}