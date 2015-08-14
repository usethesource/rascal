package org.rascalmpl.shell;

import java.util.Collection;
import java.util.List;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.utils.StringUtils;
import org.rascalmpl.interpreter.utils.StringUtils.OffsetLengthTerm;

import jline.console.completer.Completer;

public class RascalCompleter implements Completer {

  private final Evaluator eval;

  public RascalCompleter(Evaluator evaluator) {
    eval = evaluator;
  }

  @Override
  public int complete(String buffer, int cursor, List<CharSequence> candidates) {
    OffsetLengthTerm identifier = StringUtils.findRascalIdentifierAtOffset(buffer, cursor);
    candidates.clear();
    if (identifier != null) {
      Collection<String> completions = eval.completePartialIdentifier(identifier.term);
      if (completions == null || completions.isEmpty()) {
        return -1;
      }
      completions.stream()
        .map(or -> or.substring(identifier.length)) // remove the common prefix
        .forEach(c -> candidates.add(c));
      return identifier.offset + identifier.length; // give the location in the buffer where the completion should happen
    }
    return -1;
  }

}
