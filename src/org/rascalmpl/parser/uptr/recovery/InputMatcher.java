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

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CaseInsensitiveLiteralStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.StackNodeVisitorAdapter;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IString;

public interface InputMatcher {
    public static InputMatcher FAIL = new FailingMatcher();

    // Find a match in the input. Returning `null` means no match has been found.
    MatchResult findMatch(int[] input, int startLocation, int maxLength);

    public static class MatchResult {
        private final int start;
        private final int length;

        public MatchResult(int start, int length) {
            this.start = start;
            this.length = length;
        }

        public int getStart() {
            return start;
        }

        public int getLength() {
            return length;
        }

        public int getEnd() {
            return start + length;
        }

        @Override
        public String toString() {
            return "MatchResult [start=" + start + ", length=" + length + "]";
        }        
    }

    public static InputMatcher createMatcher(IConstructor constructor) {
        if (constructor.getConstructorType() == RascalValueFactory.Symbol_Lit) {
            return new LiteralMatcher(((IString) constructor.get(0)).getValue());
        }

        if (constructor.getConstructorType() == RascalValueFactory.Symbol_Cilit) {
            return new CaseInsensitiveLiteralMatcher(((IString) constructor.get(0)).getValue());
        }

        return FAIL;
    }

    public static <P> InputMatcher createMatcher(AbstractStackNode<P> stackNode) {
        return stackNode.accept(new StackNodeVisitorAdapter<P,InputMatcher>() {
            @Override
            public InputMatcher visit(LiteralStackNode<P> literal) {
                return new LiteralMatcher(literal.getLiteral());
            }

            @Override
            public InputMatcher visit(CaseInsensitiveLiteralStackNode<P> literal) {
                return new CaseInsensitiveLiteralMatcher(literal.getLiteral());
            }
        });
    }
}
