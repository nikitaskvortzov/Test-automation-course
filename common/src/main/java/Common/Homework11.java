package Common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class Homework11 {

    public static final Comparator<Integer> ABS_DESC_COMPARATOR =
            Comparator.comparingInt((Integer number) -> Math.abs(number))
                    .reversed();

    public static final Comparator<String> SUDO_FIRST_COMPARATOR =
            (first, second) -> {
                boolean firstContainsSudo = first.contains("sudo");
                boolean secondContainsSudo = second.contains("sudo");

                if (firstContainsSudo && !secondContainsSudo) {
                    return -1;
                }

                if (!firstContainsSudo && secondContainsSudo) {
                    return 1;
                }

                return 0;
            };

    public static long countElementsGreaterThanFive(
            Collection<Integer> numbers
    ) {
        return numbers.stream()
                .filter(number -> number > 5)
                .count();
    }

    public static Integer[] convertStringsToIntegers(String[] strings) {
        return Arrays.stream(strings)
                .filter(string -> string != null && string.matches("\\d+"))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }
}
//