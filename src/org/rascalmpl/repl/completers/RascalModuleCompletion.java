package org.rascalmpl.repl.completers;

import java.util.List;
import java.util.function.Function;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class RascalModuleCompletion implements Completer {

    private final Function<String, List<String>> searchPathLookup;
    
    public RascalModuleCompletion(Function<String, List<String>> searchPathLookup) {
        this.searchPathLookup = searchPathLookup;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        if (line.wordIndex() != 1) {
            // we can only complete import/extend statements that reference a module
            return;
        }
        switch (line.words().get(0)) {
            case "import": // intended fallthrough
            case "extend":
                completeModuleNames(line.word(), candidates, true);
                return;
            default:
                return;
        }
    }

    public void completeModuleNames(String word, List<Candidate> candidates, boolean importStatement) {
        // as jline will take care to filter prefixes, we only have to report modules in the directory (or siblings of the name)
        // we do not have to filter out prefixes
        word = RascalQualifiedNames.unescape(word); // remove escape that the interpreter cannot deal with
        int rootedIndex = word.lastIndexOf("::");
        String moduleRoot = rootedIndex == -1? "": word.substring(0, rootedIndex);
        String modulePrefix = moduleRoot.isEmpty() ? "" : moduleRoot + "::";
        for (var mod : searchPathLookup.apply(moduleRoot)) {
            var fullPath = RascalQualifiedNames.escape(modulePrefix + mod);
            var isFullModulePath = !mod.endsWith("::");
            candidates.add(new Candidate(fullPath + (isFullModulePath & importStatement? ";" : ""), fullPath, "modules", null, null, null, false));
        }
    }
}
