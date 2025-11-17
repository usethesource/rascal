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
 */package org.rascalmpl.util.base64;

import java.io.IOException;
import java.io.Writer;

/**
 * Write base64 encoding output. It produces ascii characters, and the internal API always produces bytes instead of chars.
 * For some output this avoids an extra intermediate conversion.
 */
public interface Base64CharWriter extends AutoCloseable {
    /** an array of ascii characters that correspond to the base64 encoding of a block */
    void write(byte[] c, int length) throws IOException;
    @Override
    default void close() throws IOException {}

    static Base64CharWriter latinBytesTo(Writer target) {
        return (buffer, length) -> {
            // single char writes are translated to char array, so it's better 
            // to also our own char array and pass that
            // it's the fastest path that has the last amount of copies.
            // for writer the fast path is to create a 
            char[] copy = new char[length];
            for (int i = 0; i < length; i++) {
                copy[i] = (char)(buffer[i] & 0xFF);
            }
            target.write(copy, 0, length);
        };
    }

    static Base64CharWriter latinBytesTo(StringBuilder target) {
        return (buffer, length) -> {
            // single chars in string builders are pass through to an internal buffer
            // so that is the fastest way to avoid intermediate arrays
            for (int i = 0; i < length; i++) {
                target.append((char)(buffer[i] & 0xFF));
            }
        };
    }
}