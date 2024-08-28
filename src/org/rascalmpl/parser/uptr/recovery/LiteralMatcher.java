/**
 * Copyright (c) 2022, NWO-I Centrum Wiskunde & Informatica (CWI)
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
