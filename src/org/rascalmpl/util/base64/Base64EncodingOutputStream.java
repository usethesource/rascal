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
import java.util.Base64.Encoder;

/**
 * A stream that when bytes are written to it, will output base64 encoded bytes
 * to the writer interface. 
 * 
 * Most likely you want to use the {@link StreamingBase64} class to construct an instance.
 */
public class Base64EncodingOutputStream extends OutputStream {
    private final Encoder encoder;
    private final Base64CharWriter target;

    private static final int ENC_INPUT_SIZE = 3 * 1024;
    private static final int ENC_OUTPUT_SIZE = 4 * 1024;

    private boolean closed = false;
    private byte[] buffer = new byte[ENC_INPUT_SIZE];
    private int written = 0;

    public Base64EncodingOutputStream(Base64CharWriter writer, boolean padding) {
        encoder = padding ? Base64.getEncoder() : Base64.getEncoder().withoutPadding();
        this.target = writer;
    }

    private void actualFlush() throws IOException {
        byte[] toWrite;
        int available;
        if (written != ENC_INPUT_SIZE) {
            // last block
            toWrite = new byte[((written / 3) + 2) * 4];
            available = encoder.encode(Arrays.copyOf(buffer, written), toWrite);
        }
        else {
            toWrite = new byte[ENC_OUTPUT_SIZE];
            available = encoder.encode(buffer, toWrite);
        }
        written = 0;
        target.write(toWrite, available);
    }

    private void flushIfNeeded() throws IOException {
        if (written == ENC_INPUT_SIZE) {
            actualFlush();
        }
    }

    @Override
    public void write(int b) throws IOException {
        buffer[written++] = (byte) (b & 0xFF);
        flushIfNeeded();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        int copied = 0;
        while (copied < len) {
            int toWrite = Math.min(len - copied, ENC_INPUT_SIZE - written);
            System.arraycopy(b, off + copied, buffer, written, toWrite);
            copied += toWrite;
            written += toWrite;
            flushIfNeeded();
        }
    }

    @Override
    public void flush() throws IOException {
        // ignore, we only flush if the buffer is full
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        actualFlush();
        this.target.close();
    }


    /**
     * Fast path that skips intermediate streams and in blocks streams from source to target
     */
    public static void direct(InputStream source, Base64CharWriter target, boolean padding) throws IOException {
        var encoder = padding ? Base64.getEncoder() : Base64.getEncoder().withoutPadding();
        var inputBuffer = new byte[ENC_INPUT_SIZE];
        var outputBuffer = new byte[ENC_OUTPUT_SIZE];
        int read;
        do {
            // we have to encode full blocks
            // else we get padding chars
            int blockRead;
            read = 0;
            while (read != ENC_INPUT_SIZE && (blockRead = source.read(inputBuffer, read, ENC_INPUT_SIZE - read)) != -1) {
                read += blockRead;
            }
            if (read == 0) {
                return;
            }
            if (read != inputBuffer.length) {
                // last block
                inputBuffer = Arrays.copyOf(inputBuffer, read);
            }
            int toWrite = encoder.encode(inputBuffer, outputBuffer);
            target.write(outputBuffer, toWrite);
        } while (read == ENC_INPUT_SIZE);
    }

}
