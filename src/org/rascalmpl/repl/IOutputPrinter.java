package org.rascalmpl.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

public interface IOutputPrinter {
    void write(PrintWriter target);

    default Reader asReader() {
        try (var result = new StringWriter()) {
            try (var resultWriter = new PrintWriter(result)) {
                write(resultWriter);
            }
            return new StringReader(result.toString());
        }
        catch (IOException ex) {
            throw new IllegalStateException("StringWriter close should never throw exception", ex);
        }
    }
}
