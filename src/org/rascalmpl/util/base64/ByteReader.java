/*
 * Copyright (c) 2025, Swat.engineering
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
package org.rascalmpl.util.base64;

import java.io.IOException;
import java.io.Reader;

/** Read bytes, much like an input stream, but not requiring an input stream */
@FunctionalInterface
public interface ByteReader extends AutoCloseable {
    /** 
     * implementation should fill the whole buffer, unless it's the end of the stream 
     * @return filled bytes or -1 in case of EOF, if less than buffer size, assumed it's the last block, and won't be read from again
     * */
    int fillBuffer(byte[] buffer) throws IOException;

    @Override
    default void close() throws IOException { }



    /** for strings only containing ASCII or ISO_8859_1 characters */
    public static ByteReader fromLatin(String source) {
        return new ByteReader() {
            int pos = 0;
            @Override
            public int fillBuffer(byte[] buffer) {
                if (pos == source.length()) {
                    return -1;
                }
                int toWrite = Math.min(buffer.length, source.length() - pos);
                for (int i = 0; i < toWrite; i++) {
                    buffer[i] = (byte)(source.charAt(i +pos) & 0xFF);
                }
                pos += toWrite;
                return toWrite;
            }
        };
    }

    /** for strings only containing ASCII or ISO_8859_1 characters */
    public static ByteReader fromLatin(Reader source) {
        var intermediateBuffer = new char[4 * 1024];
        return new ByteReader() {
            @Override
            public int fillBuffer(byte[] buffer) throws IOException {
                int filled = 0;
                int remaining = buffer.length;
                while (filled < buffer.length) {
                    int toRead = Math.min(remaining, intermediateBuffer.length);
                    int read = source.read(intermediateBuffer, 0, toRead);
                    if (read == -1) {
                        break;
                    }
                    for (int i = 0; i < read; i++) {
                        buffer[filled + i] = (byte)(intermediateBuffer[i] & 0xFF);
                    }
                    filled += read;
                    remaining -= read;
                }
                if (filled == 0) {
                    return -1;
                }
                return filled;
            }
            
        };
    }

    public static ByteReader fromBytes(byte[] source) {
        return new ByteReader() {
            int pos = 0;
            @Override
            public int fillBuffer(byte[] buffer) throws IOException {
                if (pos == source.length) {
                    return -1;
                }
                int toRead = Math.min(source.length - pos, buffer.length);
                System.arraycopy(source, pos, buffer, 0, toRead);
                pos += toRead;
                return toRead;
            }
        };
    }
}
