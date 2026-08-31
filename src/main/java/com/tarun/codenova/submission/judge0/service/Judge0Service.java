package com.tarun.codenova.submission.judge0.service;

import com.tarun.codenova.submission.judge0.dto.Judge0ResultResponse;
import com.tarun.codenova.submission.judge0.dto.Judge0SubmissionRequest;
import com.tarun.codenova.submission.judge0.dto.Judge0SubmissionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class Judge0Service {

    private final RestClient restClient;
    private final Integer javaLanguageId;

    public Judge0Service(
            RestClient.Builder restClientBuilder,
            @Value("${judge0.base-url}") String baseUrl,
            @Value("${judge0.java-language-id}") Integer javaLanguageId) {

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();

        this.javaLanguageId = javaLanguageId;
    }

    public Judge0ResultResponse execute(
            String sourceCode,
            String input) {

        Judge0SubmissionRequest request =
                Judge0SubmissionRequest.builder()
                        .source_code(sourceCode)
                        .language_id(javaLanguageId)
                        .stdin(input)
                        .build();

        // Submit code to Judge0
        Judge0SubmissionResponse submissionResponse =
                restClient.post()
                        .uri("/submissions?base64_encoded=false&wait=false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(request)
                        .retrieve()
                        .body(Judge0SubmissionResponse.class);

        String token = submissionResponse.getToken();

        // Poll Judge0 until execution finishes
        while (true) {

            Judge0ResultResponse result =
                    restClient.get()
                            .uri(
                                    "/submissions/{token}?base64_encoded=false",
                                    token
                            )
                            .retrieve()
                            .body(Judge0ResultResponse.class);

            Integer statusId = result.getStatus().getId();

            // 1 = In Queue
            // 2 = Processing
            if (statusId != 1 && statusId != 2) {
                return result;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                throw new RuntimeException(
                        "Judge0 polling interrupted",
                        e
                );
            }
        }
    }
}