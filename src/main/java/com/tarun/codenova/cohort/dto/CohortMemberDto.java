package com.tarun.codenova.cohort.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CohortMemberDto {

    private Long id;
    private String username;
    private String email;
}