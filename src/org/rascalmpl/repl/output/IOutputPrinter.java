package org.rascalmpl.repl.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * The output of a REPL command is represented by this class, depending on the consumer (terminal/notebook/webserver) a different function might be called.
 */
public interface IOutputPrinter {
    
    /**
     * Write the output on this print writer interface. It should always print something, even if it's a warning saying it cannot be printed
     * @param target where to write the output to.
     */
    void write(PrintWriter target);

    String mimeType();

    /**
     * Offer the same output as {@linkplain #write(PrintWriter)} but as a pull based reader.
     * The standard implementation takes care to just call the write function with a buffer.
     * If you however can provide a streaming reading, override this function instead, depending
     * on the consumer, it might be called instead of the write function.
     * @return a reader that produces the same contents as the write function, but in a pull style instead of push. 
     */
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
