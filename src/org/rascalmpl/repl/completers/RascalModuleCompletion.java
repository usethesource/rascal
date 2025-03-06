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
        List<String> paths = searchPathLookup.apply(moduleRoot);
        if (paths != null) {
            for (var mod : paths) {
                var fullPath = RascalQualifiedNames.escape(modulePrefix + mod);
                var isFullModulePath = !mod.endsWith("::");
                candidates.add(new Candidate(fullPath + (isFullModulePath & importStatement? ";" : ""), fullPath, "modules", null, null, null, false));
            }
        }
    }
}
