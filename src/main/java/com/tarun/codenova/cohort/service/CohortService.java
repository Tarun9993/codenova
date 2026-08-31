package com.tarun.codenova.cohort.service;

import com.tarun.codenova.cohort.dto.CohortMemberDto;
import com.tarun.codenova.cohort.dto.CohortRequestDto;
import com.tarun.codenova.cohort.dto.CohortResponseDto;
import com.tarun.codenova.cohort.entity.Cohort;
import com.tarun.codenova.cohort.enums.CohortStatus;
import com.tarun.codenova.cohort.mapper.CohortMapper;
import com.tarun.codenova.cohort.repository.CohortRepository;
import com.tarun.codenova.common.enums.Roles;
import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.user.entity.User;
import com.tarun.codenova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CohortService {

    private final CohortRepository cohortRepository;
    private final UserRepository userRepository;
    private final CohortMapper cohortMapper;


    /*
     * =========================================================
     * CREATE COHORT
     * =========================================================
     */

    public CohortResponseDto createCohort(
            CohortRequestDto requestDto) {

        User trainer = getAuthenticatedUser();

        if (trainer.getRoles() != Roles.TRAINER) {

            throw new IllegalStateException(
                    "Only trainers can create cohorts"
            );

        }


        if (cohortRepository.existsByNameAndTrainer(
                requestDto.getName(),
                trainer)) {

            throw new IllegalArgumentException(
                    "Cohort already exists with name: "
                            + requestDto.getName()
            );

        }


        LocalDateTime now =
                LocalDateTime.now();


        Cohort cohort = Cohort.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .trainer(trainer)
                .status(CohortStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();


        Cohort savedCohort =
                cohortRepository.save(cohort);


        return cohortMapper.toResponseDto(
                savedCohort
        );
    }


    /*
     * =========================================================
     * GET MY COHORTS
     * =========================================================
     */

    public List<CohortResponseDto> getMyCohorts() {

        User trainer =
                getAuthenticatedUser();


        if (trainer.getRoles() != Roles.TRAINER) {

            throw new IllegalStateException(
                    "Only trainers can view their cohorts"
            );

        }


        return cohortRepository
                .findByTrainerOrderByCreatedAtDesc(trainer)
                .stream()
                .map(cohortMapper::toResponseDto)
                .toList();
    }


    /*
     * =========================================================
     * UPDATE COHORT
     * =========================================================
     */

    public CohortResponseDto updateCohort(
            Long cohortId,
            CohortRequestDto requestDto) {

        User trainer =
                getAuthenticatedUser();


        Cohort cohort =
                getCohortOwnedByTrainer(
                        cohortId,
                        trainer
                );


        if (cohort.getStatus() ==
                CohortStatus.ARCHIVED) {

            throw new IllegalStateException(
                    "Archived cohorts cannot be edited"
            );

        }


        /*
         * Prevent duplicate cohort name
         * for the same trainer.
         */

        boolean duplicate =
                cohortRepository
                        .existsByNameAndTrainer(
                                requestDto.getName(),
                                trainer
                        );


        if (duplicate &&
                !cohort.getName()
                        .equals(requestDto.getName())) {

            throw new IllegalArgumentException(
                    "Cohort already exists with name: "
                            + requestDto.getName()
            );

        }


        cohort.setName(
                requestDto.getName()
        );

        cohort.setDescription(
                requestDto.getDescription()
        );

        cohort.setUpdatedAt(
                LocalDateTime.now()
        );


        Cohort updatedCohort =
                cohortRepository.save(cohort);


        return cohortMapper.toResponseDto(
                updatedCohort
        );
    }


    /*
     * =========================================================
     * ARCHIVE COHORT
     * =========================================================
     */

    /*
     * =========================================================
     * ARCHIVE COHORT
     * =========================================================
     */

    public CohortResponseDto archiveCohort(
            Long cohortId) {

        User trainer =
                getAuthenticatedUser();


        Cohort cohort =
                getCohortOwnedByTrainer(
                        cohortId,
                        trainer
                );


        if (cohort.getStatus() ==
                CohortStatus.ARCHIVED) {

            throw new IllegalStateException(
                    "Cohort is already archived"
            );

        }


        cohort.setStatus(
                CohortStatus.ARCHIVED
        );

        cohort.setUpdatedAt(
                LocalDateTime.now()
        );


        Cohort archivedCohort =
                cohortRepository.save(cohort);


        return cohortMapper.toResponseDto(
                archivedCohort
        );
    }


    /*
     * =========================================================
     * GET COHORT MEMBERS
     * =========================================================
     */

    public List<CohortMemberDto> getCohortMembers(
            Long cohortId) {

        User trainer =
                getAuthenticatedUser();


        Cohort cohort =
                getCohortOwnedByTrainer(
                        cohortId,
                        trainer
                );


        return userRepository
                .findByCohortId(cohort.getId())
                .stream()
                .map(user ->
                        CohortMemberDto.builder()
                                .id(user.getId())
                                .username(user.getUsername())
                                .email(user.getEmail())
                                .build()
                )
                .toList();
    }


    /*
     * =========================================================
     * FIND COHORT OWNED BY TRAINER
     * =========================================================
     */

    private Cohort getCohortOwnedByTrainer(
            Long cohortId,
            User trainer) {

        Cohort cohort =
                cohortRepository.findById(cohortId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cohort not found with id: "
                                                + cohortId
                                )
                        );


        if (!cohort.getTrainer()
                .getId()
                .equals(trainer.getId())) {

            throw new IllegalStateException(
                    "You can only manage your own cohorts"
            );

        }


        return cohort;
    }


    /*
     * =========================================================
     * AUTHENTICATED USER
     * =========================================================
     */

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        String email =
                authentication.getName();


        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: "
                                        + email
                        )
                );
    }

}