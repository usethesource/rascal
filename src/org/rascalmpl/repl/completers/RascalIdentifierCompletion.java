package org.rascalmpl.repl.completers;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class RascalIdentifierCompletion implements Completer {

    private final BiFunction<String, String, Map<String, String>> lookupPartialIdentifiers;
    
    public RascalIdentifierCompletion(BiFunction<String, String, Map<String, String>> lookupPartialIdentifiers) {
        this.lookupPartialIdentifiers = lookupPartialIdentifiers;
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        boolean canBeIdentifier;
        switch (line.words().get(0)) {
            case ":": //fallthrough
                // completion of settings for the REPL is handled elsewere
                // it will also call this function in the 1 case where it's needed
            case "import": // fallthrough
                // not triggering on import of modules
            case "extend": // fallthrough
                // not triggering on extend of modules
                canBeIdentifier = false;
                break;
            default:
                canBeIdentifier = true;
                break;
            
        }
        if (canBeIdentifier) {
            completePartialIdentifier(line.word(), candidates);
        }
    }

    public void completePartialIdentifier(String name, List<Candidate> candidates) {
        name = RascalQualifiedNames.unescape(name); // remove escape that the interpreter cannot deal with
        int qualifiedSplit = name.lastIndexOf("::");
        String qualifier = qualifiedSplit > -1 ? name.substring(0, qualifiedSplit) : "";
        String partial = qualifiedSplit > -1 ? name.substring(qualifiedSplit + 2) : name;
        for (var can: lookupPartialIdentifiers.apply(qualifier, partial).entrySet()) {
            String id = RascalQualifiedNames.escape(can.getKey());
            candidates.add(new Candidate(id, id, can.getValue(), null, null, null, false));
        }
    }
    
}
