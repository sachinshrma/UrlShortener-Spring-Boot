package com.sachinshrma.URLShortener.util;

public class EncoderDecoder {
    private static final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String toBase(long number, int base) {
        if (number < base) {
            return Character.toString(alphabet.charAt((int) number));
        }

        long value = number;
        StringBuffer sb = new StringBuffer();

        while (value != 0) {
            int remind = (int) (value % base);
            value = (value - remind) / base;
            sb.append(alphabet.charAt(remind));
        }

        return sb.toString();
    }

    public static String toBase62(long number) {
        return toBase(number, 62);
    }
}
