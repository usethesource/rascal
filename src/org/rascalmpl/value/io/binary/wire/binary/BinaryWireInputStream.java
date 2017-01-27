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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.rascalmpl.value.io.binary.util.TaggedInt;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.binary.util.WindowCacheFactory;
import org.rascalmpl.value.io.binary.wire.FieldKind;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;


public class BinaryWireInputStream implements IWireInputStream {

    private static final byte[] WIRE_VERSION = new byte[] { 1, 0, 0 };
    private int reads;
    private ByteBuffer buffer;
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
        this(stream, 8*1024);
    }
    public BinaryWireInputStream(InputStream stream, int bufferSize) throws IOException {
        this.__stream = stream;
        buffer = ByteBuffer.allocate(bufferSize);
        buffer.flip();
        byte[] header = readBytes(WIRE_VERSION.length);
        if (!Arrays.equals(WIRE_VERSION, header)) {
            throw new IOException("Unsupported wire format");
        }
        int stringReadSize = decodeInteger();
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

    private ByteBuffer assureAvailable(int required) throws IOException {
        ByteBuffer buffer = this.buffer;
        if (buffer.remaining() < required) {
            reads++;
            if (reads == 10 && buffer.capacity() < 8*1024*1024) {
                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity()*2);
                if (buffer.hasRemaining()) {
                    newBuffer.put(buffer);
                }
                this.buffer = newBuffer;
                buffer = newBuffer;
                reads = 0;
            }
            else {
                if (buffer.hasRemaining()) {
                    buffer.compact();
                }
                else {
                    buffer.clear();
                }
            }
            byte[] backingArray = buffer.array();
            int arrayPos = buffer.arrayOffset() + buffer.position();
            int left = required - arrayPos;
            while (left > 0) {
                int read = __stream.read(backingArray, arrayPos, backingArray.length - arrayPos);
                if (read == -1) {
                    if (arrayPos == 0) {
                        throw new EOFException();
                    }
                    break;
                }
                arrayPos += read;
                left -= read;
            }
            buffer.position(arrayPos - buffer.arrayOffset());
            buffer.flip();
        }
        return buffer;
    }

    private byte[] readBytes(int len) throws IOException {
        ByteBuffer buffer = this.buffer;
        byte[] result = new byte[len];
        if (len <= buffer.remaining()) {
            buffer.get(result, 0, len);
            return result;
        }
        // first drain the buffer
        int pos = 0;
        int toDrain = buffer.remaining();
        if (toDrain > 0) {
            buffer.get(result, pos, toDrain);
            pos += toDrain;
        }
        // then read the rest
        while (pos < len) {
            int remaining = len - pos;
            if (remaining > buffer.capacity()) {
                assert !buffer.hasRemaining();
                // skip the buffer, large read directly into array
                int read = __stream.read(result, pos, remaining);
                if (read == -1) {
                    throw new EOFException();
                }
                pos += read;
            }
            else {
                buffer = assureAvailable(remaining);
                buffer.get(result, pos, remaining);
                pos += remaining;
            }
        }
        return result;
    }
    /*
     * LEB128 decoding (or actually LEB32) of positive and negative integers, negative integers always use 5 bytes, positive integers are compact.
     */
    private int decodeInteger() throws IOException {
        try {
            ByteBuffer buffer = assureAvailable(5);
            // manually unrolling the loop was the fastest for reading, yet not for writing
            byte b = buffer.get();
            if ((b & 0x80) == 0) {
                return b;
            }

            int result = b & 0x7F;

            b = buffer.get();
            result ^= ((b & 0x7F) << 7);
            if ((b & 0x80) == 0) {
                return result;
            }

            b = buffer.get();
            result ^= ((b & 0x7F) << 14);
            if ((b & 0x80) == 0) {
                return result;
            }

            b = buffer.get();
            result ^= ((b & 0x7F) << 21);
            if ((b & 0x80) == 0) {
                return result;
            }

            b = buffer.get();
            result ^= ((b & 0x7F) << 28);
            if ((b & 0x80) == 0) {
                return result;
            }
            throw new IOException("Incorrect integer");
        }
        catch (BufferUnderflowException e) {
            throw new EOFException();
        }
    }

    /*
     * A string is encoded to UTF8 and stored with a prefix of the amount of bytes needed
     */
    private String decodeString() throws IOException {
        int len = decodeInteger();
        byte[] bytes = readBytes(len);
        // this is the fastest way, other paths to a string lead to an extra allocated char array
        return new String(bytes, StandardCharsets.UTF_8);
    }


    @Override
    public int next() throws IOException {
        assertNotClosed();
        // clear memory
        intValues = null;
        stringValues = null;
        int next = decodeInteger();
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
                stringValue = decodeString();
                stringsRead.read(stringValue);
                break;
            case FieldKind.INT:
                intValue = decodeInteger();
                break;
            case FieldKind.PREVIOUS_STR:
                int reference = decodeInteger();
                fieldType = TaggedInt.getTag(reference);
                assert fieldType == FieldKind.STRING;
                stringValue = stringsRead.lookBack(TaggedInt.getOriginal(reference));
                break;
            case FieldKind.REPEATED:
                int flaggedAmount = decodeInteger();
                nestedType = TaggedInt.getTag(flaggedAmount);
                nestedLength = TaggedInt.getOriginal(flaggedAmount);
                if (nestedLength == TaggedInt.MAX_ORIGINAL_VALUE) {
                    nestedLength = decodeInteger();
                }
                switch (nestedType) {
                    case FieldKind.Repeated.BYTES:
                        bytesValue = readBytes(nestedLength);
                        break;
                    case FieldKind.Repeated.INTS:
                        int[] intValues = new int[nestedLength];
                        for (int i = 0; i < nestedLength; i++) {
                            intValues[i] = decodeInteger();
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
        int reference = decodeInteger();
        String result;
        if (TaggedInt.getTag(reference) == FieldKind.STRING) {
            // normal string
            result = decodeString();
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
