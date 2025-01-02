package org.rascalmpl.repl.parametric;

import java.util.List;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class ParametricCompleter implements Completer {
    private final ILanguageProtocol lang;
    
    public ParametricCompleter(ILanguageProtocol lang) {
        this.lang = lang;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        var word = line.word();
        if (word == null) {
            word = "";
        }
        // TODO: in the future consider making the interface more a map of the jline3 candidate interface
        lang.completeFragment(line.line(), word)
            .forEach((c, g) -> candidates.add(new Candidate(c, c, g, null, null, null, false)));
    }

}
