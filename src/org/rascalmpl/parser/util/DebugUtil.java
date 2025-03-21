/**
 * Copyright (c) 2024-2025, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

package org.rascalmpl.parser.util;

import org.rascalmpl.values.parsetrees.ProductionAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;

public class DebugUtil {
    /**
     * Turn a production IConstructor into a string of the form "S -> E1 E2 ..."
     */

    private DebugUtil() {
    }

    public static String prodToString(IConstructor prod) {
        StringBuilder builder = new StringBuilder("'");

        String sortName = ProductionAdapter.getSortName(prod);
        if (sortName.equals("LAYOUT")) {
            return sortName + " -> ...";
        }

        builder.append(quotedStringToPlain(sortName));

        builder.append(" ->");

        if (prod.getName().equals("prod")) {
            ProductionAdapter.getConstructorName(prod);
            IList symbols = (IList) prod.get(1);
            for (IValue symbol : symbols) {
                builder.append(" ");
                IConstructor conSymbol = (IConstructor) symbol;
                builder.append(quotedStringToPlain(String.valueOf(conSymbol.get(0))));   // Sort or label
            }
        } else if (prod.getName().equals("error")) {
            builder.append("err." + prod.get(2) + ":" + prodToString((IConstructor) prod.get(1)));
        } else {
            builder.append(prod.getName() + "...");
        }

        builder.append("'");

        return builder.toString();
    }

    private static String quotedStringToPlain(String s) {
        if (s.isEmpty()) {
            return s;
        }

        if (s.equals("\"LAYOUTLIST\"") || s.equals("LAYOUTLIST")) {
            return "L*";
        }

        if (s.charAt(0) == '"' && s.charAt(s.length()-1) == '"') {
            return s.substring(1, s.length()-1).replace("\\", "");
        }

        return s;
    }

    public static void opportunityToBreak() {
        // Nop method that allows breakpoints to be set at the call site even if originally there is no code to break on
    }

}
