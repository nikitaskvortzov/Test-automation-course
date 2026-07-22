package Homework;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

public class Homework2Test {

    private static final Random RANDOM = new Random();

    @BeforeAll
    public static void beforeAllTests() {
        System.out.println("========================");
        System.out.println("Test method start");
    }

    @AfterEach
    public void afterEachTest() {
        System.out.println("Test method end");
        System.out.println("========================");
    }

    // 1) 4 теста с аннотацией @Test

    @Test
    @DisplayName("Test isEven with a random number 1..100 (Test)")
    void testIsEvenSingle() {
        int n = RANDOM.nextInt(100) + 1; // 1..100
        boolean result = Homework1.isEven(n);
        System.out.println("Input: " + n + " Output: " + result);
        if (n % 2 == 0) {
            System.out.println("TEST PASSED: isEven(" + n + ")");
        } else {
            System.out.println("TEST FAILED: isEven(" + n + ")");
        }
    }

    @Test
    @DisplayName("Test checkAccess with 20 random ages 0..99 (Test)")
    void testCheckAccessSingle() {
        boolean allPassed = true;
        for (int i = 0; i < 20; i++) {
            int age = RANDOM.nextInt(100); // 0..99
            String access = Homework1.checkAccess(age);
            System.out.println("Input age: " + age + " -> " + access);
            boolean pass = (age > 18 && "Allowed".equals(access)) || (age <= 18 && "Denied".equals(access));
            if (!pass) {
                allPassed = false;
            }
        }
        if (allPassed) {
            System.out.println("TEST PASSED: checkAccess 20 random ages");
        } else {
            System.out.println("TEST FAILED: checkAccess 20 random ages");
        }
    }

    @Test
    @DisplayName("Test getGrade with single random score (Test)")
    void testGetGradeSingle() {
        int score = RANDOM.nextInt(101);
        String grade = Homework1.getGrade(score);
        boolean pass = score >= 0 && score <= 100 && grade != null;
        System.out.println("Score: " + score + " -> Grade: " + grade);
        if (score < 0 || score > 100) {
            System.out.println("TEST FAILED: getGrade(" + score + ")");
        } else {
            System.out.println("TEST PASSED: getGrade(" + score + ") = " + grade);
        }
    }

    @Test
    @DisplayName("Test getGrade boundary (Test)")
    void testGetGradeBoundarySingle() {
        int score = 50;
        String grade = Homework1.getGrade(score);
        boolean pass = "C".equals(grade);
        System.out.println("Score: " + score + " -> Grade: " + grade);
        if (pass) {
            System.out.println("TEST PASSED: getGrade(" + score + ") boundary");
        } else {
            System.out.println("TEST FAILED: getGrade(" + score + ") boundary");
        }
    }

    // 2) 4 теста с @RepeatedTest

    @RepeatedTest(2)
    @DisplayName("RepeatedTest: isEven random (repeat 2) (Test)")
    void repeatedTestIsEven() {
        int n = RANDOM.nextInt(100) + 1;
        boolean result = Homework1.isEven(n);
        System.out.println("Input: " + n + " Output: " + result);
        if (n % 2 == 0) {
            System.out.println("TEST PASSED: isEven(" + n + ")");
        } else {
            System.out.println("TEST FAILED: isEven(" + n + ")");
        }
    }

    @RepeatedTest(2)
    @DisplayName("RepeatedTest: checkAccess random ages (repeat 2) (Test)")
    void repeatedTestCheckAccess() {
        int age = RANDOM.nextInt(100);
        String access = Homework1.checkAccess(age);
        boolean pass = ("Allowed".equals(access) && age > 18) || ("Denied".equals(access) && age <= 18);
        System.out.println("Input age: " + age + " -> " + access);
        if (pass) {
            System.out.println("TEST PASSED: checkAccess(" + age + ")");
        } else {
            System.out.println("TEST FAILED: checkAccess(" + age + ")");
        }
    }

    @RepeatedTest(2)
    @DisplayName("RepeatedTest: getGrade random score (repeat 2) (Test)")
    void repeatedTestGetGrade() {
        int score = RANDOM.nextInt(101);
        String grade = Homework1.getGrade(score);
        boolean pass = score >= 0 && score <= 100 && grade != null;
        System.out.println("Score: " + score + " -> Grade: " + grade);
        if (pass) {
            System.out.println("TEST PASSED: getGrade(" + score + ") = " + grade);
        } else {
            System.out.println("TEST FAILED: getGrade(" + score + ")");
        }
    }

    @RepeatedTest(2)
    @DisplayName("RepeatedTest: getGrade boundary check (repeat 2) (Test)")
    void repeatedTestGetGradeBoundary() {
        int score = 81;
        String grade = Homework1.getGrade(score);
        boolean pass = "A".equals(grade);
        System.out.println("Score: " + score + " -> Grade: " + grade);
        if (pass) {
            System.out.println("TEST PASSED: getGrade(" + score + ") boundary");
        } else {
            System.out.println("TEST FAILED: getGrade(" + score + ") boundary");
        }
    }

    // 3) 4 теста с @ParameterizedTest

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("ParameterizedTest: getGrade with random scores 0..100 (Test)")
    void parameterizedTestGetGrade(int score) {
        String grade = Homework1.getGrade(score);
        boolean pass = score >= 0 && score <= 100 && grade != null;
        System.out.println("Score: " + score + " -> Grade: " + grade);
        if (pass) {
            System.out.println("TEST PASSED: getGrade(" + score + ") = " + grade);
        } else {
            System.out.println("TEST FAILED: getGrade(" + score + ")");
        }
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("ParameterizedTest: isEven with scores (Test)")
    void parameterizedTestIsEven(int score) {
        boolean res = Homework1.isEven(score);
        System.out.println("Score: " + score + " -> isEven: " + res);
        System.out.println("TEST PASSED: isEven(" + score + ") == " + res);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("ParameterizedTest: checkAccess with scores (Test)")
    void parameterizedTestCheckAccess(int score) {
        String access = Homework1.checkAccess(score);
        boolean pass = "Allowed".equals(access) || "Denied".equals(access);
        System.out.println("Score: " + score + " -> Access: " + access);
        if (pass) {
            System.out.println("TEST PASSED: checkAccess(" + score + ") -> " + access);
        } else {
            System.out.println("TEST FAILED: checkAccess(" + score + ")");
        }
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("ParameterizedTest: another getGrade (Test)")
    void parameterizedTestGetGradeAnother(int score) {
        String grade = Homework1.getGrade(score);
        boolean pass = score >= 0 && score <= 100 && grade != null;
        System.out.println("Score: " + score + " -> Grade: " + grade);
        if (pass) {
            System.out.println("TEST PASSED: getGrade(" + score + ") -> " + grade);
        } else {
            System.out.println("TEST FAILED: getGrade(" + score + ")");
        }
    }

    static Stream<Integer> provideScores() {
        Random rnd = new Random();
        return Stream.generate(() -> rnd.nextInt(101)).limit(10);
    }
}
