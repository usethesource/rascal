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
package org.rascalmpl.value.io.binary;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;

import org.rascalmpl.value.io.binary.util.FieldKind;
import org.rascalmpl.value.io.binary.util.OpenAddressingLastWritten;
import org.rascalmpl.value.io.binary.util.TaggedInt;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;

import com.google.protobuf.CodedOutputStream;

public class ValueWireOutputStream implements Closeable, Flushable {

    private static final byte[] WIRE_VERSION = new byte[] { 1, 0, 0 };
    private boolean closed = false;
    private final CodedOutputStream stream;
    private final OutputStream __stream;
    private final TrackLastWritten<String> stringsWritten;

    public ValueWireOutputStream(OutputStream stream, int stringSharingWindowSize) throws IOException {
        assert stringSharingWindowSize > 0;
        this.__stream = stream;
        this.__stream.write(WIRE_VERSION);
        this.stream = CodedOutputStream.newInstance(stream);
        this.stream.writeUInt32NoTag(stringSharingWindowSize);
        this.stringsWritten = new OpenAddressingLastWritten<String>(stringSharingWindowSize) {
            @Override
            protected boolean equals(String a, String b) {
                return a.equals(b);
            }

            @Override
            protected int hash(String obj) {
                return obj.hashCode();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            try (OutputStream closeJava = __stream) {
                flush();
            }
            finally {
                closed = true;
            }
        }
    }
    
    private void assertNotClosed() throws IOException {
        if (closed) {
            throw new IOException("Stream already closed"); 
        }
    }

    @Override
    public void flush() throws IOException {
        assertNotClosed();
        stream.flush();
        __stream.flush();
    }

    private void writeFieldTag(final int fieldId, final int type) throws IOException {
        stream.writeUInt32NoTag(TaggedInt.make(fieldId, type));
    }

    public void startMessage(int messageId) throws IOException {
        assertNotClosed();
        writeFieldTag(messageId, 0);
    }

    public void writeField(int fieldId, String value) throws IOException {
        assertNotClosed();
        int alreadyWritten = stringsWritten.howLongAgo(value);
        if (alreadyWritten != -1) {
            writeFieldTag(fieldId, FieldKind.PREVIOUS_STR);
            stream.writeUInt32NoTag(TaggedInt.make(alreadyWritten, FieldKind.STRING));
        }
        else {
            writeFieldTag(fieldId, FieldKind.STRING);
            stream.writeStringNoTag(value);
            stringsWritten.write(value);
        }
    }
    
    public void writeField(int fieldId, int value) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.INT);
        stream.writeUInt32NoTag(value);
    }
    
    public void writeField(int fieldId, byte[] value) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.BYTES);
        stream.writeByteArrayNoTag(value);
    }
    
    public void writeField(int fieldId, int[] values) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.REPEATED);
        stream.writeUInt32NoTag(TaggedInt.make(values.length, FieldKind.INT));
        for (int v : values) {
            stream.writeUInt32NoTag(v);
        }
    }
    
    public void writeField(int fieldId, String[] values) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.REPEATED);
        stream.writeUInt32NoTag(TaggedInt.make(values.length, FieldKind.STRING));
        for (String s : values) {
            int alreadyWritten = stringsWritten.howLongAgo(s);
            if (alreadyWritten != -1) {
                stream.writeUInt32NoTag(TaggedInt.make(alreadyWritten, FieldKind.STRING));
            }
            else {
                stream.writeUInt32NoTag(0);
                stream.writeStringNoTag(s);
                stringsWritten.write(s);
            }
        }
    }
    
    public void writeNestedField(int fieldId) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.NESTED);
        
    }
    public void writeRepeatedNestedField(int fieldId, int numberOfNestedElements) throws IOException {
        assertNotClosed();
        writeFieldTag(fieldId, FieldKind.REPEATED);
        stream.writeUInt32NoTag(TaggedInt.make(numberOfNestedElements, FieldKind.NESTED));
    }
    public void endMessage() throws IOException {
        assertNotClosed();
        writeFieldTag(0, 0);
    }
    
    public void writeEmptyMessage(int messageId) throws IOException {
        startMessage(messageId);
        endMessage();
    }
}
