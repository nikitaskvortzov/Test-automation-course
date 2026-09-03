package Homework;

import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Random;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework2Test {

    private static final Random RANDOM = new Random();

    @BeforeEach
    @Step("Подготовить окружение перед тестом")
    public void beforeEachTests() {
        System.out.println("========================");
        System.out.println("Test method start");
    }

    @AfterEach
    @Step("Завершить тест")
    public void afterEachTest() {
        System.out.println("Test method end");
        System.out.println("========================");
    }

    @Test
    @DisplayName("Проверить isEven для случайного числа")
    void testIsEvenSingle() {
        int number = RANDOM.nextInt(100) + 1;

        boolean actualResult = checkIsEven(number);

        checkIsEvenResult(number, actualResult);
    }

    @Test
    @DisplayName("Проверить checkAccess для 20 случайных возрастов")
    void testCheckAccessSingle() {
        for (int i = 0; i < 20; i++) {
            int age = RANDOM.nextInt(100);

            String actualAccess = checkAccess(age);

            checkAccessResult(age, actualAccess);
        }
    }

    @Test
    @DisplayName("Проверить getGrade для случайного результата")
    void testGetGradeSingle() {
        int score = RANDOM.nextInt(101);

        String actualGrade = getGrade(score);

        checkGradeIsValid(score, actualGrade);
    }

    @Test
    @DisplayName("Проверить граничное значение getGrade")
    void testGetGradeBoundarySingle() {
        int score = 50;

        String actualGrade = getGrade(score);

        checkGrade(score, "C", actualGrade);
    }

    @RepeatedTest(2)
    @DisplayName("Повторно проверить isEven для случайного числа")
    void repeatedTestIsEven() {
        int number = RANDOM.nextInt(100) + 1;

        boolean actualResult = checkIsEven(number);

        checkIsEvenResult(number, actualResult);
    }

    @RepeatedTest(2)
    @DisplayName("Повторно проверить checkAccess для случайного возраста")
    void repeatedTestCheckAccess() {
        int age = RANDOM.nextInt(100);

        String actualAccess = checkAccess(age);

        checkAccessResult(age, actualAccess);
    }

    @RepeatedTest(2)
    @DisplayName("Повторно проверить getGrade для случайного результата")
    void repeatedTestGetGrade() {
        int score = RANDOM.nextInt(101);

        String actualGrade = getGrade(score);

        checkGradeIsValid(score, actualGrade);
    }

    @RepeatedTest(2)
    @DisplayName("Повторно проверить граничное значение getGrade")
    void repeatedTestGetGradeBoundary() {
        int score = 81;

        String actualGrade = getGrade(score);

        checkGrade(score, "A", actualGrade);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Проверить getGrade для набора результатов")
    void parameterizedTestGetGrade(int score) {
        String actualGrade = getGrade(score);

        checkGradeIsValid(score, actualGrade);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Проверить isEven для набора чисел")
    void parameterizedTestIsEven(int score) {
        boolean actualResult = checkIsEven(score);

        checkIsEvenResult(score, actualResult);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Проверить checkAccess для набора возрастов")
    void parameterizedTestCheckAccess(int age) {
        String actualAccess = checkAccess(age);

        checkAccessResult(age, actualAccess);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Повторно проверить getGrade для набора результатов")
    void parameterizedTestGetGradeAnother(int score) {
        String actualGrade = getGrade(score);

        checkGradeIsValid(score, actualGrade);
    }

    @Step("Получить результат проверки четности числа: {number}")
    private boolean checkIsEven(int number) {
        return Homework1.isEven(number);
    }

    @Step("Получить уровень доступа для возраста: {age}")
    private String checkAccess(int age) {
        return Homework1.checkAccess(age);
    }

    @Step("Получить оценку для результата: {score}")
    private String getGrade(int score) {
        return Homework1.getGrade(score);
    }

    @Step("Проверить результат isEven для числа: {number}")
    private void checkIsEvenResult(int number, boolean actualResult) {
        boolean expectedResult = number % 2 == 0;

        assertEquals(
                expectedResult,
                actualResult,
                "Неверный результат метода isEven для числа: " + number
        );
    }

    @Step("Проверить результат checkAccess для возраста: {age}")
    private void checkAccessResult(int age, String actualAccess) {
        String expectedAccess = age > 18 ? "Allowed" : "Denied";

        assertEquals(
                expectedAccess,
                actualAccess,
                "Неверный уровень доступа для возраста: " + age
        );
    }

    @Step("Проверить, что оценка для результата {score} корректна")
    private void checkGradeIsValid(int score, String actualGrade) {
        assertTrue(
                score >= 0 && score <= 100,
                "Результат должен находиться в диапазоне от 0 до 100"
        );

        assertNotNull(
                actualGrade,
                "Оценка не должна быть null"
        );
    }

    @Step("Проверить оценку {expectedGrade} для результата: {score}")
    private void checkGrade(
            int score,
            String expectedGrade,
            String actualGrade
    ) {
        assertEquals(
                expectedGrade,
                actualGrade,
                "Неверная оценка для результата: " + score
        );
    }

    @Step("Сформировать набор случайных результатов от 0 до 100")
    static Stream<Integer> provideScores() {
        return Stream.generate(() -> RANDOM.nextInt(101))
                .limit(10);
    }
}

//package Homework;
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import java.util.Random;
//import java.util.stream.Stream;
//
//public class Homework2Test {
//
//
//
//    private static final Random RANDOM = new Random();
//
//    @BeforeEach
//    public void beforeEachTests() {
//        System.out.println("========================");
//        System.out.println("Test method start");
//    }
//
//    @AfterEach
//    public void afterEachTest() {
//        System.out.println("Test method end");
//        System.out.println("========================");
//    }
//
//    // 1) 4 теста с аннотацией @Test
//
//    @Test
//    @DisplayName("Test isEven with a random number 1..100 (Test)")
//    void testIsEvenSingle() {
//        int n = RANDOM.nextInt(100) + 1; // 1..100
//        boolean result = Homework1.isEven(n);
//        System.out.println("Input: " + n + " Output: " + result);
//        if (n % 2 == 0) {
//            System.out.println("TEST PASSED: isEven(" + n + ")");
//        } else {
//            System.out.println("TEST FAILED: isEven(" + n + ")");
//        }
//    }
//
//    @Test
//    @DisplayName("Test checkAccess with 20 random ages 0..99 (Test)")
//    void testCheckAccessSingle() {
//        boolean allPassed = true;
//        for (int i = 0; i < 20; i++) {
//            int age = RANDOM.nextInt(100); // 0..99
//            String access = Homework1.checkAccess(age);
//            System.out.println("Input age: " + age + " -> " + access);
//            boolean pass = (age > 18 && "Allowed".equals(access)) || (age <= 18 && "Denied".equals(access));
//            if (!pass) {
//                allPassed = false;
//            }
//        }
//        if (allPassed) {
//            System.out.println("TEST PASSED: checkAccess 20 random ages");
//        } else {
//            System.out.println("TEST FAILED: checkAccess 20 random ages");
//        }
//    }
//
//    @Test
//    @DisplayName("Test getGrade with single random score (Test)")
//    void testGetGradeSingle() {
//        int score = RANDOM.nextInt(101);
//        String grade = Homework1.getGrade(score);
//        boolean pass = score >= 0 && score <= 100 && grade != null;
//        System.out.println("Score: " + score + " -> Grade: " + grade);
//        if (score < 0 || score > 100) {
//            System.out.println("TEST FAILED: getGrade(" + score + ")");
//        } else {
//            System.out.println("TEST PASSED: getGrade(" + score + ") = " + grade);
//        }
//    }
//
//    @Test
//    @DisplayName("Test getGrade boundary (Test)")
//    void testGetGradeBoundarySingle() {
//        int score = 50;
//        String grade = Homework1.getGrade(score);
//        boolean pass = "C".equals(grade);
//        System.out.println("Score: " + score + " -> Grade: " + grade);
//        if (pass) {
//            System.out.println("TEST PASSED: getGrade(" + score + ") boundary");
//        } else {
//            System.out.println("TEST FAILED: getGrade(" + score + ") boundary");
//        }
//    }
//
//    // 2) 4 теста с @RepeatedTest
//
//    @RepeatedTest(2)
//    @DisplayName("RepeatedTest: isEven random (repeat 2) (Test)")
//    void repeatedTestIsEven() {
//        int n = RANDOM.nextInt(100) + 1;
//        boolean result = Homework1.isEven(n);
//        System.out.println("Input: " + n + " Output: " + result);
//        if (n % 2 == 0) {
//            System.out.println("TEST PASSED: isEven(" + n + ")");
//        } else {
//            System.out.println("TEST FAILED: isEven(" + n + ")");
//        }
//    }
//
//    @RepeatedTest(2)
//    @DisplayName("RepeatedTest: checkAccess random ages (repeat 2) (Test)")
//    void repeatedTestCheckAccess() {
//        int age = RANDOM.nextInt(100);
//        String access = Homework1.checkAccess(age);
//        boolean pass = ("Allowed".equals(access) && age > 18) || ("Denied".equals(access) && age <= 18);
//        System.out.println("Input age: " + age + " -> " + access);
//        if (pass) {
//            System.out.println("TEST PASSED: checkAccess(" + age + ")");
//        } else {
//            System.out.println("TEST FAILED: checkAccess(" + age + ")");
//        }
//    }
//
//    @RepeatedTest(2)
//    @DisplayName("RepeatedTest: getGrade random score (repeat 2) (Test)")
//    void repeatedTestGetGrade() {
//        int score = RANDOM.nextInt(101);
//        String grade = Homework1.getGrade(score);
//        boolean pass = score >= 0 && score <= 100 && grade != null;
//        System.out.println("Score: " + score + " -> Grade: " + grade);
//        if (pass) {
//            System.out.println("TEST PASSED: getGrade(" + score + ") = " + grade);
//        } else {
//            System.out.println("TEST FAILED: getGrade(" + score + ")");
//        }
//    }
//
//    @RepeatedTest(2)
//    @DisplayName("RepeatedTest: getGrade boundary check (repeat 2) (Test)")
//    void repeatedTestGetGradeBoundary() {
//        int score = 81;
//        String grade = Homework1.getGrade(score);
//        boolean pass = "A".equals(grade);
//        System.out.println("Score: " + score + " -> Grade: " + grade);
//        if (pass) {
//            System.out.println("TEST PASSED: getGrade(" + score + ") boundary");
//        } else {
//            System.out.println("TEST FAILED: getGrade(" + score + ") boundary");
//        }
//    }
//
//    // 3) 4 теста с @ParameterizedTest
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: getGrade with random scores 0..100 (Test)")
//    void parameterizedTestGetGrade(int score) {
//        String grade = Homework1.getGrade(score);
//        boolean pass = score >= 0 && score <= 100 && grade != null;
//        System.out.println("Score: " + score + " -> Grade: " + grade);
//        if (pass) {
//            System.out.println("TEST PASSED: getGrade(" + score + ") = " + grade);
//        } else {
//            System.out.println("TEST FAILED: getGrade(" + score + ")");
//        }
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: isEven with scores (Test)")
//    void parameterizedTestIsEven(int score) {
//        boolean res = Homework1.isEven(score);
//        System.out.println("Score: " + score + " -> isEven: " + res);
//        System.out.println("TEST PASSED: isEven(" + score + ") == " + res);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: checkAccess with scores (Test)")
//    void parameterizedTestCheckAccess(int score) {
//        String access = Homework1.checkAccess(score);
//        boolean pass = "Allowed".equals(access) || "Denied".equals(access);
//        System.out.println("Score: " + score + " -> Access: " + access);
//        if (pass) {
//            System.out.println("TEST PASSED: checkAccess(" + score + ") -> " + access);
//        } else {
//            System.out.println("TEST FAILED: checkAccess(" + score + ")");
//        }
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: another getGrade (Test)")
//    void parameterizedTestGetGradeAnother(int score) {
//        String grade = Homework1.getGrade(score);
//        boolean pass = score >= 0 && score <= 100 && grade != null;
//        System.out.println("Score: " + score + " -> Grade: " + grade);
//        if (pass) {
//            System.out.println("TEST PASSED: getGrade(" + score + ") -> " + grade);
//        } else {
//            System.out.println("TEST FAILED: getGrade(" + score + ")");
//        }
//    }
//
//    static Stream<Integer> provideScores() {
//        Random rnd = new Random();
//        return Stream.generate(() -> rnd.nextInt(101)).limit(10);
//    }
//}
//
