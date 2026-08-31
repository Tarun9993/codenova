package com.tarun.codenova.problem.repository;

import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.entity.ProblemTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemTestCaseRepository
        extends JpaRepository<ProblemTestCase, Long> {

    List<ProblemTestCase> findByProblem(Problem problem);

    List<ProblemTestCase> findByProblemAndHiddenFalse(
            Problem problem
    );
}