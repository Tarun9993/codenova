//package com.tarun.codenova.submission.controller;
//
//import com.tarun.codenova.submission.judge0.dto.Judge0ResultResponse;
//import com.tarun.codenova.submission.judge0.service.Judge0Service;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/judge0-test")
//@RequiredArgsConstructor
//@SecurityRequirement(name = "bearerAuth")
//public class Judge0TestController {
//
//    private final Judge0Service judge0Service;
//
//    @PostMapping
//    public Judge0ResultResponse testJudge0(
//            @RequestBody Judge0TestRequest request) {
//
//        return judge0Service.execute(
//                request.getSourceCode(),
//                request.getInput()
//        );
//    }
//
//    @lombok.Data
//    public static class Judge0TestRequest {
//        private String sourceCode;
//        private String input;
//        private String expectedOutput;
//    }
//}