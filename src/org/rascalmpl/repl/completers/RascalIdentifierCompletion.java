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
