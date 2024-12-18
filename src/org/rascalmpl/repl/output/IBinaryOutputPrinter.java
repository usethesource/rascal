package org.rascalmpl.repl.output;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/**
 * Sometimes, an output produced a binary (such as an image or a executable file)
 * In that case, you can return a return an overload of this type, which a renderer can add support for
 * 
 * <p>
 * For example an output under the <code>image/png</code> would most likely have regular {@link IOutputPrinter#write(java.io.PrintWriter)} that prints a message saying it's a image that can't be printed as text, while the render (if it supports it) can cast it to this interface and get the actual bytes
 * </p>
 */
public interface IBinaryOutputPrinter extends IOutputPrinter {
    /**
     * Write bytes to a stream.
     * @throws IOException functions on `OutputStream` can cause IOExceptions
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
