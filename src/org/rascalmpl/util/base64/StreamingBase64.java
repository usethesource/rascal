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
import java.io.Reader;
import java.io.Writer;


/**
 * Streaming translations of base64 contents
 */
public class StreamingBase64 {

    /**
     * Create an OutputStream that on writing bytes, encodes them to the target writer
     * 
     * The OutputStream needs to be closed before the target can be consumed
     */
    public static OutputStream encode(Writer target) {
        return encode(target, true);
    }

    /**
     * Create an OutputStream that on writing bytes, encodes them to the target writer, and you can disable the optional `=` padding characters
     * 
     * The OutputStream needs to be closed before the target can be consumed
     */
    public static OutputStream encode(Writer target, boolean padding) {
        return encode(Base64CharWriter.latinBytesTo(target), padding);
    } 

    /**
     * Create an OutputStream that on writing bytes, encodes them to the StringBuilder
     * 
     * The OutputStream needs to be closed before the target can be consumed
     */
    public static OutputStream encode(StringBuilder target) {
        return encode(target, true);
    }

    /**
     * Create an OutputStream that on writing bytes, encodes them to the StringBuilder, and you can disable the optional `=` padding characters
     * 
     * The OutputStream needs to be closed before the target can be consumed
     */
    public static OutputStream encode(StringBuilder target, boolean padding) {
        return encode(Base64CharWriter.latinBytesTo(target), padding);
    }
    
    private static OutputStream encode(Base64CharWriter target, boolean padding) {
        return new Base64EncodingOutputStream(target, padding);
    }

    /**
     * Utility function that encodes a stream of bytes directly to a target
     */
    public static void encode(InputStream source, StringBuilder target, boolean padding) throws IOException {
        Base64EncodingOutputStream.direct(source, Base64CharWriter.latinBytesTo(target), padding);
    }


    /**
     * Utility function that encodes a stream of bytes directly to a target
     */
    public static void encode(InputStream source, Writer target, boolean padding) throws IOException {
        Base64EncodingOutputStream.direct(source, Base64CharWriter.latinBytesTo(target), padding);
    }

    /**
     * Do a streaming base64 decode, reading base64 string while the inputstream is consumed
     */
    public static InputStream decode(Reader source) {
        return decode(ByteReader.fromLatin(source));
    }

    /**
     * Do a streaming base64 decode, reading base64 string while the inputstream is consumed
     */
    public static InputStream decode(String source) {
        return decode(ByteReader.fromLatin(source));
    }

    /**
     * Do a streaming base64 decode, reading base64 string (encoded as bytes) while the inputstream is consumed
     */
    public static InputStream decode(byte[] source) {
        return decode(ByteReader.fromBytes(source));
    }

    private static InputStream decode(ByteReader source) {
        return new Base64DecodingInputStream(source);
    }

    /** fast-path function to write the decoded bytes to a outputstream */
    public static void decode(Reader source, OutputStream target) throws IOException {
        Base64DecodingInputStream.direct(ByteReader.fromLatin(source), target);
    }

    /** fast-path function to write the decoded bytes to a outputstream */
    public static void decode(String source, OutputStream target) throws IOException {
        Base64DecodingInputStream.direct(ByteReader.fromLatin(source), target);
    }

    /** fast-path function to write the decoded bytes to a outputstream */
    public static void decode(byte[] source, OutputStream target) throws IOException {
        Base64DecodingInputStream.direct(ByteReader.fromBytes(source), target);
    }
}
