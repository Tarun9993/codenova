    package com.tarun.codenova.user.entity;

    import com.tarun.codenova.cohort.entity.Cohort;
    import com.tarun.codenova.common.enums.Roles;
    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String email;
        private String username;
        private String password;
        @Enumerated(EnumType.STRING)
        private Roles roles;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private boolean mustChangePassword;

        @ManyToOne
        @JoinColumn(name = "cohort_id")
        private Cohort cohort;

    }
