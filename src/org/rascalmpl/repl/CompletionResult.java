package org.rascalmpl.repl;

import java.util.Collection;

public class CompletionResult {
  private final int offset;
  private final int length;
  private final Collection<String> suggestions;

  public CompletionResult(int offset, int length, Collection<String> suggestions) {
    this.offset = offset;
    this.length = length;
    this.suggestions = suggestions;
  }
  public int getLength() {
    return length;
  }
  public int getOffset() {
    return offset;
  }
  public Collection<String> getSuggestions() {
    return suggestions;
  }
}
