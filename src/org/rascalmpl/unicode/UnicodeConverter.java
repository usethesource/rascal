package org.rascalmpl.unicode;

public class UnicodeConverter {
    public static String unicodeArrayToString(int[] chars) {
        StringBuilder builder = new StringBuilder();
        for (int c : chars) {
            builder.appendCodePoint(c);
        }

        return builder.toString();
    }
}
