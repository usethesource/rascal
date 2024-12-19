package org.rascalmpl.repl.output.impl;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;

import org.rascalmpl.repl.output.IOutputPrinter;
import org.rascalmpl.repl.output.MimeTypes;

public class StringOutputPrinter implements IOutputPrinter {
    private final String body;

    
    public StringOutputPrinter(String body) {
        this(body, MimeTypes.PLAIN_TEXT);
    }
    public StringOutputPrinter(String body, String mimeType) {
        this.body = body;
    }

    @Override
    public String mimeType() {
        return mimeType();
    }

    @Override
    public void write(PrintWriter target) {
        target.println(body);
    }
    
    @Override
    public Reader asReader() {
        return new StringReader(body + System.lineSeparator());
    }
}
