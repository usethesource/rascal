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

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.rascalmpl.debug.NullRascalMonitor;
import org.rascalmpl.shell.ShellEvaluatorFactory;

import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IString;

/**
 * Make sure we generate escapes before rascal keywords, such that `lang::rascal::syntax` becomes `lang::rascal::\syntax`
 */
public class RascalQualifiedNames {

    private static final Pattern splitIdentifiers = Pattern.compile("::");

    public static String escape(String name) {
        return splitIdentifiers.splitAsStream(name + " ") // add space such that the last "::" is not lost
            .map(RascalQualifiedNames::escapeKeyword)
            .collect(Collectors.joining("::")).trim();
    }
    public static String unescape(String term) {
        if (!term.contains("\\")) {
            return term;
        }
        return splitIdentifiers.splitAsStream(term + " ") // add space such that the last "::" is not lost
            .map(RascalQualifiedNames::unescapeKeyword)
            .collect(Collectors.joining("::")).trim()
            ;
    }

    private static final Set<String> RASCAL_KEYWORDS = new HashSet<String>();

    private static void assureKeywordsAreLoaded() {
        if (RASCAL_KEYWORDS.isEmpty()) {
            synchronized (RASCAL_KEYWORDS) {
                if (!RASCAL_KEYWORDS.isEmpty()) {
                    return;
                }

                var monitor = new NullRascalMonitor();
                var eval = ShellEvaluatorFactory.getBasicEvaluator(Reader.nullReader(), new PrintWriter(Writer.nullWriter()), new PrintWriter(Writer.nullWriter()), monitor);
                eval.doImport(monitor, "util::Reflective");

                ((ISet) eval.call("getRascalReservedIdentifiers"))
                    .stream()
                    .filter(IString.class::isInstance)
                    .map(IString.class::cast)
                    .map(IString::getValue)
                    .forEach(RASCAL_KEYWORDS::add);

                if (RASCAL_KEYWORDS.isEmpty()) {
                    RASCAL_KEYWORDS.add("syntax");
                }
            }
        }
    }

    private static String escapeKeyword(String s) {
        assureKeywordsAreLoaded();
        if (RASCAL_KEYWORDS.contains(s.trim())) {
            return "\\" + s;
        }
        return s;
    }

    private static String unescapeKeyword(String s) {
        if (s.startsWith("\\") && !s.contains("-")) {
            return s.substring(1);
        }
        return s;
    }

}
