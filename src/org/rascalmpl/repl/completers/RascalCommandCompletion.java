/*
 * Copyright (c) 2015-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.rascalmpl.repl.completers;


import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
public class RascalCommandCompletion implements Completer {
    private static final NavigableMap<String,String> COMMAND_KEYWORDS;
    static {
        COMMAND_KEYWORDS = new TreeMap<>();
        COMMAND_KEYWORDS.put("set", "change an evaluator setting");
        COMMAND_KEYWORDS.put("undeclare", "undeclare a local variable of the REPL");
        COMMAND_KEYWORDS.put("help", "print help message");
        COMMAND_KEYWORDS.put("edit", "open a rascal module in the editor");
        COMMAND_KEYWORDS.put("unimport", "unload an imported module from the REPL");
        COMMAND_KEYWORDS.put("declarations", "show declarations"); // TODO figure out what it does
        COMMAND_KEYWORDS.put("quit", "cleanly exit the REPL");
        COMMAND_KEYWORDS.put("history", "history"); // TODO: figure out what it does
        COMMAND_KEYWORDS.put("test", "run test modules");
        COMMAND_KEYWORDS.put("modules", "show imported modules");// TODO: figure out what it does 
        COMMAND_KEYWORDS.put("clear", "clear REPL screen");
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
