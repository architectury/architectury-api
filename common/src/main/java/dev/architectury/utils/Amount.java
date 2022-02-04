package dev.architectury.utils;

public class Amount {
    /**
     * Converts a long to an int while dropping overflowed values.
     *
     * @param amount the long to convert
     * @return the int value
     */
    public static int toInt(long amount) {
        if (amount >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (amount <= Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        
        return (int) amount;
    }
}
