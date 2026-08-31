package com.tarun.codenova.cohort.mapper;

import com.tarun.codenova.cohort.dto.CohortResponseDto;
import com.tarun.codenova.cohort.entity.Cohort;
import org.springframework.stereotype.Component;

@Component
public class CohortMapper {

    public CohortResponseDto toResponseDto(Cohort cohort) {

        CohortResponseDto dto = new CohortResponseDto();

        dto.setId(cohort.getId());
        dto.setName(cohort.getName());
        dto.setDescription(cohort.getDescription());

        if (cohort.getTrainer() != null) {
            dto.setTrainerId(cohort.getTrainer().getId());
            dto.setTrainerUsername(
                    cohort.getTrainer().getUsername()
            );
        }
         dto.setStatus(cohort.getStatus());
        dto.setCreatedAt(cohort.getCreatedAt());
        dto.setUpdatedAt(cohort.getUpdatedAt());

        return dto;
    }
}