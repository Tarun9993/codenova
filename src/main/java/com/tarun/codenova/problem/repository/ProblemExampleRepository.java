package com.tarun.codenova.problem.repository;

import com.tarun.codenova.problem.entity.ProblemExample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemExampleRepository extends JpaRepository<ProblemExample,Long> {

    Optional<ProblemExample> findByProblemIdAndExampleNumber(
            Long problemId,
            Integer exampleNumber
    );

}
