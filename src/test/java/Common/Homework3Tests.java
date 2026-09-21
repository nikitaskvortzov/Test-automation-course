package Common;

import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Tag("smoke")

public class Homework3Tests {

    private final Homework3 service = new Homework3();

    private static final Random RANDOM = new Random(123);

    @RepeatedTest(10)
    @DisplayName("Проверить, что возраст 20 лет считается взрослым")
    void testIsAdult() {
        boolean actualResult = isAdult(20);

        checkIsAdultResult(actualResult);
    }

    @RepeatedTest(10)
    @DisplayName("Проверить, что список имен не пустой")
    void testGetNamesNotEmpty() {
        List<String> names = getNames();

        checkNamesList(names);
    }

    @RepeatedTest(10)
    @DisplayName("Проверить значение метода value")
    void testValueShouldBe8ButIs7() {
        int actualValue = getValue();

        checkValue(actualValue, 8);
    }

    @RepeatedTest(10)
    @DisplayName("Проверить, что объект активен")
    void testIsActive() {
        boolean actualResult = isActive();

        checkIsActiveResult(actualResult);
    }

    @Test
    @DisplayName("Проверить isEven для случайного числа")
    void testIsEvenSingle() {
        int number = RANDOM.nextInt(100) + 1;

        boolean actualResult = isEven(number);

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
    @DisplayName("Проверить граничное значение getGrade: 50")
    void testGetGradeBoundarySingle() {
        int score = 50;

        String actualGrade = getGrade(score);

        checkGrade(score, "C", actualGrade);
    }

    @RepeatedTest(10)
    @DisplayName("Повторно проверить isEven")
    void repeatedTestIsEven() {
        int number = RANDOM.nextInt(100) + 1;

        boolean actualResult = isEven(number);

        checkIsEvenResult(number, actualResult);
    }

    @RepeatedTest(10)
    @DisplayName("Повторно проверить checkAccess")
    void repeatedTestCheckAccess() {
        int age = RANDOM.nextInt(100);

        String actualAccess = checkAccess(age);

        checkAccessResult(age, actualAccess);
    }

    @RepeatedTest(10)
    @DisplayName("Повторно проверить getGrade")
    void repeatedTestGetGrade() {
        int score = RANDOM.nextInt(101);

        String actualGrade = getGrade(score);

        checkGradeIsValid(score, actualGrade);
    }

    @RepeatedTest(10)
    @DisplayName("Повторно проверить граничное значение getGrade: 81")
    void repeatedTestGetGradeBoundary() {
        int score = 81;

        String actualGrade = getGrade(score);

        checkGrade(score, "A", actualGrade);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Проверить getGrade на наборе значений")
    void parameterizedTestGetGrade(int score) {
        String actualGrade = getGrade(score);

        checkGradeIsValid(score, actualGrade);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Проверить isEven на наборе чисел")
    void parameterizedTestIsEven(int number) {
        boolean actualResult = isEven(number);

        checkIsEvenResult(number, actualResult);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Проверить checkAccess на наборе возрастов")
    void parameterizedTestCheckAccess(int age) {
        String actualAccess = checkAccess(age);

        checkAccessResult(age, actualAccess);
    }

    @ParameterizedTest
    @MethodSource("provideScores")
    @DisplayName("Повторно проверить getGrade на наборе значений")
    void parameterizedTestGetGradeAnother(int score) {
        String actualGrade = getGrade(score);

        checkGradeIsValid(score, actualGrade);
    }

    @Step("Проверить, является ли человек совершеннолетним: возраст {age}")
    private boolean isAdult(int age) {
        return service.isAdult(age);
    }

    @Step("Получить список имен")
    private List<String> getNames() {
        return service.getNames();
    }

    @Step("Получить значение метода value")
    private int getValue() {
        return service.value();
    }

    @Step("Проверить активность сервиса")
    private boolean isActive() {
        return service.isActive();
    }

    @Step("Проверить четность числа: {number}")
    private boolean isEven(int number) {
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

    @Step("Проверить результат isAdult")
    private void checkIsAdultResult(boolean actualResult) {
        Assertions.assertTrue(
                actualResult,
                "Для возраста 20 лет ожидалось значение true"
        );
    }

    @Step("Проверить список имен")
    private void checkNamesList(List<String> names) {
        Assertions.assertNotNull(
                names,
                "Список имен не должен быть null"
        );

        Assertions.assertFalse(
                names.isEmpty(),
                "Список имен не должен быть пустым"
        );
    }

    @Step("Проверить значение метода value. Ожидаемое значение: {expectedValue}")
    private void checkValue(int actualValue, int expectedValue) {
        Assertions.assertEquals(
                expectedValue,
                actualValue,
                "Получено неожиданное значение метода value"
        );
    }

    @Step("Проверить, что сервис активен")
    private void checkIsActiveResult(boolean actualResult) {
        Assertions.assertTrue(
                actualResult,
                "Ожидалось, что сервис будет активен"
        );
    }

    @Step("Проверить результат isEven для числа: {number}")
    private void checkIsEvenResult(int number, boolean actualResult) {
        boolean expectedResult = number % 2 == 0;

        Assertions.assertEquals(
                expectedResult,
                actualResult,
                "Метод isEven вернул неправильный результат"
        );
    }

    @Step("Проверить уровень доступа для возраста: {age}")
    private void checkAccessResult(int age, String actualAccess) {
        String expectedAccess = age > 18 ? "Allowed" : "Denied";

        Assertions.assertEquals(
                expectedAccess,
                actualAccess,
                "Метод checkAccess вернул неправильный уровень доступа"
        );
    }

    @Step("Проверить корректность оценки для результата: {score}")
    private void checkGradeIsValid(int score, String actualGrade) {
        Assertions.assertTrue(
                score >= 0 && score <= 100,
                "Результат должен находиться в диапазоне от 0 до 100"
        );

        Assertions.assertNotNull(
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
        Assertions.assertEquals(
                expectedGrade,
                actualGrade,
                "Получена неправильная оценка для результата: " + score
        );
    }

    @Step("Сформировать тестовые результаты от 0 до 100")
    static Stream<Integer> provideScores() {
        Random random = new Random(42);

        return Stream.generate(() -> random.nextInt(101))
                .limit(10);
    }
}


