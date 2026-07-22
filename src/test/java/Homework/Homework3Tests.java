package Homework;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import java.util.List;

public class Homework3Tests {

    private final Homework3 service = new Homework3();

    @Test
    @DisplayName("Test 1: isAdult возвращает true для 20")
    void testIsAdult() {
        boolean actual = service.isAdult(20);
        Assertions.assertTrue(actual, "Ожидалось true, но получил false для age=20");
    }

    @Test
    @DisplayName("Test 2: getNames возвращает список не пустой")
    void testGetNamesNotEmpty() {
        List<String> names = service.getNames();
        Assertions.assertNotNull(names, "Список не должен быть null");
        Assertions.assertFalse(names.isEmpty(), "Список имён не должен быть пустым");
    }

    @Test
    @DisplayName("Test 3: проверка корректности значения (падает)")
    void testValueShouldBe8ButIs7() {
        int actual = service.value();
        Assertions.assertEquals(8, actual, "Ожидалось 8, но получено " + actual);
    }

    @Test
    @DisplayName("Test 4: isActive возвращает true")
    void testIsActive() {
        boolean actual = service.isActive();
        Assertions.assertTrue(actual, "Ожидалось true для isActive");
    }
}
