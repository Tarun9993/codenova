package com.tarun.codenova.cohort.repository;

import com.tarun.codenova.cohort.entity.Cohort;
import com.tarun.codenova.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CohortRepository
        extends JpaRepository<Cohort, Long> {

    boolean existsByNameAndTrainer(
            String name,
            User trainer
    );

    List<Cohort> findByTrainerOrderByCreatedAtDesc(
            User trainer
    );
}