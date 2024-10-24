/**
 * Copyright (c) 2024, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

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
            if (Character.isLowerCase(character)) {
                altChars[i] = Character.toUpperCase(character);
            } else if (Character.isUpperCase(character)) {
                altChars[i] = Character.toLowerCase(character);
            } else {
                altChars[i] = character;
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
    public MatchResult findMatch(int[] input, int startLocation, int maxLength) {
        int length = chars.length;

        int limit = Math.min(startLocation + maxLength - length, input.length - length + 1);
        for (int start=startLocation; start < limit; start++) {
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
