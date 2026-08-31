//package com.tarun.codenova.submission.execution;
//
//
//import com.tarun.codenova.submission.entity.ExecutionResult;
//import com.tarun.codenova.submission.enums.ExecutionStatus;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardOpenOption;
//
//@Component
//public class JavaCodeExecutor {
//
//    private static final long TIME_LIMIT_SECONDS = 2;
//
//    public ExecutionResult execute(String sourceCode, String input) {
//
//        long startTime = System.currentTimeMillis();
//
//        Path tempDirectory = null;
//
//        try {
//            // Create temporary directory
//            tempDirectory = Files.createTempDirectory("codenova-");
//
//            // Create Main.java
//            Path javaFile = tempDirectory.resolve("Main.java");
//
//            Files.writeString(
//                    javaFile,
//                    sourceCode,
//                    StandardOpenOption.CREATE,
//                    StandardOpenOption.WRITE
//            );
//
//            // Compile
//            ProcessBuilder compileProcessBuilder =
//                    new ProcessBuilder(
//                            "javac",
//                            javaFile.getFileName().toString()
//                    );
//
//            compileProcessBuilder.directory(tempDirectory.toFile());
//
//            Process compileProcess = compileProcessBuilder.start();
//
//            String compileError =
//                    new String(compileProcess.getErrorStream().readAllBytes());
//
//            int compileExitCode = compileProcess.waitFor();
//
//            if (compileExitCode != 0) {
//
//                return ExecutionResult.builder()
//                        .status(ExecutionStatus.COMPILATION_ERROR)
//                        .output(null)
//                        .error(compileError)
//                        .executionTime(
//                                System.currentTimeMillis() - startTime
//                        )
//                        .exitCode(compileExitCode)
//                        .build();
//            }
//
//            // Execute
//            ProcessBuilder runProcessBuilder =
//                    new ProcessBuilder(
//                            "java",
//                            "-cp",
//                            tempDirectory.toString(),
//                            "Main"
//                    );
//
//            runProcessBuilder.directory(tempDirectory.toFile());
//
//            Process runProcess = runProcessBuilder.start();
//
//            // Send input
//            if (input != null) {
//                runProcess.getOutputStream().write(
//                        input.getBytes()
//                );
//            }
//
//            runProcess.getOutputStream().close();
//
//            // Wait with timeout
//            boolean finished =
//                    runProcess.waitFor(
//                            TIME_LIMIT_SECONDS,
//                            java.util.concurrent.TimeUnit.SECONDS
//                    );
//
//            if (!finished) {
//
//                runProcess.destroyForcibly();
//
//                return ExecutionResult.builder()
//                        .status(ExecutionStatus.TIME_LIMIT_EXCEEDED)
//                        .output(null)
//                        .error("Execution time limit exceeded")
//                        .executionTime(
//                                System.currentTimeMillis() - startTime
//                        )
//                        .exitCode(-1)
//                        .build();
//            }
//
//            String output =
//                    new String(runProcess.getInputStream().readAllBytes());
//
//            String error =
//                    new String(runProcess.getErrorStream().readAllBytes());
//
//            int exitCode = runProcess.exitValue();
//
//            if (exitCode != 0) {
//
//                return ExecutionResult.builder()
//                        .status(ExecutionStatus.RUNTIME_ERROR)
//                        .output(output)
//                        .error(error)
//                        .executionTime(
//                                System.currentTimeMillis() - startTime
//                        )
//                        .exitCode(exitCode)
//                        .build();
//            }
//
//            return ExecutionResult.builder()
//                    .status(ExecutionStatus.SUCCESS)
//                    .output(output)
//                    .error(null)
//                    .executionTime(
//                            System.currentTimeMillis() - startTime
//                    )
//                    .exitCode(0)
//                    .build();
//
//        } catch (Exception e) {
//
//            return ExecutionResult.builder()
//                    .status(ExecutionStatus.RUNTIME_ERROR)
//                    .output(null)
//                    .error(e.getMessage())
//                    .executionTime(
//                            System.currentTimeMillis() - startTime
//                    )
//                    .exitCode(-1)
//                    .build();
//
//        } finally {
//
//            // Delete temporary files
//            if (tempDirectory != null) {
//                try {
//                    Files.walk(tempDirectory)
//                            .sorted(java.util.Comparator.reverseOrder())
//                            .forEach(path -> {
//                                try {
//                                    Files.deleteIfExists(path);
//                                } catch (IOException ignored) {
//                                }
//                            });
//                } catch (IOException ignored) {
//                }
//            }
//        }
//    }
//}