package com.tarun.codenova.cohort.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CohortRequestDto {

    @NotBlank
    private String name;

    private String description;
}