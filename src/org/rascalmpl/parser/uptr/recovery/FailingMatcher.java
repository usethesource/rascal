package org.rascalmpl.parser.uptr.recovery;

public class FailingMatcher implements InputMatcher {
    @Override
    public MatchResult findMatch(int[] input, int startLocation) {
        return null;
    }
}
