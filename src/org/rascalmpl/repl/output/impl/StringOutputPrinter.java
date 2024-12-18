package org.rascalmpl.repl.output.impl;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;

import org.rascalmpl.repl.output.IOutputPrinter;
import org.rascalmpl.repl.output.MimeTypes;

public class StringOutputPrinter implements IOutputPrinter {
    private final String body;
    private final String newline;
    
    public StringOutputPrinter(String body, String newline) {
        this(body, newline, MimeTypes.PLAIN_TEXT);
    }
    public StringOutputPrinter(String body, String newline, String mimeType) {
        this.body = body;
        this.newline = newline;
    }

    @Override
    public String mimeType() {
        return mimeType();
    }

    @Override
    public void write(PrintWriter target) {
        target.print(body);
        target.print(newline);
    }
    
    @Override
    public Reader asReader() {
        return new StringReader(body + newline);
    }
}
