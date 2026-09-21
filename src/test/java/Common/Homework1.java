package Common;

import java.util.ArrayList;
import java.util.List;

public class Homework1 {

    public static boolean isEven(int n) {
        return n % 2 == 0;
    }

    public static String checkAccess(int age) {
        if (age > 18) {
            return "Allowed";
        } else {
            return "Denied";
        }
    }

    public static boolean isPositive(int n) {
        return (n >= 0) ? true : false;
        // return n >= 0;
    }

    public static String getGrade(int score) {
        if (score >= 0 && score <= 20) {
            return "E";
        } else if (score >= 21 && score <= 40) {
            return "D";
        } else if (score >= 41 && score <= 60) {
            return "C";
        } else if (score >= 61 && score <= 80) {
            return "B";
        } else if (score >= 81 && score <= 100) {
            return "A";
        } else {
            return "Error";
        }
    }

    public static String blastOff(int start) {
        if (start <= 0) {
            return "Поехали!";
        }
        String result = "";
        for (int i = start; i >= 1; i--) {
            result += i + " ";
        }
        result += "Поехали!";
        return result;
    }

    public static int sumToN(int n) {
        if (n <= 0) {
            return 0;
        }
        return n * (n + 1) / 2;
    }

    public static boolean hasBug(String[] messages) {
        if (messages == null) return false;
        for (String s : messages) {
            if (s != null && s.equalsIgnoreCase("Bug")) {
                return true;
            }
        }
        return false;
    }

    public static String getEvenInRange(int start, int end) {
        int from = Math.min(start, end);
        int to = Math.max(start, end);
        String result = "";
        boolean first = true;

        for (int i = from; i <= to; i++) {
            if (i % 2 == 0) {
                if (!first) result += " ";
                result += i;
                first = false;
            }
        }
        return result;
    }

    public static int findMax(int[] arr) {
        int m = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > m) {
                m = arr[i];
            }
        }
        return m;
    }

    public static String[] reverse(String[] arr) {
        String[] rev = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            rev[arr.length - 1 - i] = arr[i];
        }
        return rev;
    }

    public static double calcAverage(List<Integer> list) {
        long sum = 0;
        for (int x : list) {
            sum += x;
        }
        return (double) sum / list.size();
    }

    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        List<String> result = new ArrayList<>(list.size());
        for (String s : list) {
            if (s == null) continue;
            if (!s.equals(nameToRemove)) {
                result.add(s);
            }
        }
        return result;
    }
}
