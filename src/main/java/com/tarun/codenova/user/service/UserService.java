package com.tarun.codenova.user.service;

import com.tarun.codenova.auth.dto.ChangePasswordDto;
import com.tarun.codenova.cohort.entity.Cohort;
import com.tarun.codenova.cohort.repository.CohortRepository;
import com.tarun.codenova.common.email.EmailService;
import com.tarun.codenova.common.enums.Roles;
import com.tarun.codenova.common.exception.EmailAlreadyExistsException;
import com.tarun.codenova.common.exception.ResourceNotFoundException;
import com.tarun.codenova.user.dto.RegistrationDto;
import com.tarun.codenova.user.dto.ResponseDto;
import com.tarun.codenova.user.dto.TrainerUserRequestDto;
import com.tarun.codenova.user.dto.UpdateProfileDto;
import com.tarun.codenova.user.entity.User;
import com.tarun.codenova.user.mappers.UserMapper;
import com.tarun.codenova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final CohortRepository cohortRepository;
    private final EmailService emailService;

    public ResponseDto register(RegistrationDto registrationDto) {

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException("EmailAlreadyExist");
        }

        User user = userMapper.dtoToEntity(registrationDto);

        user.setPassword(
                passwordEncoder.encode(
                        registrationDto.getPassword()
                )
        );

        user.setRoles(Roles.USER);

        LocalDateTime now = LocalDateTime.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setMustChangePassword(false);

        User savedUser = userRepository.save(user);

        return userMapper.entityToResponse(savedUser);
    }

    public ResponseDto registerTrainer(
            RegistrationDto registrationDto) {

        if (userRepository.existsByEmail(
                registrationDto.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "EmailAlreadyExist"
            );
        }

        User user =
                userMapper.dtoToEntity(registrationDto);

        user.setPassword(
                passwordEncoder.encode(
                        registrationDto.getPassword()
                )
        );

        user.setRoles(Roles.TRAINER);

        LocalDateTime now = LocalDateTime.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setMustChangePassword(false);

        User savedUser =
                userRepository.save(user);

        return userMapper.entityToResponse(savedUser);
    }

    public ResponseDto registerUserForCohort(
            TrainerUserRequestDto requestDto) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String trainerEmail =
                authentication.getName();

        User trainer =
                userRepository.findByEmail(trainerEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Trainer not found with email: "
                                                + trainerEmail
                                )
                        );

        if (trainer.getRoles() != Roles.TRAINER) {
            throw new IllegalStateException(
                    "Only trainers can create users"
            );
        }

        Cohort cohort =
                cohortRepository.findById(
                        requestDto.getCohortId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cohort not found with id: "
                                        + requestDto.getCohortId()
                        )
                );

        if (!cohort.getTrainer()
                .getId()
                .equals(trainer.getId())) {

            throw new IllegalStateException(
                    "You can only add users to your own cohort"
            );
        }

        if (userRepository.existsByEmail(
                requestDto.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "EmailAlreadyExist"
            );
        }

        String temporaryPassword =
                generateTemporaryPassword();

        LocalDateTime now =
                LocalDateTime.now();

        User user = User.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(
                        passwordEncoder.encode(
                                temporaryPassword
                        )
                )
                .roles(Roles.USER)
                .cohort(cohort)
                .mustChangePassword(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User savedUser =
                userRepository.save(user);

        emailService.sendWelcomeEmail(
                savedUser.getEmail(),
                savedUser.getUsername(),
                temporaryPassword,
                cohort.getName()
        );

        // Temporary for development/testing.
        System.out.println(
                "Temporary password for "
                        + savedUser.getEmail()
                        + " : "
                        + temporaryPassword
        );

        return userMapper.entityToResponse(
                savedUser
        );
    }

    private String generateTemporaryPassword() {

        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "abcdefghijklmnopqrstuvwxyz"
                        + "0123456789"
                        + "@#$%";

        StringBuilder password =
                new StringBuilder();

        SecureRandom random =
                new SecureRandom();

        for (int i = 0; i < 12; i++) {

            int index =
                    random.nextInt(
                            characters.length()
                    );

            password.append(
                    characters.charAt(index)
            );
        }

        return password.toString();
    }

    public void changePassword(ChangePasswordDto requestDto) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + email
                                )
                        );

        // 1. Verify current password
        if (!passwordEncoder.matches(
                requestDto.getCurrentPassword(),
                user.getPassword()
        )) {

            throw new IllegalArgumentException(
                    "Current password is incorrect"
            );
        }

        // 2. New password must be different
        if (passwordEncoder.matches(
                requestDto.getNewPassword(),
                user.getPassword()
        )) {

            throw new IllegalArgumentException(
                    "New password must be different from the current password"
            );
        }

        // 3. Save new password
        user.setPassword(
                passwordEncoder.encode(
                        requestDto.getNewPassword()
                )
        );

        user.setMustChangePassword(false);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public ResponseDto getMyProfile() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        return userMapper.entityToResponse(user);
    }

    public ResponseDto updateMyProfile(
            UpdateProfileDto dto) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        ));

        user.setUsername(dto.getUsername());
        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser =
                userRepository.save(user);

        return userMapper.entityToResponse(updatedUser);
    }


    public ResponseDto getTrainerUser(Long userId) {

        /*
         * =========================================================
         * GET CURRENT TRAINER
         * =========================================================
         */

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String trainerEmail = authentication.getName();

        User trainer =
                userRepository.findByEmail(trainerEmail)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Trainer not found with email: "
                                                + trainerEmail
                                )
                        );


        /*
         * =========================================================
         * FIND LEARNER
         * =========================================================
         */

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + userId
                                )
                        );


        /*
         * =========================================================
         * MAKE SURE TARGET IS A USER
         * =========================================================
         */

        if (user.getRoles() != Roles.USER) {

            throw new IllegalStateException(
                    "Only learner profiles can be viewed"
            );
        }


        /*
         * =========================================================
         * MAKE SURE USER HAS A COHORT
         * =========================================================
         */

        if (user.getCohort() == null) {

            throw new IllegalStateException(
                    "This user is not assigned to a cohort"
            );
        }


        /*
         * =========================================================
         * MAKE SURE COHORT BELONGS TO CURRENT TRAINER
         * =========================================================
         */

        if (user.getCohort().getTrainer() == null ||
                !user.getCohort()
                        .getTrainer()
                        .getId()
                        .equals(trainer.getId())) {

            throw new IllegalStateException(
                    "You can only view users from your own cohorts"
            );
        }


        /*
         * =========================================================
         * RETURN USER PROFILE
         * =========================================================
         */

        return userMapper.entityToResponse(user);
    }
}