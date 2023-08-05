package net.zonixmc.beacon.util;

public class NumberUtil {

    private final static String[] ROMAN_NUMERALS = { "I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M" };
    private final static int[] ROMAN_VALUES = { 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000 };

    public static String convertToRoman(int number) {

        if (number < 1 || number > 3999) return null;

        StringBuilder roman = new StringBuilder();

        for (int index = ROMAN_VALUES.length - 1; index >= 0; index--) {

            while (number >= ROMAN_VALUES[index]) {

                number -= ROMAN_VALUES[index];
                roman.append(ROMAN_NUMERALS[index]);

            }

        }

        return roman.toString();

    }

    public static Integer parseToInt(String context) {

        try {

            double aDouble = Double.parseDouble(context);

            if (Double.isInfinite(aDouble) || Double.isNaN(aDouble)) return null;

            return Integer.parseInt(context);

        }

        catch (Exception ignored) { return null; }

    }

}
