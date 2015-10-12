package org.rascalmpl.repl;

import java.util.Collection;

public class CompletionResult {
    private final int offset;
    private final Collection<String> suggestions;

    public CompletionResult(int offset, Collection<String> suggestions) {
        this.offset = offset;
        this.suggestions = suggestions;
    }
    public int getOffset() {
        return offset;
    }
    public Collection<String> getSuggestions() {
        return suggestions;
    }
}
