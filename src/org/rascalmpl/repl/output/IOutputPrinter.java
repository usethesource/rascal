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
package org.rascalmpl.repl.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Represent a lazy container for output. Depending on the consumer, it will ask to write to a writer, or to generate a reader.
 * There is also a {@link IBinaryOutputPrinter} overload that would indicate there is a file that should instead be served, if possible in the context.
 */
public interface IOutputPrinter {
    
    /**
     * Write the output on this print writer interface. It should always print something, even if it's a warning saying it cannot be printed
     * @param target where to write the output to.
     * @param unicodeSupported if the target can render unicode characters
     */
    void write(PrintWriter target, boolean unicodeSupported);

    String mimeType();

    /**
     * Offer the same output as {@linkplain #write(PrintWriter)} but as a pull based reader.
     * The standard implementation takes care to just call the write function with a buffer.
     * If you however can provide a streaming reading, override this function instead, depending
     * on the consumer, it might be called instead of the write function.
     * @param unicodeSupported if the consumer can render unicode characters
     * @return a reader that produces the same contents as the write function, but in a pull style instead of push. 
     */
    default Reader asReader(boolean unicodeSupported) {
        try (var result = new StringWriter()) {
            try (var resultWriter = new PrintWriter(result)) {
                write(resultWriter, unicodeSupported);
            }
            return new StringReader(result.toString());
        }
        catch (IOException ex) {
            throw new IllegalStateException("StringWriter close should never throw exception", ex);
        }
    }

}
