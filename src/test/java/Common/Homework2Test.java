package Common;

import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Random;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("smoke")

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
