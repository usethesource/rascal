package org.rascalmpl.repl;

public interface CompletionFunction {
    CompletionResult complete(String line, int cursor);
}
