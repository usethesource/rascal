package org.rascalmpl.parser.uptr.recovery;

public class LiteralMatcher implements InputMatcher {
    private int[] chars;

    public LiteralMatcher(String literal) {
        chars = new int[literal.length()];
        for (int i=0; i<literal.length(); i++) {
            chars[i] = literal.codePointAt(i);
        }
    }

    public LiteralMatcher(int[] literal) {
        chars = literal;
    }

    @Override
    public MatchResult findMatch(int[] input, int startLocation) {
        int length = chars.length;
        for (int start=startLocation; start<input.length - length + 1; start++) {
            boolean matches = true;
            for (int i=0; i<length; i++) {
                if (input[start + i] != chars[i]) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return new MatchResult(start, length);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "LiteralMatcher[" + new String(chars, 0, chars.length) + "]";
    }
}
