package Homework;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

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


//package Homework;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.Stream;
//
//public class Homework3Tests {
//
//    private final Homework3 service = new Homework3();
//    private static final Random RANDOM = new Random(123);
//
//    // Задание 1
//    @RepeatedTest(10)
//    @DisplayName("Test 1: isAdult возвращает true для возраста 20")
//    void testIsAdult() {
//        boolean actual = service.isAdult(20);
//        Assertions.assertTrue(actual,
//                "Ожидалось true для isAdult(20), но получено: " + actual);
//    }
//
//    @RepeatedTest(10)
//    @DisplayName("Test 2: getNames возвращает непустой список")
//    void testGetNamesNotEmpty() {
//        List<String> names = service.getNames();
//        Assertions.assertNotNull(names, "Список не должен быть null");
//        Assertions.assertFalse(names.isEmpty(),
//                "Список имён не должен быть пустым");
//    }
//
//    @RepeatedTest(10)
//    @DisplayName("Test 3: проверка значения, должна падать (ожидалось 8, получено 7)")
//    void testValueShouldBe8ButIs7() {
//        int actual = service.value();
//        Assertions.assertEquals(8, actual,
//                "Ожидалось 8, но получено: " + actual);
//    }
//
//    @RepeatedTest(10)
//    @DisplayName("Test 4: isActive возвращает true")
//    void testIsActive() {
//        boolean actual = service.isActive();
//        Assertions.assertTrue(actual,
//                "Ожидалось true для isActive, но получено: " + actual);
//    }
//
//    // Задание 2
//
//    @Test
//    @DisplayName("Test isEven with a random number 1..100 (Test)")
//    void testIsEvenSingle() {
//        int n = RANDOM.nextInt(100) + 1;
//        boolean result = Homework1.isEven(n);
//        Assertions.assertTrue(result,
//                "Ожидалось true для isEven(" + n + "), но получено false");
//    }
//
//    @Test
//    @DisplayName("Test checkAccess with 20 random ages 0..99 (Test)")
//    void testCheckAccessSingle() {
//        boolean allPassed = true;
//        for (int i = 0; i < 20; i++) {
//            int age = RANDOM.nextInt(100);
//            String access = Homework1.checkAccess(age);
//            boolean pass = (age > 18 && "Allowed".equals(access)) || (age <= 18 && "Denied".equals(access));
//            if (!pass) {
//                allPassed = false;
//            }
//        }
//        Assertions.assertTrue(allPassed,
//                "Ожидалось корректное поведение checkAccess для 20 случайных возрастов");
//    }
//
//    @Test
//    @DisplayName("Test getGrade with single random score (Test)")
//    void testGetGradeSingle() {
//        int score = RANDOM.nextInt(101);
//        String grade = Homework1.getGrade(score);
//        Assertions.assertNotNull(grade,
//                "Ожидалось, что grade не будет null для score=" + score);
//        Assertions.assertTrue(score >= 0 && score <= 100,
//                "Ожидалось 0 <= score <= 100, получено " + score);
//    }
//
//    @Test
//    @DisplayName("Test getGrade boundary (Test)")
//    void testGetGradeBoundarySingle() {
//        int score = 50;
//        String grade = Homework1.getGrade(score);
//        Assertions.assertEquals("C", grade,
//                "Ожидалось 'C' для score=50, получено '" + grade + "'");
//    }
//
//    @RepeatedTest(10)
//    @DisplayName("RepeatedTest: isEven random (repeat 10)")
//    void repeatedTestIsEven() {
//        int n = RANDOM.nextInt(100) + 1;
//        boolean result = Homework1.isEven(n);
//        Assertions.assertEquals(n % 2 == 0, result,
//                "Ожидалось, что isEven(" + n + ") вернет правильный результат");
//    }
//
//    @RepeatedTest(10)
//    @DisplayName("RepeatedTest: checkAccess random ages (repeat 10)")
//    void repeatedTestCheckAccess() {
//        int age = RANDOM.nextInt(100);
//        String access = Homework1.checkAccess(age);
//        boolean pass = (age > 18 && "Allowed".equals(access)) || (age <= 18 && "Denied".equals(access));
//        Assertions.assertTrue(pass,
//                "Ожидалось корректное значение access для age=" + age + ", получено: " + access);
//    }
//
//    @RepeatedTest(10)
//    @DisplayName("RepeatedTest: getGrade random score (repeat 10)")
//    void repeatedTestGetGrade() {
//        int score = RANDOM.nextInt(101);
//        String grade = Homework1.getGrade(score);
//        Assertions.assertNotNull(grade, "getGrade вернул null для score=" + score);
//        Assertions.assertTrue(score >= 0 && score <= 100,
//                "Score должен быть в диапазоне 0..100, получено " + score);
//    }
//
//    @RepeatedTest(10)
//    @DisplayName("RepeatedTest: getGrade boundary check (repeat 10)")
//    void repeatedTestGetGradeBoundary() {
//        int score = 81;
//        String grade = Homework1.getGrade(score);
//        Assertions.assertEquals("A", grade,
//                "Ожидалось 'A' для score=81, получено '" + grade + "'");
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: getGrade with random scores 0..100 (Test)")
//    void parameterizedTestGetGrade(int score) {
//        String grade = Homework1.getGrade(score);
//        Assertions.assertNotNull(grade, "getGrade вернул null для score=" + score);
//        Assertions.assertTrue(score >= 0 && score <= 100,
//                "Score должен быть в диапазоне 0..100, получено " + score);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: isEven with scores (Test)")
//    void parameterizedTestIsEven(int score) {
//        boolean res = Homework1.isEven(score);
//        Assertions.assertEquals(res, score % 2 == 0,
//                "isEven(" + score + ") должен возвращать верный результат");
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: checkAccess with scores (Test)")
//    void parameterizedTestCheckAccess(int score) {
//        String access = Homework1.checkAccess(score);
//        boolean pass = "Allowed".equals(access) || "Denied".equals(access);
//        Assertions.assertTrue(pass, "Ожидалось 'Allowed' или 'Denied' для score=" + score + ", получено: " + access);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideScores")
//    @DisplayName("ParameterizedTest: another getGrade (Test)")
//    void parameterizedTestGetGradeAnother(int score) {
//        String grade = Homework1.getGrade(score);
//        Assertions.assertNotNull(grade, "getGrade вернул null для score=" + score);
//        Assertions.assertTrue(score >= 0 && score <= 100,
//                "Score должен быть в диапазоне 0..100, получено " + score);
//    }
//
//    static Stream<Integer> provideScores() {
//        Random rnd = new Random(42);
//        return Stream.generate(() -> rnd.nextInt(101)).limit(10);
//    }
//}
