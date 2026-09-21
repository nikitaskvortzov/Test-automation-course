package Common;

import java.util.List;
import java.util.Arrays;

public class Homework3 {
    // возвращает булево значение
    public boolean isAdult(int age) {
        return age >= 18;
    }

    // должен возвращать список
    public List<String> getNames() {
        return Arrays.asList("Nikita", "Vova", "Oleg");
    }

    // метод, который вернет неверное значение
    public int value() {
        return 7;
    }

    // возвращает булево значение
    public boolean isActive() {
        return true;
    }
}
