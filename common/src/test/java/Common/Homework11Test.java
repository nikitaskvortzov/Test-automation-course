package Common;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit_tests")

public class Homework11Test {

    @Test
    void shouldSortNumbersByAbsoluteValueDescending() {
        Integer[] numbers = {2, -10, 5};
        Arrays.sort(numbers, Homework11.ABS_DESC_COMPARATOR);
        Integer[] expected = {-10, 5, 2};
        assertArrayEquals(expected, numbers);
    }

    @Test
    void shouldPutSudoStringFirst() {
        String first = "sudo command";
        String second = "ordinary command";
        int result = Homework11.SUDO_FIRST_COMPARATOR.compare(first, second);
        assertEquals(-1, result);
    }

    @Test
    void shouldCountElementsGreaterThanFive() {
        Collection<Integer> numbers = List.of(2, 6, 10);
        long result = Homework11.countElementsGreaterThanFive(numbers);
        assertEquals(2, result);
    }

    @Test
    void shouldConvertStringsToIntegers() {
        String[] strings = {"10", "abc", "5"};
        Integer[] result = Homework11.convertStringsToIntegers(strings);
        Integer[] expected = {10, 5};
        assertArrayEquals(expected, result);
    }
}
