package com.tarun.codenova.cohort.entity;

import com.tarun.codenova.cohort.enums.CohortStatus;
import com.tarun.codenova.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cohort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CohortStatus status = CohortStatus.ACTIVE;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}