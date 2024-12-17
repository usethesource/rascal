package org.rascalmpl.repl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/**
 * The output of a REPL command is represented by this class, depending on the consumer (terminal/notebook/webserver) a different function might be called.
 */
public interface IOutputPrinter {
    /**
     * Write the output on this print writer interface. 
     * In case {@linkplain #isBinary()} returns true, it might not be called, but please write a message why binary output should have been used.
     * @param target where to write the output to.
     */
    void write(PrintWriter target);


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

    /**
     * Some renders support binary output (such as images), if this function returns true, they'll call
     * either {@linkplain #write(OutputStream)} or {@linkplain #asInputStream()} instead.
     */
    default boolean isBinary() {
        return false;
    }

    /**
     * Write bytes to a stream, it will only be called is {@linkplain #isBinary()} returns true, the renderer supports it, and the renderer opens a dedicated stream per resource.
     * @throws IOException function on `OutputStream` can cause IOExceptions
     */

    default void write(OutputStream target) throws IOException {
        throw new RuntimeException("Write to output stream only supported in case of binary output (such as images)");
    }


    /**
     * Produce bytes that represent the output of a stream, in a streaming/pull style. Will only be called if {@linkplain #isBinary()} is true, the renderer supports it, and the renderer prefers an inputstream to copy from.
     * @return an streaming representation of the bytes that makeup the output of the command
     */
    default InputStream asInputStream() {
        try (var result = new ByteArrayOutputStream()) {
            write(result);
            return new ByteArrayInputStream(result.toByteArray());
        }
        catch (IOException ex) {
            throw new IllegalStateException("Write or Close should not have throw an exception", ex);
        }
    }
}
