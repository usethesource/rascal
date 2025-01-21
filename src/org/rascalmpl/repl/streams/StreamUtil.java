/*
 * Copyright (c) 2023-2025, NWO-I CWI and Swat.engineering
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
package org.rascalmpl.repl.streams;

import java.io.PrintWriter;
import java.io.Writer;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

/**
 * Small class with some utilities to wrap/simulate streams
 */
public class StreamUtil {

    public static PrintWriter generateErrorStream(Terminal tm, Writer out) {
        // previously we would alway write errors to System.err, but that tends to mess up terminals
        // and also our own error print
        // so now we try to not write to System.err
        if (supportsColors(tm)) {
            return new PrintWriter(new RedErrorWriter(out), true);
        }
        if (supportsItalic(tm)) {
            return new PrintWriter(new ItalicErrorWriter(out), true);
        }
        return new PrintWriter(System.err, true);
    
    }

    public static boolean supportsColors(Terminal tm) {
        Integer cols = tm.getNumericCapability(Capability.max_colors);
        return cols != null && cols >= 8;
    }

    public static boolean supportsItalic(Terminal tm) {
        String ital = tm.getStringCapability(Capability.enter_italics_mode);
        return ital != null && !ital.equals("");
    }
    
}
