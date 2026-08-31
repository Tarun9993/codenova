package com.tarun.codenova.problem.repository;

import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemExecutionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemExecutionConfigRepository extends JpaRepository<ProblemExecutionConfig, Long> {

    boolean existsByProblem(Problem problem);

    Optional<ProblemExecutionConfig> findByProblem(Problem problem);
}
