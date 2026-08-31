package com.tarun.codenova.problem.enums;

/**
 * Defines how the CodeNova judge evaluates a submitted solution.
 *
 * The strategy is configured per problem through
 * ProblemExecutionConfig.
 */
public enum EvaluationStrategy {

    /**
     * The solution returns the answer directly.
     *
     * Examples:
     *
     * int maxSubArray(int[] nums)
     * int add(int a, int b)
     * boolean isPalindrome(int x)
     * String toLowerCase(String s)
     * int[] twoSum(int[] nums, int target)
     * int[][] someProblem(...)
     *
     * The generated Main class prints the returned value
     * and the judge compares it with expectedOutput.
     */
    RETURN_VALUE,


    /**
     * The solution modifies an input array and returns the
     * number of valid elements in that array.
     *
     * Example:
     *
     * int removeDuplicates(int[] nums)
     *
     * If the method returns:
     *
     * 3
     *
     * then only:
     *
     * nums[0]
     * nums[1]
     * nums[2]
     *
     * are considered part of the answer.
     *
     * The generated Main class prints:
     *
     * 3
     * [valid prefix]
     */
    MUTATED_PREFIX,


    /**
     * The solution modifies an input array in-place and
     * does not return the answer.
     *
     * Example:
     *
     * void sortColors(int[] nums)
     * void moveZeroes(int[] nums)
     *
     * The generated Main class executes the method and
     * then prints the complete mutated array.
     */
    MUTATED_ARRAY
}