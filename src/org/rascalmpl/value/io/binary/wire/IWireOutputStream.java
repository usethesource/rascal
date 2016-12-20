/** 
 * Copyright (c) 2016, Davy Landman, Centrum Wiskunde & Informatica (CWI) 
 * All rights reserved. 
 *  
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
 *  
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 *  
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.rascalmpl.value.io.binary.wire;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Map;


/**
 * a basic message writer for the IValue wire format
 */
public interface IWireOutputStream extends Closeable, Flushable  {
    /**
     * start a message, always end with a {@linkplain #endMessage()}.
     */
    void startMessage(int messageId) throws IOException;

    void writeField(int fieldId, int value) throws IOException;
    void writeField(int fieldId, byte[] value) throws IOException;
    void writeField(int fieldId, String value) throws IOException;

    void writeField(int fieldId, int[] values) throws IOException;
    void writeField(int fieldId, String[] values) throws IOException;
    void writeField(int fieldId, Map<String, Integer> values) throws IOException;

    /**
     * A nested field signals that next up in the stream, we get a nested message. it has no value of itself.
     */
    void writeNestedField(int fieldId) throws IOException;
    /**
     * Similar to {@linkplain #writeNestedField(int)} , we signal a nested field that repeats fieldId times. Again, the user has to take care of actually writing this many nested messages.
     */
    void writeRepeatedNestedField(int fieldId, int numberOfNestedElements) throws IOException;

    void endMessage() throws IOException;

    default void writeEmptyMessage(int messageId) throws IOException {
        startMessage(messageId);
        endMessage();
    }
}
