package com.tarun.codenova.problem.repository;

import com.tarun.codenova.problem.entity.Problem;
import com.tarun.codenova.problem.enums.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    boolean existsByTitle(String title);

    @Query("""
            SELECT p
            FROM Problem p
            WHERE (:search IS NULL OR
                   LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:difficulty IS NULL OR p.difficulty = :difficulty)
            """)
    Page<Problem> searchProblems(
            @Param("search") String search,
            @Param("difficulty") Difficulty difficulty,
            Pageable pageable
    );
}