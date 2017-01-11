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
package org.rascalmpl.value.io.binary.wire.binary;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.rascalmpl.value.io.binary.util.TaggedInt;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.binary.util.WindowCacheFactory;
import org.rascalmpl.value.io.binary.wire.FieldKind;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;


public class BinaryWireInputStream implements IWireInputStream {

    private static final byte[] WIRE_VERSION = new byte[] { 1, 0, 0 };
    private final InputStream __stream;
    private final TrackLastRead<String> stringsRead;
    private boolean closed = false;
    private int current;
    private int messageID;
    private int fieldType;
    private int fieldID;
    private String stringValue;
    private int intValue;
    private byte[] bytesValue;
    private int nestedType;
    private String[] stringValues;
    private int[] intValues;
    private int nestedLength;

    public BinaryWireInputStream(InputStream stream) throws IOException {
        this.__stream = stream;
        byte[] header = new byte[WIRE_VERSION.length];
        this.__stream.read(header);
        if (!Arrays.equals(WIRE_VERSION, header)) {
            throw new IOException("Unsupported wire format");
        }
        int stringReadSize = readInteger();
        this.stringsRead = WindowCacheFactory.getInstance().getTrackLastRead(stringReadSize);
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            try {
                __stream.close();
            } finally {
                closed = true;
                WindowCacheFactory.getInstance().returnTrackLastRead(stringsRead);
            }
        }
        else {
            throw new IOException("Already closed");
        }
    }

    private void assertNotClosed() throws IOException {
        if (closed) {
            throw new IOException("Stream already closed"); 
        }
    }

    private int readInteger() throws IOException {
        int b = __stream.read();
        if ((b & 0x80) == 0) {
            return b;
        }

        int result = b & 0x7F;

        b = __stream.read();
        result ^= ((b & 0x7F) << 7);
        if ((b & 0x80) == 0) {
            return result;
        }

        b = __stream.read();
        result ^= ((b & 0x7F) << 14);
        if ((b & 0x80) == 0) {
            return result;
        }

        b = __stream.read();
        result ^= ((b & 0x7F) << 21);
        if ((b & 0x80) == 0) {
            return result;
        }

        b = __stream.read();
        result ^= ((b & 0x7F) << 28);
        if ((b & 0x80) == 0) {
            return result;
        }

        if (b == -1) {
            throw new EOFException();
        }
        throw new IOException("Incorrect integer");
    }

    private String readString() throws IOException {
        int len = readInteger();
        byte[] bytes = readBytes(len);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private byte[] readBytes(int len) throws IOException {
        byte[] result = new byte[len];
        int pos = 0;
        while (pos < len) {
            int read = __stream.read(result, pos, len - pos);
            if (read == -1) {
                throw new EOFException();
            }
            pos += read;
        }
        return result;
    }

    @Override
    public int next() throws IOException {
        assertNotClosed();
        // clear memory
        intValues = null;
        stringValues = null;
        int next = readInteger();
        if (next == 0) {
            return current = MESSAGE_END;
        }
        fieldID = TaggedInt.getOriginal(next);
        fieldType = TaggedInt.getTag(next);
        switch (fieldType) {
            case 0:
                // special case that signals starts of values
                messageID = fieldID;
                return current = MESSAGE_START;
            case FieldKind.NESTED:
                // only case where we don't read the value
                break;
            case FieldKind.STRING:
                stringValue = readString();
                stringsRead.read(stringValue);
                break;
            case FieldKind.INT:
                intValue = readInteger();
                break;
            case FieldKind.PREVIOUS_STR:
                int reference = readInteger();
                fieldType = TaggedInt.getTag(reference);
                assert fieldType == FieldKind.STRING;
                stringValue = stringsRead.lookBack(TaggedInt.getOriginal(reference));
                break;
            case FieldKind.REPEATED:
                int flaggedAmount = readInteger();
                nestedType = TaggedInt.getTag(flaggedAmount);
                nestedLength = TaggedInt.getOriginal(flaggedAmount);
                if (nestedLength == TaggedInt.MAX_ORIGINAL_VALUE) {
                    nestedLength = readInteger();
                }
                switch (nestedType) {
                    case FieldKind.Repeated.BYTES:
                        bytesValue = readBytes(nestedLength);
                        break;
                    case FieldKind.Repeated.INTS:
                        int[] intValues = new int[nestedLength];
                        for (int i = 0; i < nestedLength; i++) {
                            intValues[i] = readInteger();
                        }
                        this.intValues = intValues;
                        break;
                    case FieldKind.Repeated.STRINGS: 
                        String[] stringValues = new String[nestedLength];
                        for (int i = 0; i < nestedLength; i++) {
                            stringValues[i]= readNestedString();
                        }
                        this.stringValues = stringValues;
                        break;
                    case FieldKind.Repeated.NESTEDS:
                        break;
                    default:
                        throw new IOException("Unsupported nested type:" + nestedType);
                }
                break;
            default:
                throw new IOException("Unexpected wire type: " + fieldType);
        }
        return current = FIELD;
    }

    private String readNestedString() throws IOException {
        int reference = readInteger();
        String result;
        if (TaggedInt.getTag(reference) == FieldKind.STRING) {
            // normal string
            result = readString();
            stringsRead.read(result);
        }
        else {
            assert TaggedInt.getTag(reference) == FieldKind.PREVIOUS_STR;
            result = stringsRead.lookBack(TaggedInt.getOriginal(reference));
        }
        return result;
    }


    @Override
    public int current() {
        return current;
    }

    @Override
    public int message() {
        assert current == MESSAGE_START;
        return messageID;
    }

    @Override
    public int field() {
        assert current == FIELD;
        return fieldID;
    }
    
    @Override
    public int getInteger() {
        assert fieldType == FieldKind.INT;
        return intValue;
    }

    @Override
    public String getString() {
        assert fieldType == FieldKind.STRING;
        return stringValue;
    }
    
    
    @Override
    public byte[] getBytes() {
        assert fieldType == FieldKind.REPEATED && nestedType == FieldKind.Repeated.BYTES;
        return bytesValue;
    }
    
    @Override
    public int getFieldType() {
        assert current == FIELD;
        return fieldType;
    }
    
    @Override
    public int getRepeatedType() {
        assert current == FIELD && fieldType == FieldKind.REPEATED;
        return nestedType;
    }
    
    @Override
    public int getRepeatedLength() {
        assert current == FIELD && fieldType == FieldKind.REPEATED;
        return nestedLength;
    }

    @Override
    public String[] getStrings() {
        assert getRepeatedType() == FieldKind.Repeated.STRINGS;
        return stringValues;
    }
    
    @Override
    public int[] getIntegers() {
        assert getRepeatedType() == FieldKind.Repeated.INTS;
        return intValues;
    }
    
    @Override
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
