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
package org.rascalmpl.repl.output.impl;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;

import org.rascalmpl.repl.output.IOutputPrinter;
import org.rascalmpl.repl.output.MimeTypes;

public class AsciiStringOutputPrinter implements IOutputPrinter {
    private final String body;

    
    public AsciiStringOutputPrinter(String body) {
        this(body, MimeTypes.PLAIN_TEXT);
    }
    public AsciiStringOutputPrinter(String body, String mimeType) {
        this.body = body;
    }

    @Override
    public String mimeType() {
        return mimeType();
    }

    @Override
    public void write(PrintWriter target, boolean unicodeSupported) {
        target.println(body);
    }
    
    @Override
    public Reader asReader(boolean unicodeSupported) {
        return new StringReader(body + System.lineSeparator());
    }
}
