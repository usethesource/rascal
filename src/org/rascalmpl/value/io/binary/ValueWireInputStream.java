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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.rascalmpl.value.io.binary.util.FieldKind;
import org.rascalmpl.value.io.binary.util.LinearCircularLookupWindow;
import org.rascalmpl.value.io.binary.util.TaggedInt;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;

public class ValueWireInputStream implements Closeable {

    public static enum ReaderPosition {
        MESSAGE_START,
        FIELD,
        MESSAGE_END {
            @Override
            public boolean isEnd() {
                return true;
            }
        };

        public boolean isEnd() {
            return false;
        }
    }

    private static final byte[] WIRE_VERSION = new byte[] { 1, 0, 0 };
    private final InputStream __stream;
    private final LinearCircularLookupWindow<String> stringsRead;
    private CodedInputStream stream;
    private boolean closed = false;
    private ReaderPosition current;
    private int messageID;
    private int fieldType;
    private int fieldID;
    private String stringValue;
    private int intValue;
    private byte[] bytesValue;
    private int nestedType;
    private String[] stringValues;
    private int[] intValues;

    public ValueWireInputStream(InputStream stream) throws IOException {
        this.__stream = stream;
        byte[] header = new byte[WIRE_VERSION.length];
        this.__stream.read(header);
        if (!Arrays.equals(WIRE_VERSION, header)) {
            throw new IOException("Unsupported wire format");
        }
        this.stream = CodedInputStream.newInstance(stream);
        int stringReadSize = this.stream.readRawVarint32();
        this.stringsRead = new LinearCircularLookupWindow<>(stringReadSize);
        this.stream.setSizeLimit(Integer.MAX_VALUE); 
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            __stream.close();
        }
        else {
            throw new IOException("Already closed");
        }
    }

    public ReaderPosition next() throws IOException {
        intValues = null;
        stringValues = null;
        int next;
        try {
            next = stream.readRawVarint32();
        } 
        catch (InvalidProtocolBufferException e) {
            throw new EOFException();
        }
        if (next == 0) {
            return current = ReaderPosition.MESSAGE_END;
        }
        fieldID = TaggedInt.getOriginal(next);
        fieldType = TaggedInt.getTag(next);
        switch (fieldType) {
            case 0:
                // special case that signals starts of values
                messageID = fieldID;
                return current = ReaderPosition.MESSAGE_START;
            case FieldKind.STRING:
                stream.resetSizeCounter();
                stringValue = stream.readString();
                stringsRead.read(stringValue);
                stream.resetSizeCounter();
                break;
            case FieldKind.INT:
                intValue = stream.readRawVarint32();
                break;
            case FieldKind.BYTES:
                stream.resetSizeCounter();
                bytesValue = stream.readByteArray();
                break;
            case FieldKind.PREVIOUS_STR:
                int reference = stream.readRawVarint32();
                fieldType = TaggedInt.getTag(reference);
                assert fieldType == FieldKind.STRING;
                stringValue = stringsRead.lookBack(TaggedInt.getOriginal(reference));
                break;
            case FieldKind.REPEATED:
                stream.resetSizeCounter();
                int flaggedAmount = (int) stream.readRawVarint32();
                nestedType = TaggedInt.getTag(flaggedAmount);
                int nestedLength = TaggedInt.getOriginal(flaggedAmount);
                switch (nestedType) {
                    case FieldKind.INT:
                        int[] intValues = new int[nestedLength];
                        for (int i = 0; i < nestedLength; i++) {
                            intValues[i] = stream.readRawVarint32();
                        }
                        this.intValues = intValues;
                        break;
                    case FieldKind.STRING: 
                        String[] stringValues = new String[nestedLength];
                        for (int i = 0; i < nestedLength; i++) {
                            reference = stream.readRawVarint32();
                            if (TaggedInt.getTag(reference) == 0) {
                                // normal string
                                stringValues[i] = stream.readString();
                                stringsRead.read(stringValues[i]);
                            }
                            else {
                                assert TaggedInt.getTag(reference) == FieldKind.STRING;
                                stringValues[i] = stringsRead.lookBack(TaggedInt.getOriginal(reference));
                            }
                        }
                        break;
                }
                stream.resetSizeCounter();
                break;
            default:
                throw new IOException("Unexpected wire type: " + fieldType);
        }
        return current = ReaderPosition.FIELD;
    }


    public ReaderPosition current() {
        return current;
    }

    public int message() {
        assert current == ReaderPosition.MESSAGE_START;
        return messageID;
    }

    public int field() {
        assert current == ReaderPosition.FIELD;
        return fieldID;
    }
    
    public int getInteger() {
        assert fieldType == FieldKind.INT;
        return intValue;
    }

    public String getString() {
        assert fieldType == FieldKind.STRING;
        return stringValue;
    }
    
    
    public byte[] getBytes() {
        assert fieldType == FieldKind.BYTES;
        return bytesValue;
    }
    
    public int getFieldType() {
        assert current == ReaderPosition.FIELD;
        return fieldType;
    }
    
    public int getRepeatedType() {
        assert current == ReaderPosition.FIELD && fieldType == FieldKind.REPEATED;
        return nestedType;
    }

    public String[] getStrings() {
        assert fieldType == FieldKind.REPEATED && nestedType == FieldKind.STRING;
        return stringValues;
    }
    
    public int[] getIntegers() {
        assert fieldType == FieldKind.REPEATED && nestedType == FieldKind.INT;
        return intValues;
    }

    public void skipMessage() throws IOException {
        int toSkip = 1;
        while (toSkip != 0) {
            switch (next()) {
                case MESSAGE_START:
                    toSkip++;
                    break;
                case MESSAGE_END:
                    toSkip--;
                    break;
                default:
                    break;
            }
        }
    }

  
}
