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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;

/**
 * A stream that when bytes are read from to it, will read from the provided source, 
 * and decode that as base64 and return the decoded bytes.
 * 
 * Most likely you want to use the {@link StreamingBase64} class to construct an instance.
 */
public class Base64DecodingInputStream extends InputStream {
    private static final int DEC_INPUT_SIZE = 4 * 1024;
    private static final int DEC_OUTPUT_SIZE = 3 * 1024;

    private final Decoder decoder = Base64.getDecoder();
	private final ByteReader source;
    private final byte[] buffer = new byte[DEC_OUTPUT_SIZE]; // base64 produces chunks of 3 bytes per 4 bytes input
    private int consumed = 0;
    private int limit = 0;
    private boolean lastBlock = false;

    public Base64DecodingInputStream(ByteReader source) {
        this.source = source;
    }

    @Override
    public int available() throws IOException {
        if (consumed == limit && !lastBlock) {
            var readBuffer = new byte[DEC_INPUT_SIZE];
            // the Base64 class with the fastest path
            // requires us to fill the buffer completly
            // and we only skip this for the last block
            var read = source.fillBuffer(readBuffer);
            if (read != readBuffer.length) {
                lastBlock = true;
                if (read == -1) {
                    read = 0;
                }
                else {
                    readBuffer = Arrays.copyOf(readBuffer, read);
                }
            }
            if (read > 0) {
                limit = decoder.decode(readBuffer, buffer);
            }
            else {
                limit = 0;
            }
            consumed = 0;
        }
        return limit - consumed;
    }

    @Override
    public int read() throws IOException {
        if (available() == 0) {
            return -1;
        }
        return Byte.toUnsignedInt(buffer[consumed++]);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int remaining = available();
        if (remaining == 0) {
            return -1;
        }
        int toRead = Math.min(remaining, len);
        System.arraycopy(buffer, consumed, b, off, toRead);
        consumed += toRead;
        return toRead;
    }

    @Override
    public void close() throws IOException {
        this.source.close();
    }

    /**
     * Fast path alternative that skips intermediate streams, and streams from source to target as fast as possible
     */
    public static void direct(ByteReader source, OutputStream target) throws IOException {
        var decoder = Base64.getDecoder();
        var inputBuffer = new byte[DEC_INPUT_SIZE];
        var outputBuffer = new byte[DEC_OUTPUT_SIZE]; 
        int read;
        do {
            read = source.fillBuffer(inputBuffer);
            if (read == -1) {
                return;
            }
            if (read != inputBuffer.length) {
                inputBuffer = Arrays.copyOf(inputBuffer, read);
            }
            int toWrite = decoder.decode(inputBuffer, outputBuffer);
            target.write(outputBuffer, 0, toWrite);
        } while (read == inputBuffer.length);
    }
}
