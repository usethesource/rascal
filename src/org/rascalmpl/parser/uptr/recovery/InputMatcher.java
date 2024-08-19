package org.rascalmpl.parser.uptr.recovery;

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CaseInsensitiveLiteralStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.values.RascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IString;

public interface InputMatcher {
    public static InputMatcher FAIL = new FailingMatcher();

    MatchResult findMatch(int[] input, int startLocation);

    public static class MatchResult {
        private int start;
        private int length;

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
        if (stackNode instanceof LiteralStackNode) {
            return new LiteralMatcher(((LiteralStackNode<P>) stackNode).getLiteral());
        }

        if (stackNode instanceof CaseInsensitiveLiteralStackNode) {
            return new CaseInsensitiveLiteralMatcher(((CaseInsensitiveLiteralStackNode<P>) stackNode).getLiteral());
        }

        return null;
    }
}
