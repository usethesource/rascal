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
