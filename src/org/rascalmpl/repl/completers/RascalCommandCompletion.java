package org.rascalmpl.repl.completers;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.rascalmpl.interpreter.utils.StringUtils;
import org.rascalmpl.interpreter.utils.StringUtils.OffsetLengthTerm;
public class RascalCommandCompletion implements Completer {
    private static final NavigableMap<String,String> COMMAND_KEYWORDS;
    static {
        COMMAND_KEYWORDS = new TreeMap<>();
        COMMAND_KEYWORDS.put("set", "change a evaluator setting");
        COMMAND_KEYWORDS.put("undeclare", "undeclare a local variable of the REPL");
        COMMAND_KEYWORDS.put("help", "print help message");
        COMMAND_KEYWORDS.put("edit", "open a rascal module in the editor");
        COMMAND_KEYWORDS.put("unimport", "unload an imported module from the REPL");
        COMMAND_KEYWORDS.put("declarations", "show declarations"); // TODO figure out what it does
        COMMAND_KEYWORDS.put("quit", "cleanly exit the REPL");
        COMMAND_KEYWORDS.put("history", "history"); // TODO: figure out what it does
        COMMAND_KEYWORDS.put("test", "run rest modules");
        COMMAND_KEYWORDS.put("modules", "show imported modules");// TODO: figure out what it does 
        COMMAND_KEYWORDS.put("clear", "clear repl screen");
    }

    private final NavigableMap<String, String> setOptions;
    private final BiConsumer<String, List<Candidate>> completeIdentifier;
    private final BiConsumer<String, List<Candidate>> completeModule;
    public RascalCommandCompletion(NavigableMap<String, String> setOptions, BiConsumer<String, List<Candidate>> completeIdentifier, BiConsumer<String, List<Candidate>> completeModule) {
        this.setOptions = setOptions;
        this.completeIdentifier = completeIdentifier;
        this.completeModule = completeModule;
    }


    private static void generateCandidates(String partial, NavigableMap<String, String> candidates, String group, List<Candidate> target) {
        for (var can : candidates.subMap(partial, true, partial + Character.MAX_VALUE, false).entrySet()) {
            target.add(new Candidate(can.getKey(), can.getKey(), group, can.getValue(), null, null, true));
        }
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        var words = line.words();
        if (words.isEmpty() || !words.get(0).equals(":")) {
            return;
        }
        if (line.wordIndex() == 1) {
            // complete initial command/modifier
            generateCandidates(line.word(), COMMAND_KEYWORDS, "interpreter modifiers", candidates);
            return;
        }
        if (line.wordIndex() == 2) {
            // complete arguments for first
            switch (words.get(1)) {
                case "set": 
                    generateCandidates(line.word(), setOptions, "evaluator settings", candidates);
                    return;
                case "undeclare": 
                    completeIdentifier.accept(line.word(), candidates);
                    return;
                case "edit": // intended fall-through
                case "unimport": 
                    completeModule.accept(line.word(), candidates);
                    return;
                default: return;
            }
        }
        // for the future it would be nice to also support completing thinks like `:set profiling <cursor>`
    }

}
