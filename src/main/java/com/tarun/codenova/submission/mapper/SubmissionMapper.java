package com.tarun.codenova.submission.mapper;

import com.tarun.codenova.submission.dto.SubmissionRequestDto;
import com.tarun.codenova.submission.dto.SubmissionResponseDto;
import com.tarun.codenova.submission.entity.Submission;
import com.tarun.codenova.submission.judge0.dto.SubmissionJudgeResultDto;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public Submission toEntity(
            SubmissionRequestDto dto) {

        return Submission.builder()
                .sourceCode(dto.getSourceCode())
                .language(dto.getLanguage())
                .build();
    }


    /*
     * ============================================================
     * RESPONSE WITH JUDGE RESULT
     * ============================================================
     */

    public SubmissionResponseDto toResponseDto(
            Submission submission,
            SubmissionJudgeResultDto judgeResult) {

        SubmissionResponseDto dto =
                new SubmissionResponseDto();

        dto.setId(
                submission.getId()
        );

        dto.setProblemId(
                submission.getProblem().getId()
        );

        dto.setLanguage(
                submission.getLanguage()
        );

        dto.setStatus(
                submission.getStatus()
        );

        dto.setSubmittedAt(
                submission.getSubmittedAt()
        );

        dto.setExecutionTime(
                submission.getExecutionTime()
        );

        dto.setMemoryUsed(
                submission.getMemoryUsed()
        );

        dto.setErrorMessage(
                submission.getErrorMessage()
        );


        if (judgeResult != null) {

            dto.setTotalTestCases(
                    judgeResult.getTotalTestCases()
            );

            dto.setPassedTestCases(
                    judgeResult.getPassedTestCases()
            );

            dto.setTestCases(
                    judgeResult.getTestCases()
            );
        }


        return dto;
    }


    /*
     * ============================================================
     * RESPONSE FROM DATABASE
     *
     * Used by:
     *
     * GET /api/submissions/my
     * ============================================================
     */

    public SubmissionResponseDto toResponseDto(
            Submission submission) {

        SubmissionResponseDto dto =
                new SubmissionResponseDto();

        dto.setId(
                submission.getId()
        );

        dto.setProblemId(
                submission.getProblem().getId()
        );

        dto.setLanguage(
                submission.getLanguage()
        );

        dto.setStatus(
                submission.getStatus()
        );

        dto.setSubmittedAt(
                submission.getSubmittedAt()
        );

        dto.setExecutionTime(
                submission.getExecutionTime()
        );

        dto.setMemoryUsed(
                submission.getMemoryUsed()
        );

        dto.setErrorMessage(
                submission.getErrorMessage()
        );


        /*
         * Test case counts are now stored
         * inside the Submission entity.
         */

        dto.setTotalTestCases(
                submission.getTotalTestCases() != null
                        ? submission.getTotalTestCases()
                        : 0
        );

        dto.setPassedTestCases(
                submission.getPassedTestCases() != null
                        ? submission.getPassedTestCases()
                        : 0
        );


        return dto;
    }
}