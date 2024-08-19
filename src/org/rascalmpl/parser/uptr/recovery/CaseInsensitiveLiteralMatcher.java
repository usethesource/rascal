package org.rascalmpl.parser.uptr.recovery;

public class CaseInsensitiveLiteralMatcher implements InputMatcher {
	private final int[] chars;
    private final int[] altChars;

    public CaseInsensitiveLiteralMatcher(String literal) {
        int length = literal.length();
		chars = new int[length];
		altChars = new int[length];
		for(int i = 0; i<length; i++){
			int character = literal.codePointAt(i);
            chars[i] = character;
			switch (Character.getType(character)) {
			    case Character.LOWERCASE_LETTER:
                altChars[i] = Character.toUpperCase(character);
                break;

                case Character.UPPERCASE_LETTER:
                altChars[i] = Character.toLowerCase(character);
                break;

                default:
                altChars[i] = character;
                break;
            }
		}
    }

    public CaseInsensitiveLiteralMatcher(int[][] ciLiteral) {
        int length = ciLiteral.length;
		chars = new int[length];
		altChars = new int[length];
		for(int i = 0; i<length; i++){
            chars[i] = ciLiteral[i][0];
            altChars[i] = ciLiteral[i][ciLiteral[i].length - 1];
        }
    }

    @Override
    public MatchResult findMatch(int[] input, int startLocation) {
        int length = chars.length;

        for (int start=startLocation; start < input.length - length; start++) {
            boolean matches = true;
            for (int i=0; i<length; i++) {
                int inputChar = input[start+i];
                if (inputChar != chars[i] && inputChar != altChars[i]) {
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

    public String toString() {
        return "CaseInsensitiveLiteralMatcher[" + new String(chars, 0, chars.length) + "]";
    }

}
