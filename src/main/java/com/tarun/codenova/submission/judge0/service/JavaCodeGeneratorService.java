package com.tarun.codenova.submission.judge0.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tarun.codenova.problem.entity.ProblemExecutionConfig;
import com.tarun.codenova.problem.entity.ProblemTestCase;
import com.tarun.codenova.problem.enums.EvaluationStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JavaCodeGeneratorService {

    private final ObjectMapper objectMapper = new ObjectMapper();


    // ============================================================
    // GENERATE COMPLETE JAVA PROGRAM
    // ============================================================

    public String generate(
            String userCode,
            ProblemExecutionConfig config,
            ProblemTestCase testCase) {

        try {

            if (userCode == null || userCode.isBlank()) {
                throw new IllegalArgumentException(
                        "Solution code cannot be empty"
                );
            }

            if (config == null) {
                throw new IllegalArgumentException(
                        "Problem execution config cannot be null"
                );
            }

            if (testCase == null) {
                throw new IllegalArgumentException(
                        "Test case cannot be null"
                );
            }


            // ========================================================
            // PARSE TEST CASE INPUT
            // ========================================================

            JsonNode input =
                    objectMapper.readTree(
                            testCase.getInput()
                    );

            if (!input.isArray()) {

                throw new IllegalArgumentException(
                        "Test case input must be a JSON array"
                );
            }


            // ========================================================
            // PARSE PARAMETER TYPES
            // ========================================================

            List<String> parameterTypes =
                    parseParameterTypes(
                            config.getParameterTypes()
                    );


            if (input.size() != parameterTypes.size()) {

                throw new IllegalArgumentException(
                        "Number of inputs (" +
                                input.size() +
                                ") does not match number of parameters (" +
                                parameterTypes.size() +
                                ")"
                );
            }


            // ========================================================
            // NORMALIZE USER CODE
            // ========================================================

            String normalizedUserCode =
                    normalizeSolutionClass(
                            userCode
                    );


            StringBuilder code =
                    new StringBuilder();


            /*
             * User solution
             */
            code.append(
                    normalizedUserCode
            );

            code.append(
                    "\n\n"
            );


            // ========================================================
            // MAIN CLASS
            // ========================================================

            code.append(
                    "public class Main {\n\n"
            );

            code.append(
                    "    public static void main(String[] args) {\n\n"
            );

            code.append(
                    "        Solution solution = new Solution();\n\n"
            );


            // ========================================================
            // CREATE ARGUMENTS
            // ========================================================

            List<String> argumentNames =
                    new ArrayList<>();


            for (
                    int i = 0;
                    i < parameterTypes.size();
                    i++
            ) {

                String parameterType =
                        parameterTypes.get(i);

                String argumentName =
                        "arg" + i;

                argumentNames.add(
                        argumentName
                );


                String javaValue =
                        convertJsonToJava(
                                input.get(i),
                                parameterType
                        );


                code.append(
                        "        "
                );

                code.append(
                        parameterType
                );

                code.append(
                        " "
                );

                code.append(
                        argumentName
                );

                code.append(
                        " = "
                );

                code.append(
                        javaValue
                );

                code.append(
                        ";\n"
                );
            }


            code.append(
                    "\n"
            );


            // ========================================================
            // METHOD CALL
            // ========================================================

            String arguments =
                    String.join(
                            ", ",
                            argumentNames
                    );


            String methodCall =
                    "solution."
                            + config.getMethodName()
                            + "("
                            + arguments
                            + ")";


            // ========================================================
            // EVALUATION STRATEGY
            // ========================================================

            EvaluationStrategy strategy =
                    config.getEvaluationStrategy();


            if (strategy == null) {

                strategy =
                        EvaluationStrategy.RETURN_VALUE;
            }


            switch (strategy) {

                case RETURN_VALUE ->

                        generateReturnValueOutput(
                                code,
                                methodCall,
                                config
                        );


                case MUTATED_PREFIX ->

                        generateMutatedPrefixOutput(
                                code,
                                methodCall,
                                config,
                                argumentNames
                        );


                case MUTATED_ARRAY ->

                        generateMutatedArrayOutput(
                                code,
                                methodCall,
                                config,
                                argumentNames
                        );


                default -> throw new IllegalArgumentException(
                        "Unsupported evaluation strategy: "
                                + strategy
                );
            }


            // ========================================================
            // CLOSE MAIN CLASS
            // ========================================================

            code.append(
                    "\n"
            );

            code.append(
                    "    }\n"
            );

            code.append(
                    "}\n"
            );


            return code.toString();


        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Failed to generate Java execution code: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // ============================================================
    // RETURN VALUE
    // ============================================================

    private void generateReturnValueOutput(
            StringBuilder code,
            String methodCall,
            ProblemExecutionConfig config) {

        String returnType =
                config.getReturnType();


        // --------------------------------------------------------
        // VOID
        // --------------------------------------------------------

        if ("void".equals(returnType)) {

            code.append(
                    "        "
            );

            code.append(
                    methodCall
            );

            code.append(
                    ";\n"
            );

            return;
        }


        // --------------------------------------------------------
        // CALL METHOD
        // --------------------------------------------------------

        code.append(
                "        "
        );

        code.append(
                returnType
        );

        code.append(
                " result = "
        );

        code.append(
                methodCall
        );

        code.append(
                ";\n\n"
        );


        // --------------------------------------------------------
        // ARRAY RETURN VALUE
        // --------------------------------------------------------

        if (isArrayType(returnType)) {

            code.append(
                    "        System.out.println("
            );


            if (returnType.endsWith("[][]")) {

                code.append(
                        "java.util.Arrays.deepToString(result)"
                );

            } else {

                code.append(
                        "java.util.Arrays.toString(result)"
                );
            }


            code.append(
                    ");\n"
            );

            return;
        }


        // --------------------------------------------------------
        // NORMAL RETURN VALUE
        // --------------------------------------------------------

        code.append(
                "        System.out.println(result);\n"
        );
    }


    // ============================================================
    // MUTATED PREFIX
    // ============================================================

    private void generateMutatedPrefixOutput(
            StringBuilder code,
            String methodCall,
            ProblemExecutionConfig config,
            List<String> argumentNames) {

        /*
         * Example:
         *
         * int removeDuplicates(int[] nums)
         *
         * If result = 2:
         *
         * nums = [1, 1, 2]
         *
         * and after modification:
         *
         * nums = [1, 2, 2]
         *
         * only:
         *
         * [1, 2]
         *
         * is considered.
         */


        if (!"int".equals(config.getReturnType())) {

            throw new IllegalArgumentException(
                    "MUTATED_PREFIX requires returnType=int"
            );
        }


        if (argumentNames.isEmpty()) {

            throw new IllegalArgumentException(
                    "MUTATED_PREFIX requires at least one argument"
            );
        }


        String arrayArgument =
                argumentNames.get(0);


        // --------------------------------------------------------
        // METHOD CALL
        // --------------------------------------------------------

        code.append(
                "        int result = "
        );

        code.append(
                methodCall
        );

        code.append(
                ";\n\n"
        );


        // --------------------------------------------------------
        // PRINT K
        // --------------------------------------------------------

        code.append(
                "        System.out.println(result);\n"
        );


        // --------------------------------------------------------
        // PRINT FIRST K ELEMENTS
        // --------------------------------------------------------

        code.append(
                "        System.out.println("
        );

        code.append(
                "java.util.Arrays.toString("
        );

        code.append(
                "java.util.Arrays.copyOf("
        );

        code.append(
                arrayArgument
        );

        code.append(
                ", result"
        );

        code.append(
                ")"
        );

        code.append(
                ")"
        );

        code.append(
                ");\n"
        );
    }


    // ============================================================
    // MUTATED ARRAY
    // ============================================================

    private void generateMutatedArrayOutput(
            StringBuilder code,
            String methodCall,
            ProblemExecutionConfig config,
            List<String> argumentNames) {

        /*
         * Example:
         *
         * void sortColors(int[] nums)
         *
         * The method modifies nums directly.
         *
         * Therefore:
         *
         * 1. Execute method
         * 2. Print the complete modified array
         */


        if (argumentNames.isEmpty()) {

            throw new IllegalArgumentException(
                    "MUTATED_ARRAY requires at least one argument"
            );
        }


        String arrayArgument =
                argumentNames.get(0);


        // --------------------------------------------------------
        // METHOD CALL
        // --------------------------------------------------------

        code.append(
                "        "
        );

        code.append(
                methodCall
        );

        code.append(
                ";\n\n"
        );


        // --------------------------------------------------------
        // DETERMINE ARRAY TYPE
        // --------------------------------------------------------

        String parameterType =
                parseParameterTypes(
                        config.getParameterTypes()
                ).get(0);


        // --------------------------------------------------------
        // PRINT 2D ARRAY
        // --------------------------------------------------------

        if (parameterType.endsWith("[][]")) {

            code.append(
                    "        System.out.println("
            );

            code.append(
                    "java.util.Arrays.deepToString("
            );

            code.append(
                    arrayArgument
            );

            code.append(
                    ")"
            );

            code.append(
                    ");\n"
            );

            return;
        }


        // --------------------------------------------------------
        // PRINT 1D ARRAY
        // --------------------------------------------------------

        if (parameterType.endsWith("[]")) {

            code.append(
                    "        System.out.println("
            );

            code.append(
                    "java.util.Arrays.toString("
            );

            code.append(
                    arrayArgument
            );

            code.append(
                    ")"
            );

            code.append(
                    ");\n"
            );

            return;
        }


        throw new IllegalArgumentException(
                "MUTATED_ARRAY requires an array parameter"
        );
    }


    // ============================================================
    // NORMALIZE SOLUTION CLASS
    // ============================================================

    private String normalizeSolutionClass(
            String userCode) {

        /*
         * Judge0 receives generated source as Main.java.
         *
         * Therefore:
         *
         * public class Solution
         *
         * would cause:
         *
         * class Solution is public,
         * should be declared in Solution.java
         *
         * We make Solution package-private.
         */

        return userCode.replaceFirst(
                "(?m)\\bpublic\\s+class\\s+Solution\\b",
                "class Solution"
        );
    }


    // ============================================================
    // PARSE PARAMETER TYPES
    // ============================================================

    private List<String> parseParameterTypes(
            String parameterTypes) {

        List<String> result =
                new ArrayList<>();


        if (
                parameterTypes == null ||
                        parameterTypes.isBlank()
        ) {

            return result;
        }


        String[] types =
                parameterTypes.split(",");


        for (String type : types) {

            if (!type.isBlank()) {

                result.add(
                        type.trim()
                );
            }
        }


        return result;
    }


    // ============================================================
    // JSON → JAVA
    // ============================================================

    private String convertJsonToJava(
            JsonNode node,
            String parameterType) {

        if (node == null || node.isNull()) {

            throw new IllegalArgumentException(
                    "Input value cannot be null for type: "
                            + parameterType
            );
        }


        // --------------------------------------------------------
        // INT
        // --------------------------------------------------------

        if ("int".equals(parameterType)) {

            if (!node.isNumber()) {

                throw new IllegalArgumentException(
                        "Expected number for int parameter"
                );
            }

            return node.asText();
        }


        // --------------------------------------------------------
        // LONG
        // --------------------------------------------------------

        if ("long".equals(parameterType)) {

            if (!node.isNumber()) {

                throw new IllegalArgumentException(
                        "Expected number for long parameter"
                );
            }

            return node.asLong() + "L";
        }


        // --------------------------------------------------------
        // DOUBLE
        // --------------------------------------------------------

        if ("double".equals(parameterType)) {

            if (!node.isNumber()) {

                throw new IllegalArgumentException(
                        "Expected number for double parameter"
                );
            }

            return node.asDouble() + "d";
        }


        // --------------------------------------------------------
        // FLOAT
        // --------------------------------------------------------

        if ("float".equals(parameterType)) {

            if (!node.isNumber()) {

                throw new IllegalArgumentException(
                        "Expected number for float parameter"
                );
            }

            return node.asDouble() + "f";
        }


        // --------------------------------------------------------
        // BOOLEAN
        // --------------------------------------------------------

        if ("boolean".equals(parameterType)) {

            if (!node.isBoolean()) {

                throw new IllegalArgumentException(
                        "Expected boolean parameter"
                );
            }

            return node.asBoolean()
                    ? "true"
                    : "false";
        }


        // --------------------------------------------------------
        // STRING
        // --------------------------------------------------------

        if ("String".equals(parameterType)) {

            if (!node.isTextual()) {

                throw new IllegalArgumentException(
                        "Expected string parameter"
                );
            }

            return "\""
                    + escapeJavaString(
                    node.asText()
            )
                    + "\"";
        }


        // --------------------------------------------------------
        // INT[]
        // --------------------------------------------------------

        if ("int[]".equals(parameterType)) {

            return convertIntArray(node);
        }


        // --------------------------------------------------------
        // LONG[]
        // --------------------------------------------------------

        if ("long[]".equals(parameterType)) {

            return convertLongArray(node);
        }


        // --------------------------------------------------------
        // DOUBLE[]
        // --------------------------------------------------------

        if ("double[]".equals(parameterType)) {

            return convertDoubleArray(node);
        }


        // --------------------------------------------------------
        // STRING[]
        // --------------------------------------------------------

        if ("String[]".equals(parameterType)) {

            return convertStringArray(node);
        }


        // --------------------------------------------------------
        // INT[][]
        // --------------------------------------------------------

        if ("int[][]".equals(parameterType)) {

            return convertInt2DArray(node);
        }


        // --------------------------------------------------------
        // LONG[][]
        // --------------------------------------------------------

        if ("long[][]".equals(parameterType)) {

            return convertLong2DArray(node);
        }


        // --------------------------------------------------------
        // DOUBLE[][]
        // --------------------------------------------------------

        if ("double[][]".equals(parameterType)) {

            return convertDouble2DArray(node);
        }


        // --------------------------------------------------------
        // STRING[][]
        // --------------------------------------------------------

        if ("String[][]".equals(parameterType)) {

            return convertString2DArray(node);
        }


        throw new IllegalArgumentException(
                "Unsupported parameter type: "
                        + parameterType
        );
    }


    // ============================================================
    // INT[]
    // ============================================================

    private String convertIntArray(
            JsonNode node) {

        validateArray(
                node,
                "int[]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            JsonNode value =
                    node.get(i);


            if (!value.isNumber()) {

                throw new IllegalArgumentException(
                        "Invalid int[] element"
                );
            }


            result.append(
                    value.asInt()
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // LONG[]
    // ============================================================

    private String convertLongArray(
            JsonNode node) {

        validateArray(
                node,
                "long[]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            JsonNode value =
                    node.get(i);


            if (!value.isNumber()) {

                throw new IllegalArgumentException(
                        "Invalid long[] element"
                );
            }


            result.append(
                    value.asLong()
            );

            result.append(
                    "L"
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // DOUBLE[]
    // ============================================================

    private String convertDoubleArray(
            JsonNode node) {

        validateArray(
                node,
                "double[]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            JsonNode value =
                    node.get(i);


            if (!value.isNumber()) {

                throw new IllegalArgumentException(
                        "Invalid double[] element"
                );
            }


            result.append(
                    value.asDouble()
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // STRING[]
    // ============================================================

    private String convertStringArray(
            JsonNode node) {

        validateArray(
                node,
                "String[]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            JsonNode value =
                    node.get(i);


            if (!value.isTextual()) {

                throw new IllegalArgumentException(
                        "Invalid String[] element"
                );
            }


            result.append(
                    "\""
            );

            result.append(
                    escapeJavaString(
                            value.asText()
                    )
            );

            result.append(
                    "\""
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // INT[][]
    // ============================================================

    private String convertInt2DArray(
            JsonNode node) {

        validateArray(
                node,
                "int[][]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            result.append(
                    convertIntArray(
                            node.get(i)
                    )
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // LONG[][]
    // ============================================================

    private String convertLong2DArray(
            JsonNode node) {

        validateArray(
                node,
                "long[][]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            result.append(
                    convertLongArray(
                            node.get(i)
                    )
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // DOUBLE[][]
    // ============================================================

    private String convertDouble2DArray(
            JsonNode node) {

        validateArray(
                node,
                "double[][]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            result.append(
                    convertDoubleArray(
                            node.get(i)
                    )
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // STRING[][]
    // ============================================================

    private String convertString2DArray(
            JsonNode node) {

        validateArray(
                node,
                "String[][]"
        );


        StringBuilder result =
                new StringBuilder("{");


        for (
                int i = 0;
                i < node.size();
                i++
        ) {

            if (i > 0) {
                result.append(", ");
            }


            result.append(
                    convertStringArray(
                            node.get(i)
                    )
            );
        }


        result.append("}");


        return result.toString();
    }


    // ============================================================
    // ARRAY VALIDATION
    // ============================================================

    private void validateArray(
            JsonNode node,
            String parameterType) {

        if (!node.isArray()) {

            throw new IllegalArgumentException(
                    "Expected JSON array for parameter type: "
                            + parameterType
            );
        }
    }


    // ============================================================
    // ARRAY TYPE CHECK
    // ============================================================

    private boolean isArrayType(
            String type) {

        return type != null &&
                type.endsWith("[]");
    }


    // ============================================================
    // JAVA STRING ESCAPING
    // ============================================================

    private String escapeJavaString(
            String value) {

        if (value == null) {
            return "";
        }


        return value
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                )
                .replace(
                        "\n",
                        "\\n"
                )
                .replace(
                        "\r",
                        "\\r"
                )
                .replace(
                        "\t",
                        "\\t"
                );
    }
}