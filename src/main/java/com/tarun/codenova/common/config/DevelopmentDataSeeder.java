//package com.tarun.codenova.common.config;
//
//import com.tarun.codenova.cohort.entity.Cohort;
//import com.tarun.codenova.cohort.enums.CohortStatus;
//import com.tarun.codenova.cohort.repository.CohortRepository;
//import com.tarun.codenova.common.enums.Roles;
//import com.tarun.codenova.problem.entity.Problem;
//import com.tarun.codenova.problem.entity.ProblemExample;
//import com.tarun.codenova.problem.entity.ProblemExecutionConfig;
//import com.tarun.codenova.problem.entity.ProblemTestCase;
//import com.tarun.codenova.problem.enums.Difficulty;
//import com.tarun.codenova.problem.enums.EvaluationStrategy;
//import com.tarun.codenova.problem.repository.ProblemExampleRepository;
//import com.tarun.codenova.problem.repository.ProblemExecutionConfigRepository;
//import com.tarun.codenova.problem.repository.ProblemRepository;
//import com.tarun.codenova.problem.repository.ProblemTestCaseRepository;
//import com.tarun.codenova.submission.entity.Submission;
//import com.tarun.codenova.submission.enums.ProgrammingLanguage;
//import com.tarun.codenova.submission.enums.SubmissionStatus;
//import com.tarun.codenova.submission.repository.SubmissionRepository;
//import com.tarun.codenova.user.entity.User;
//import com.tarun.codenova.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Profile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@Profile("dev")
//@RequiredArgsConstructor
//public class DevelopmentDataSeeder implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final CohortRepository cohortRepository;
//
//    private final ProblemRepository problemRepository;
//    private final ProblemExampleRepository exampleRepository;
//    private final ProblemExecutionConfigRepository configRepository;
//    private final ProblemTestCaseRepository testCaseRepository;
//
//    private final SubmissionRepository submissionRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    // ============================================================
//    // SEED
//    // ============================================================
//
//    @Override
//    public void run(String... args) {
//
//        /*
//         * Prevent duplicate development data.
//         */
//        if (userRepository.existsByEmail("tarun@example.com")) {
//
//            System.out.println(
//                    "Development data already exists. Skipping seed."
//            );
//
//            return;
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//
//        System.out.println(
//                "========== CODENOVA SEED START =========="
//        );
//
//        // ========================================================
//        // USERS
//        // ========================================================
//
//        User admin = user(
//                "admin@codenova.com",
//                "admin",
//                "Admin@123",
//                Roles.ADMIN,
//                now
//        );
//
//        User trainer1 = user(
//                "srini@example.com",
//                "Srini",
//                "Trainer@123",
//                Roles.TRAINER,
//                now
//        );
//
//        User trainer2 = user(
//                "harsha@example.com",
//                "Harsha",
//                "Trainer@123",
//                Roles.TRAINER,
//                now
//        );
//
//        // ========================================================
//        // COHORTS
//        // ========================================================
//
//        Cohort jfsr = cohort(
//                "JFSR-AUG-2026",
//                "August JFSR Training Cohort",
//                trainer1,
//                CohortStatus.ACTIVE,
//                now
//        );
//
//        Cohort java = cohort(
//                "JAVA-SEPT-2026",
//                "September Java Training Cohort",
//                trainer1,
//                CohortStatus.ACTIVE,
//                now
//        );
//
//        Cohort spring = cohort(
//                "SPRING-BOOT-2026",
//                "Spring Boot Training Cohort",
//                trainer2,
//                CohortStatus.ACTIVE,
//                now
//        );
//
//        cohort(
//                "OLD-JAVA-2025",
//                "Archived Java Training Cohort",
//                trainer2,
//                CohortStatus.ARCHIVED,
//                now
//        );
//
//        // ========================================================
//        // USERS
//        // ========================================================
//
//        User tarun = user(
//                "tarun@example.com",
//                "Tarun_18",
//                "User@123",
//                Roles.USER,
//                now
//        );
//
//        User nikhil = user(
//                "nikhil@example.com",
//                "Nikhil",
//                "User@123",
//                Roles.USER,
//                now
//        );
//
//        User ananya = user(
//                "ananya@example.com",
//                "Ananya",
//                "User@123",
//                Roles.USER,
//                now
//        );
//
//        User rahul = user(
//                "rahul@example.com",
//                "Rahul",
//                "User@123",
//                Roles.USER,
//                now
//        );
//
//        // ========================================================
//        // ASSIGN COHORTS
//        // ========================================================
//
//        tarun.setCohort(jfsr);
//        nikhil.setCohort(jfsr);
//        ananya.setCohort(java);
//        rahul.setCohort(spring);
//
//        userRepository.saveAll(
//                List.of(
//                        tarun,
//                        nikhil,
//                        ananya,
//                        rahul
//                )
//        );
//
//        // ========================================================
//        // PROBLEMS
//        // ========================================================
//
//        Problem binarySearch = binarySearch(now);
//
//        Problem stock = stock(now);
//
//        Problem maximumSubarray = maximumSubarray(now);
//
//        Problem longestSubstring =
//                longestSubstring(now);
//
//        Problem validAnagram =
//                validAnagram(now);
//
//        Problem containsDuplicate =
//                containsDuplicate(now);
//
//        Problem addTwoIntegers =
//                addTwoIntegers(now);
//
//        Problem twoSum =
//                twoSum(now);
//
//        Problem concatenationOfArray =
//                concatenationOfArray(now);
//
//        Problem removeElement =
//                removeElement(now);
//
//        Problem removeDuplicates =
//                removeDuplicates(now);
//
//        Problem moveZeroes =
//                moveZeroes(now);
//
//        Problem sortColors =
//                sortColors(now);
//
//        Problem richestCustomerWealth =
//                richestCustomerWealth(now);
//
//        Problem singleNumber =
//                singleNumber(now);
//
//        Problem toLowerCase =
//                toLowerCase(now);
//
//        Problem reverseNumber =
//                reverseNumber(now);
//
//        Problem factorial =
//                factorial(now);
//
//        // ========================================================
//        // SAMPLE SUBMISSIONS
//        // ========================================================
//
//        submission(
//                tarun,
//                binarySearch,
//                SubmissionStatus.ACCEPTED,
//                51L,
//                16828L,
//                6,
//                6,
//                null,
//                now.minusDays(4)
//        );
//
//        submission(
//                tarun,
//                stock,
//                SubmissionStatus.ACCEPTED,
//                45L,
//                16020L,
//                6,
//                6,
//                null,
//                now.minusDays(3)
//        );
//
//        submission(
//                tarun,
//                maximumSubarray,
//                SubmissionStatus.WRONG_ANSWER,
//                55L,
//                16120L,
//                6,
//                3,
//                null,
//                now.minusDays(2)
//        );
//
//        submission(
//                tarun,
//                validAnagram,
//                SubmissionStatus.COMPILATION_ERROR,
//                0L,
//                0L,
//                6,
//                0,
//                "Compilation error",
//                now.minusDays(1)
//        );
//
//        submission(
//                nikhil,
//                binarySearch,
//                SubmissionStatus.ACCEPTED,
//                48L,
//                15900L,
//                6,
//                6,
//                null,
//                now.minusDays(5)
//        );
//
//        submission(
//                nikhil,
//                longestSubstring,
//                SubmissionStatus.ACCEPTED,
//                52L,
//                16100L,
//                6,
//                6,
//                null,
//                now.minusDays(3)
//        );
//
//        submission(
//                ananya,
//                stock,
//                SubmissionStatus.ACCEPTED,
//                43L,
//                15700L,
//                6,
//                6,
//                null,
//                now.minusDays(2)
//        );
//
//        submission(
//                rahul,
//                containsDuplicate,
//                SubmissionStatus.WRONG_ANSWER,
//                61L,
//                16500L,
//                6,
//                2,
//                null,
//                now.minusDays(1)
//        );
//
//        // ========================================================
//        // COMPLETE
//        // ========================================================
//
//        System.out.println(
//                "========== CODENOVA SEED COMPLETE =========="
//        );
//
//        System.out.println(
//                "Problems seeded: 18"
//        );
//
//        System.out.println(
//                "Admin   : admin@codenova.com / Admin@123"
//        );
//
//        System.out.println(
//                "Trainer : srini@example.com / Trainer@123"
//        );
//
//        System.out.println(
//                "Trainer : harsha@example.com / Trainer@123"
//        );
//
//        System.out.println(
//                "User    : tarun@example.com / User@123"
//        );
//
//        System.out.println(
//                "User    : nikhil@example.com / User@123"
//        );
//
//        System.out.println(
//                "User    : ananya@example.com / User@123"
//        );
//
//        System.out.println(
//                "User    : rahul@example.com / User@123"
//        );
//    }
//
//    // ============================================================
//    // USER
//    // ============================================================
//
//    private User user(
//            String email,
//            String username,
//            String password,
//            Roles role,
//            LocalDateTime now
//    ) {
//
//        return userRepository.save(
//                User.builder()
//                        .email(email)
//                        .username(username)
//                        .password(
//                                passwordEncoder.encode(password)
//                        )
//                        .roles(role)
//                        .createdAt(now)
//                        .updatedAt(now)
//                        .mustChangePassword(false)
//                        .build()
//        );
//    }
//
//    // ============================================================
//    // COHORT
//    // ============================================================
//
//    private Cohort cohort(
//            String name,
//            String description,
//            User trainer,
//            CohortStatus status,
//            LocalDateTime now
//    ) {
//
//        return cohortRepository.save(
//                Cohort.builder()
//                        .name(name)
//                        .description(description)
//                        .trainer(trainer)
//                        .status(status)
//                        .createdAt(now)
//                        .updatedAt(now)
//                        .build()
//        );
//    }
//
//    // ============================================================
//    // PROBLEM
//    // ============================================================
//
//    private Problem problem(
//            String title,
//            String description,
//            Difficulty difficulty,
//            String constraints,
//            String inputFormat,
//            String outputFormat,
//            String starterCode,
//            LocalDateTime now
//    ) {
//
//        Problem problem =
//                Problem.builder()
//                        .title(title)
//                        .description(description)
//                        .difficulty(difficulty)
//                        .constraints(constraints)
//                        .inputFormat(inputFormat)
//                        .outputFormat(outputFormat)
//                        .starterCode(starterCode)
//                        .createdAt(now)
//                        .updatedAt(now)
//                        .build();
//
//        return problemRepository.save(problem);
//    }
//
//    // ============================================================
//    // EXAMPLE
//    // ============================================================
//
//    private void example(
//            Problem problem,
//            int number,
//            String input,
//            String output,
//            String explanation
//    ) {
//
//        exampleRepository.save(
//                ProblemExample.builder()
//                        .exampleNumber(number)
//                        .input(input)
//                        .output(output)
//                        .explanation(explanation)
//                        .problem(problem)
//                        .build()
//        );
//    }
//
//    // ============================================================
//    // EXECUTION CONFIG
//    // ============================================================
//
//    private void config(
//            Problem problem,
//            String methodName,
//            String parameterTypes,
//            String returnType,
//            EvaluationStrategy evaluationStrategy
//    ) {
//
//        configRepository.save(
//                ProblemExecutionConfig.builder()
//                        .methodName(methodName)
//                        .parameterTypes(parameterTypes)
//                        .returnType(returnType)
//                        .evaluationStrategy(
//                                evaluationStrategy
//                        )
//                        .problem(problem)
//                        .build()
//        );
//    }
//
//    // ============================================================
//    // TEST CASES
//    // ============================================================
//
//    private void tests(
//            Problem problem,
//            TestData... tests
//    ) {
//
//        for (TestData test : tests) {
//
//            testCaseRepository.save(
//                    ProblemTestCase.builder()
//                            .input(test.input)
//                            .expectedOutput(test.output)
//                            .hidden(test.hidden)
//                            .problem(problem)
//                            .build()
//            );
//        }
//    }
//
//    // ============================================================
//    // SUBMISSION
//    // ============================================================
//
//    private void submission(
//            User user,
//            Problem problem,
//            SubmissionStatus status,
//            Long executionTime,
//            Long memoryUsed,
//            int total,
//            int passed,
//            String errorMessage,
//            LocalDateTime submittedAt
//    ) {
//
//        submissionRepository.save(
//                Submission.builder()
//                        .user(user)
//                        .problem(problem)
//                        .sourceCode("class Solution {}")
//                        .language(ProgrammingLanguage.JAVA)
//                        .status(status)
//                        .submittedAt(submittedAt)
//                        .executionTime(executionTime)
//                        .memoryUsed(memoryUsed)
//                        .totalTestCases(total)
//                        .passedTestCases(passed)
//                        .errorMessage(errorMessage)
//                        .build()
//        );
//    }
//
//    // ============================================================
//    // 1. BINARY SEARCH
//    // ============================================================
//
//    private Problem binarySearch(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Binary Search",
//
//                "Given a sorted array of integers and a target value, "
//                        + "return the index of the target. If the target "
//                        + "does not exist, return -1.",
//
//                Difficulty.EASY,
//
//                "1 <= nums.length <= 10000\n"
//                        + "-10000 <= nums[i] <= 10000\n"
//                        + "-10000 <= target <= 10000",
//
//                "nums = [-1,0,3,5,9,12], target = 9",
//
//                "Return the index of target, or -1 if it does not exist.",
//
//                "public class Solution {\n\n"
//                        + "    public int search(int[] nums, int target) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return -1;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[-1,0,3,5,9,12],9",
//                "4",
//                "Target 9 is at index 4."
//        );
//
//        example(
//                problem,
//                2,
//                "[-1,0,3,5,9,12],2",
//                "-1",
//                "Target does not exist."
//        );
//
//        config(
//                problem,
//                "search",
//                "int[],int",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[-1,0,3,5,9,12],9]",
//                        "4",
//                        false
//                ),
//
//                new TestData(
//                        "[[-1,0,3,5,9,12],2]",
//                        "-1",
//                        false
//                ),
//
//                new TestData(
//                        "[[1],1]",
//                        "0",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,2,3,4,5],5]",
//                        "4",
//                        true
//                ),
//
//                new TestData(
//                        "[[2,4,6,8,10],7]",
//                        "-1",
//                        true
//                ),
//
//                new TestData(
//                        "[[10,20,30,40],10]",
//                        "0",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 2. BEST TIME TO BUY AND SELL STOCK
//    // ============================================================
//
//    private Problem stock(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Best Time to Buy and Sell Stock",
//
//                "Given an array of stock prices where prices[i] "
//                        + "represents the price on the ith day, find "
//                        + "the maximum profit possible by buying once "
//                        + "and selling once.",
//
//                Difficulty.EASY,
//
//                "1 <= prices.length <= 10000\n"
//                        + "0 <= prices[i] <= 100000",
//
//                "[7,1,5,3,6,4]",
//
//                "Return the maximum possible profit.",
//
//                "public class Solution {\n\n"
//                        + "    public int maxProfit(int[] prices) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[7,1,5,3,6,4]",
//                "5",
//                "Buy at 1 and sell at 6."
//        );
//
//        example(
//                problem,
//                2,
//                "[7,6,4,3,1]",
//                "0",
//                "No profitable transaction exists."
//        );
//
//        config(
//                problem,
//                "maxProfit",
//                "int[]",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[7,1,5,3,6,4]]",
//                        "5",
//                        false
//                ),
//
//                new TestData(
//                        "[[7,6,4,3,1]]",
//                        "0",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,2]]",
//                        "1",
//                        false
//                ),
//
//                new TestData(
//                        "[[2,4,1,7]]",
//                        "6",
//                        true
//                ),
//
//                new TestData(
//                        "[[5,4,3,2,10]]",
//                        "8",
//                        true
//                ),
//
//                new TestData(
//                        "[[3,3,3,3]]",
//                        "0",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 3. MAXIMUM SUBARRAY
//    // ============================================================
//
//    private Problem maximumSubarray(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Maximum Subarray",
//
//                "Given an integer array, find the contiguous subarray "
//                        + "with the largest sum and return its sum.",
//
//                Difficulty.MEDIUM,
//
//                "1 <= nums.length <= 10000\n"
//                        + "-10000 <= nums[i] <= 10000",
//
//                "[-2,1,-3,4,-1,2,1,-5,4]",
//
//                "Return the largest possible subarray sum.",
//
//                "public class Solution {\n\n"
//                        + "    public int maxSubArray(int[] nums) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[-2,1,-3,4,-1,2,1,-5,4]",
//                "6",
//                "The subarray [4,-1,2,1] has sum 6."
//        );
//
//        example(
//                problem,
//                2,
//                "[1]",
//                "1",
//                "The only element forms the maximum subarray."
//        );
//
//        config(
//                problem,
//                "maxSubArray",
//                "int[]",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[-2,1,-3,4,-1,2,1,-5,4]]",
//                        "6",
//                        false
//                ),
//
//                new TestData(
//                        "[[1]]",
//                        "1",
//                        false
//                ),
//
//                new TestData(
//                        "[[5,4,-1,7,8]]",
//                        "23",
//                        false
//                ),
//
//                new TestData(
//                        "[[-1,-2,-3]]",
//                        "-1",
//                        true
//                ),
//
//                new TestData(
//                        "[[1,2,3,4]]",
//                        "10",
//                        true
//                ),
//
//                new TestData(
//                        "[[-5,2,-1,3,-10]]",
//                        "4",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 4. LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS
//    // ============================================================
//
//    private Problem longestSubstring(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Longest Substring Without Repeating Characters",
//
//                "Given a string, find the length of the longest substring "
//                        + "without repeating characters.",
//
//                Difficulty.MEDIUM,
//
//                "0 <= s.length() <= 10000",
//
//                "\"abcabcbb\"",
//
//                "Return the length of the longest substring without duplicates.",
//
//                "public class Solution {\n\n"
//                        + "    public int lengthOfLongestSubstring(String s) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[\"abcabcbb\"]",
//                "3",
//                "The longest substring is abc."
//        );
//
//        example(
//                problem,
//                2,
//                "[\"bbbbb\"]",
//                "1",
//                "Only one unique character exists."
//        );
//
//        config(
//                problem,
//                "lengthOfLongestSubstring",
//                "String",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[\"abcabcbb\"]",
//                        "3",
//                        false
//                ),
//
//                new TestData(
//                        "[\"bbbbb\"]",
//                        "1",
//                        false
//                ),
//
//                new TestData(
//                        "[\"pwwkew\"]",
//                        "3",
//                        false
//                ),
//
//                new TestData(
//                        "[\"abcdef\"]",
//                        "6",
//                        true
//                ),
//
//                new TestData(
//                        "[\"abba\"]",
//                        "2",
//                        true
//                ),
//
//                new TestData(
//                        "[\"\"]",
//                        "0",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 5. VALID ANAGRAM
//    // ============================================================
//
//    private Problem validAnagram(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Valid Anagram",
//
//                "Given two strings, determine whether they are anagrams "
//                        + "of each other.",
//
//                Difficulty.EASY,
//
//                "1 <= s.length(), t.length() <= 50000\n"
//                        + "Strings contain lowercase English letters.",
//
//                "\"anagram\", \"nagaram\"",
//
//                "Return true if the strings are anagrams, otherwise false.",
//
//                "public class Solution {\n\n"
//                        + "    public boolean isAnagram(String s, String t) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return false;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[\"anagram\",\"nagaram\"]",
//                "true",
//                "Both strings contain the same characters."
//        );
//
//        example(
//                problem,
//                2,
//                "[\"rat\",\"car\"]",
//                "false",
//                "The character counts are different."
//        );
//
//        config(
//                problem,
//                "isAnagram",
//                "String,String",
//                "boolean",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[\"anagram\",\"nagaram\"]",
//                        "true",
//                        false
//                ),
//
//                new TestData(
//                        "[\"rat\",\"car\"]",
//                        "false",
//                        false
//                ),
//
//                new TestData(
//                        "[\"listen\",\"silent\"]",
//                        "true",
//                        false
//                ),
//
//                new TestData(
//                        "[\"hello\",\"world\"]",
//                        "false",
//                        true
//                ),
//
//                new TestData(
//                        "[\"abc\",\"abc\"]",
//                        "true",
//                        true
//                ),
//
//                new TestData(
//                        "[\"aabb\",\"abab\"]",
//                        "true",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 6. CONTAINS DUPLICATE
//    // ============================================================
//
//    private Problem containsDuplicate(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Contains Duplicate",
//
//                "Given an integer array, determine whether any value "
//                        + "appears at least twice.",
//
//                Difficulty.EASY,
//
//                "1 <= nums.length <= 10000\n"
//                        + "-100000 <= nums[i] <= 100000",
//
//                "[1,2,3,1]",
//
//                "Return true if any value appears more than once.",
//
//                "public class Solution {\n\n"
//                        + "    public boolean containsDuplicate(int[] nums) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return false;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[1,2,3,1]",
//                "true",
//                "The value 1 appears twice."
//        );
//
//        example(
//                problem,
//                2,
//                "[1,2,3,4]",
//                "false",
//                "All values are unique."
//        );
//
//        config(
//                problem,
//                "containsDuplicate",
//                "int[]",
//                "boolean",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[1,2,3,1]]",
//                        "true",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,2,3,4]]",
//                        "false",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,1]]",
//                        "true",
//                        false
//                ),
//
//                new TestData(
//                        "[[5,4,3,2,1]]",
//                        "false",
//                        true
//                ),
//
//                new TestData(
//                        "[[1,2,2,3]]",
//                        "true",
//                        true
//                ),
//
//                new TestData(
//                        "[[10,20,30,40,50]]",
//                        "false",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 7. ADD TWO INTEGERS
//    // ============================================================
//
//    private Problem addTwoIntegers(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Add Two Integers",
//
//                "Given two integers, return their sum.",
//
//                Difficulty.EASY,
//
//                "-1000 <= num1, num2 <= 1000",
//
//                "num1 = 12, num2 = 5",
//
//                "Return the sum of num1 and num2.",
//
//                "public class Solution {\n\n"
//                        + "    public int sum(int num1, int num2) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[12,5]",
//                "17",
//                "12 + 5 = 17."
//        );
//
//        example(
//                problem,
//                2,
//                "[-10,4]",
//                "-6",
//                "-10 + 4 = -6."
//        );
//
//        config(
//                problem,
//                "sum",
//                "int,int",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[12,5]",
//                        "17",
//                        false
//                ),
//
//                new TestData(
//                        "[-10,4]",
//                        "-6",
//                        false
//                ),
//
//                new TestData(
//                        "[0,0]",
//                        "0",
//                        false
//                ),
//
//                new TestData(
//                        "[1000,1000]",
//                        "2000",
//                        true
//                ),
//
//                new TestData(
//                        "[-1000,-1000]",
//                        "-2000",
//                        true
//                ),
//
//                new TestData(
//                        "[-1000,1000]",
//                        "0",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 8. TWO SUM
//    // ============================================================
//
//    private Problem twoSum(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Two Sum",
//
//                "Given an integer array nums and an integer target, "
//                        + "return the indices of the two numbers whose "
//                        + "sum equals target.",
//
//                Difficulty.EASY,
//
//                "2 <= nums.length <= 10000\n"
//                        + "-100000 <= nums[i] <= 100000\n"
//                        + "-100000 <= target <= 100000\n"
//                        + "Exactly one valid answer exists.",
//
//                "nums = [2,7,11,15], target = 9",
//
//                "Return the two indices as an integer array.",
//
//                "public class Solution {\n\n"
//                        + "    public int[] twoSum(int[] nums, int target) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return new int[0];\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[2,7,11,15],9",
//                "[0,1]",
//                "nums[0] + nums[1] = 9."
//        );
//
//        example(
//                problem,
//                2,
//                "[3,2,4],6",
//                "[1,2]",
//                "nums[1] + nums[2] = 6."
//        );
//
//        config(
//                problem,
//                "twoSum",
//                "int[],int",
//                "int[]",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[2,7,11,15],9]",
//                        "[0, 1]",
//                        false
//                ),
//
//                new TestData(
//                        "[[3,2,4],6]",
//                        "[1, 2]",
//                        false
//                ),
//
//                new TestData(
//                        "[[3,3],6]",
//                        "[0, 1]",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,5,8,10],13]",
//                        "[1, 2]",
//                        true
//                ),
//
//                new TestData(
//                        "[[-3,4,3,90],0]",
//                        "[0, 2]",
//                        true
//                ),
//
//                new TestData(
//                        "[[10,20,30,40],70]",
//                        "[2, 3]",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 9. CONCATENATION OF ARRAY
//    // ============================================================
//
//    private Problem concatenationOfArray(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Concatenation of Array",
//
//                "Given an integer array nums, create an array ans "
//                        + "where ans consists of two copies of nums "
//                        + "placed one after another.",
//
//                Difficulty.EASY,
//
//                "1 <= nums.length <= 1000\n"
//                        + "-1000 <= nums[i] <= 1000",
//
//                "[1,2,1,1]",
//
//                "Return the concatenated array.",
//
//                "public class Solution {\n\n"
//                        + "    public int[] getConcatenation(int[] nums) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return new int[0];\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[1,2,1,1]",
//                "[1,2,1,1,1,2,1,1]",
//                "The array is repeated twice."
//        );
//
//        example(
//                problem,
//                2,
//                "[1,3,2]",
//                "[1,3,2,1,3,2]",
//                "The second half is another copy of nums."
//        );
//
//        config(
//                problem,
//                "getConcatenation",
//                "int[]",
//                "int[]",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[1,2,1,1]]",
//                        "[1, 2, 1, 1, 1, 2, 1, 1]",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,3,2]]",
//                        "[1, 3, 2, 1, 3, 2]",
//                        false
//                ),
//
//                new TestData(
//                        "[[5]]",
//                        "[5, 5]",
//                        false
//                ),
//
//                new TestData(
//                        "[[0,0,0]]",
//                        "[0, 0, 0, 0, 0, 0]",
//                        true
//                ),
//
//                new TestData(
//                        "[[-1,2,-3]]",
//                        "[-1, 2, -3, -1, 2, -3]",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 10. REMOVE ELEMENT
//    // ============================================================
//
//    private Problem removeElement(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Remove Element",
//
//                "Given an integer array nums and an integer val, "
//                        + "remove all occurrences of val in-place. "
//                        + "Return the number of elements remaining.",
//
//                Difficulty.EASY,
//
//                "0 <= nums.length <= 10000\n"
//                        + "-100 <= nums[i] <= 100\n"
//                        + "-100 <= val <= 100",
//
//                "[3,2,2,3], val = 3",
//
//                "First print the number of remaining elements and "
//                        + "then the valid prefix of the modified array.",
//
//                "public class Solution {\n\n"
//                        + "    public int removeElement(int[] nums, int val) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[3,2,2,3],3",
//                "2\n[2,2]",
//                "Two occurrences of 3 are removed."
//        );
//
//        example(
//                problem,
//                2,
//                "[0,1,2,2,3,0,4,2],2",
//                "5\n[0,1,3,0,4]",
//                "All occurrences of 2 are removed."
//        );
//
//        config(
//                problem,
//                "removeElement",
//                "int[],int",
//                "int",
//                EvaluationStrategy.MUTATED_PREFIX
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[3,2,2,3],3]",
//                        "2\n[2, 2]",
//                        false
//                ),
//
//                new TestData(
//                        "[[0,1,2,2,3,0,4,2],2]",
//                        "5\n[0, 1, 3, 0, 4]",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,1,1],1]",
//                        "0\n[]",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,2,3,4],5]",
//                        "4\n[1, 2, 3, 4]",
//                        true
//                ),
//
//                new TestData(
//                        "[[5,5,1,5,2],5]",
//                        "2\n[1, 2]",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 11. REMOVE DUPLICATES FROM SORTED ARRAY
//    // ============================================================
//
//    private Problem removeDuplicates(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Remove Duplicates from Sorted Array",
//
//                "Given a sorted integer array nums, remove duplicates "
//                        + "in-place so that each unique element appears "
//                        + "only once. Return the number of unique elements.",
//
//                Difficulty.EASY,
//
//                "1 <= nums.length <= 10000\n"
//                        + "nums is sorted in non-decreasing order.",
//
//                "[1,1,2]",
//
//                "First print the number of unique elements and then "
//                        + "the valid prefix of nums.",
//
//                "public class Solution {\n\n"
//                        + "    public int removeDuplicates(int[] nums) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[1,1,2]",
//                "2\n[1,2]",
//                "The unique values are 1 and 2."
//        );
//
//        example(
//                problem,
//                2,
//                "[0,0,1,1,1,2,2,3,3,4]",
//                "5\n[0,1,2,3,4]",
//                "Five unique values remain."
//        );
//
//        config(
//                problem,
//                "removeDuplicates",
//                "int[]",
//                "int",
//                EvaluationStrategy.MUTATED_PREFIX
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[1,1,2]]",
//                        "2\n[1, 2]",
//                        false
//                ),
//
//                new TestData(
//                        "[[0,0,1,1,1,2,2,3,3,4]]",
//                        "5\n[0, 1, 2, 3, 4]",
//                        false
//                ),
//
//                new TestData(
//                        "[[1]]",
//                        "1\n[1]",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,2,3,4,5]]",
//                        "5\n[1, 2, 3, 4, 5]",
//                        true
//                ),
//
//                new TestData(
//                        "[[2,2,2,2,2]]",
//                        "1\n[2]",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 12. MOVE ZEROES
//    // ============================================================
//
//    private Problem moveZeroes(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Move Zeroes",
//
//                "Given an integer array, move all zeroes to the end "
//                        + "while maintaining the relative order of "
//                        + "the non-zero elements.",
//
//                Difficulty.EASY,
//
//                "1 <= nums.length <= 10000\n"
//                        + "-1000 <= nums[i] <= 1000",
//
//                "[0,1,0,3,12]",
//
//                "Modify nums in-place and print the complete modified array.",
//
//                "public class Solution {\n\n"
//                        + "    public void moveZeroes(int[] nums) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[0,1,0,3,12]",
//                "[1,3,12,0,0]",
//                "All zeroes move to the end."
//        );
//
//        example(
//                problem,
//                2,
//                "[0]",
//                "[0]",
//                "The single zero remains unchanged."
//        );
//
//        config(
//                problem,
//                "moveZeroes",
//                "int[]",
//                "void",
//                EvaluationStrategy.MUTATED_ARRAY
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[0,1,0,3,12]]",
//                        "[1, 3, 12, 0, 0]",
//                        false
//                ),
//
//                new TestData(
//                        "[[0]]",
//                        "[0]",
//                        false
//                ),
//
//                new TestData(
//                        "[[1,2,3]]",
//                        "[1, 2, 3]",
//                        false
//                ),
//
//                new TestData(
//                        "[[0,0,1]]",
//                        "[1, 0, 0]",
//                        true
//                ),
//
//                new TestData(
//                        "[[1,0,2,0,3,0]]",
//                        "[1, 2, 3, 0, 0, 0]",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 13. SORT COLORS
//    // ============================================================
//
//    private Problem sortColors(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Sort Colors",
//
//                "Given an array containing only 0, 1, and 2, sort "
//                        + "the array in-place so that objects of the "
//                        + "same color are adjacent.",
//
//                Difficulty.MEDIUM,
//
//                "1 <= nums.length <= 10000\n"
//                        + "nums[i] is 0, 1, or 2.",
//
//                "[2,0,2,1,1,0]",
//
//                "Modify nums in-place and print the complete sorted array.",
//
//                "public class Solution {\n\n"
//                        + "    public void sortColors(int[] nums) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[2,0,2,1,1,0]",
//                "[0,0,1,1,2,2]",
//                "The values are sorted in ascending order."
//        );
//
//        example(
//                problem,
//                2,
//                "[2,0,1]",
//                "[0,1,2]",
//                "The array becomes [0,1,2]."
//        );
//
//        config(
//                problem,
//                "sortColors",
//                "int[]",
//                "void",
//                EvaluationStrategy.MUTATED_ARRAY
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[2,0,2,1,1,0]]",
//                        "[0, 0, 1, 1, 2, 2]",
//                        false
//                ),
//
//                new TestData(
//                        "[[2,0,1]]",
//                        "[0, 1, 2]",
//                        false
//                ),
//
//                new TestData(
//                        "[[0]]",
//                        "[0]",
//                        false
//                ),
//
//                new TestData(
//                        "[[2,2,2,2]]",
//                        "[2, 2, 2, 2]",
//                        true
//                ),
//
//                new TestData(
//                        "[[0,1,2,0,1,2]]",
//                        "[0, 0, 1, 1, 2, 2]",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 14. RICHEST CUSTOMER WEALTH
//    // ============================================================
//
//    private Problem richestCustomerWealth(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Richest Customer Wealth",
//
//                "Given a 2D array where accounts[i][j] represents "
//                        + "the amount of money customer i has in bank j, "
//                        + "return the wealth of the richest customer.",
//
//                Difficulty.EASY,
//
//                "1 <= accounts.length <= 100\n"
//                        + "1 <= accounts[i].length <= 100\n"
//                        + "1 <= accounts[i][j] <= 1000",
//
//                "[[1,2,3],[3,2,1]]",
//
//                "Return the maximum row sum.",
//
//                "public class Solution {\n\n"
//                        + "    public int maximumWealth(int[][] accounts) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[[1,2,3],[3,2,1]]",
//                "6",
//                "Both customers have total wealth 6."
//        );
//
//        example(
//                problem,
//                2,
//                "[[1,5],[7,3],[3,5]]",
//                "10",
//                "The second customer has wealth 10."
//        );
//
//        config(
//                problem,
//                "maximumWealth",
//                "int[][]",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[[1,2,3],[3,2,1]]]",
//                        "6",
//                        false
//                ),
//
//                new TestData(
//                        "[[[1,5],[7,3],[3,5]]]",
//                        "10",
//                        false
//                ),
//
//                new TestData(
//                        "[[[10]]]",
//                        "10",
//                        false
//                ),
//
//                new TestData(
//                        "[[[1,1,1],[5,5],[2,2,2,2]]]",
//                        "10",
//                        true
//                ),
//
//                new TestData(
//                        "[[[100,200],[50,50,50],[10,20,30,40]]]",
//                        "300",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 15. SINGLE NUMBER
//    // ============================================================
//
//    private Problem singleNumber(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Single Number",
//
//                "Given a non-empty integer array where every element "
//                        + "appears twice except for one element, return "
//                        + "the element that appears only once.",
//
//                Difficulty.EASY,
//
//                "1 <= nums.length <= 10000\n"
//                        + "nums.length is odd.\n"
//                        + "Every element appears twice except one.",
//
//                "[2,2,1]",
//
//                "Return the element that appears only once.",
//
//                "public class Solution {\n\n"
//                        + "    public int singleNumber(int[] nums) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[2,2,1]",
//                "1",
//                "Only 1 appears once."
//        );
//
//        example(
//                problem,
//                2,
//                "[4,1,2,1,2]",
//                "4",
//                "Only 4 appears once."
//        );
//
//        config(
//                problem,
//                "singleNumber",
//                "int[]",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[[2,2,1]]",
//                        "1",
//                        false
//                ),
//
//                new TestData(
//                        "[[4,1,2,1,2]]",
//                        "4",
//                        false
//                ),
//
//                new TestData(
//                        "[[1]]",
//                        "1",
//                        false
//                ),
//
//                new TestData(
//                        "[[7,3,7,3,9]]",
//                        "9",
//                        true
//                ),
//
//                new TestData(
//                        "[[-1,2,-1,2,5]]",
//                        "5",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 16. TO LOWER CASE
//    // ============================================================
//
//    private Problem toLowerCase(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "To Lower Case",
//
//                "Given a string, convert all uppercase English letters "
//                        + "to lowercase letters.",
//
//                Difficulty.EASY,
//
//                "1 <= s.length() <= 1000\n"
//                        + "The string contains printable ASCII characters.",
//
//                "\"Hello\"",
//
//                "Return the converted lowercase string.",
//
//                "public class Solution {\n\n"
//                        + "    public String toLowerCase(String s) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return \"\";\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[\"Hello\"]",
//                "\"hello\"",
//                "Uppercase H becomes lowercase h."
//        );
//
//        example(
//                problem,
//                2,
//                "[\"here\"]",
//                "\"here\"",
//                "The string is already lowercase."
//        );
//
//        config(
//                problem,
//                "toLowerCase",
//                "String",
//                "String",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[\"Hello\"]",
//                        "hello",
//                        false
//                ),
//
//                new TestData(
//                        "[\"here\"]",
//                        "here",
//                        false
//                ),
//
//                new TestData(
//                        "[\"LOVELY\"]",
//                        "lovely",
//                        false
//                ),
//
//                new TestData(
//                        "[\"CodeNova123\"]",
//                        "codenova123",
//                        true
//                ),
//
//                new TestData(
//                        "[\"JAVA Spring BOOT\"]",
//                        "java spring boot",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 17. REVERSE NUMBER
//    // ============================================================
//
//    private Problem reverseNumber(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Reverse of a Number",
//
//                "Given an integer n, return the number obtained by "
//                        + "reversing its digits.",
//
//                Difficulty.EASY,
//
//                "-2147483648 <= n <= 2147483647\n"
//                        + "Inputs are chosen so that the reversed value "
//                        + "fits in the integer range.",
//
//                "1234",
//
//                "Return the reversed integer.",
//
//                "public class Solution {\n\n"
//                        + "    public int reverse(int n) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[1234]",
//                "4321",
//                "The digits 1,2,3,4 become 4,3,2,1."
//        );
//
//        example(
//                problem,
//                2,
//                "[-123]",
//                "-321",
//                "The sign is preserved."
//        );
//
//        config(
//                problem,
//                "reverse",
//                "int",
//                "int",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[1234]",
//                        "4321",
//                        false
//                ),
//
//                new TestData(
//                        "[-123]",
//                        "-321",
//                        false
//                ),
//
//                new TestData(
//                        "[120]",
//                        "21",
//                        false
//                ),
//
//                new TestData(
//                        "[1]",
//                        "1",
//                        true
//                ),
//
//                new TestData(
//                        "[-4560]",
//                        "-654",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // 18. FACTORIAL
//    // ============================================================
//
//    private Problem factorial(
//            LocalDateTime now
//    ) {
//
//        Problem problem = problem(
//
//                "Factorial of a Number",
//
//                "Given a non-negative integer n, return n factorial. "
//                        + "The factorial of n is the product of all "
//                        + "positive integers less than or equal to n.",
//
//                Difficulty.EASY,
//
//                "0 <= n <= 20",
//
//                "5",
//
//                "Return n! as a long.",
//
//                "public class Solution {\n\n"
//                        + "    public long factorial(int n) {\n\n"
//                        + "        // Write your solution here\n\n"
//                        + "        return 0L;\n"
//                        + "    }\n"
//                        + "}",
//
//                now
//        );
//
//        example(
//                problem,
//                1,
//                "[5]",
//                "120",
//                "5! = 5 * 4 * 3 * 2 * 1 = 120."
//        );
//
//        example(
//                problem,
//                2,
//                "[0]",
//                "1",
//                "0! is defined as 1."
//        );
//
//        config(
//                problem,
//                "factorial",
//                "int",
//                "long",
//                EvaluationStrategy.RETURN_VALUE
//        );
//
//        tests(
//                problem,
//
//                new TestData(
//                        "[5]",
//                        "120",
//                        false
//                ),
//
//                new TestData(
//                        "[0]",
//                        "1",
//                        false
//                ),
//
//                new TestData(
//                        "[1]",
//                        "1",
//                        false
//                ),
//
//                new TestData(
//                        "[10]",
//                        "3628800",
//                        true
//                ),
//
//                new TestData(
//                        "[15]",
//                        "1307674368000",
//                        true
//                ),
//
//                new TestData(
//                        "[20]",
//                        "2432902008176640000",
//                        true
//                )
//        );
//
//        return problem;
//    }
//
//    // ============================================================
//    // TEST DATA
//    // ============================================================
//
//    private record TestData(
//            String input,
//            String output,
//            boolean hidden
//    ) {
//    }
//}