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
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.binary.message.IValueWriter;
import io.usethesource.vallang.io.binary.util.TrackLastWritten;
import io.usethesource.vallang.io.binary.util.WindowCacheFactory;
import io.usethesource.vallang.io.binary.util.WindowSizes;
import io.usethesource.vallang.io.binary.wire.IWireOutputStream;
import io.usethesource.vallang.type.Type;

public class RVMWireOutputStream implements IRVMWireOutputStream {
    
    static final int MAP_INT_INT_ARRAY = 0x42BB;
    static final int MAP_STRING_INT = 0x42AA;
    static final int HEADER_ID = 0x4242;
    static final int WINDOW_SIZE = 1;
    private final IWireOutputStream stream;
    private final IValueFactory vf;
    private TrackLastWritten<Object> cache;

    public RVMWireOutputStream(IWireOutputStream stream, IValueFactory vf, int nestedValueWindowSize) throws IOException {
        this.stream = stream;
        this.vf = vf;
        stream.startMessage(HEADER_ID);
        stream.writeField(WINDOW_SIZE, nestedValueWindowSize);
        stream.endMessage();
        cache = WindowCacheFactory.getInstance().getTrackLastWrittenReferenceEquality(nestedValueWindowSize);
    }


    @Override
    public void writeFieldStringInt(int fieldId, Map<String, Integer> values) throws IOException {
        writeField(fieldId, MAP_STRING_INT);
        String[] strings = new String[values.size()];
        int[] integers = new int[values.size()];
        int index = 0;
        for (Entry<String, Integer> e : values.entrySet()) {
            strings[index] = e.getKey();
            integers[index] = e.getValue();
            index++;
        }
        writeField(fieldId, strings);
        writeField(fieldId, integers);
    }
    
    @Override
    public void writeFieldIntIntArray(int fieldId, Map<Integer, int[]> values) throws IOException {
        writeField(fieldId, MAP_INT_INT_ARRAY);
        int[] ints = new int[values.size()];
        int[][] arrays = new int[values.size()][];
        int index = 0;
        for (Entry<Integer, int[]> e : values.entrySet()) {
            ints[index] = e.getKey();
            arrays[index] = e.getValue();
            index++;
        }
        writeField(fieldId, ints);
        for (int[] a : arrays) {
            writeField(fieldId, a);
        }
    }
    
    @Override
    public void writeField(int fieldId, IValue value, WindowSizes window) throws IOException {
        writeNestedField(fieldId);
        write(value, window);
    }
    
    @Override
    public void writeField(int fieldId, IValue[] values) throws IOException {
        writeRepeatedNestedField(fieldId, values.length);
        for (IValue v : values) {
            write(v, WindowSizes.NORMAL_WINDOW);
        }
    }
    
    @Override
    public void writeField(int fieldId, IValue[] values, WindowSizes window) throws IOException {
        writeRepeatedNestedField(fieldId, values.length);
        for (IValue v : values) {
            write(v, window);
        }
    }
    
    @Override
    public void writeField(int fieldId, Type value, WindowSizes window) throws IOException {
        writeNestedField(fieldId);
        write(value, window);
    }
    
    @Override
    public void writeField(int fieldId, Type[] values, WindowSizes window) throws IOException {
        writeRepeatedNestedField(fieldId, values.length);
        for (Type v : values) {
            write(v, window);
        }
    }

    private void write(IValue v, WindowSizes window) throws IOException {
        startMessage(CompilerIDs.NestedValue.ID);
        int written = cache.howLongAgo(v);
        if (written != -1) {
            writeField(CompilerIDs.NestedValue.BACK_REFERENCE, written);
        }
        else {
            writeNestedField(CompilerIDs.NestedValue.VALUE);
            IValueWriter.write(stream, vf, window, v);
            cache.write(v);
        }
        endMessage();
    }

    private void write(Type t, WindowSizes window) throws IOException {
        startMessage(CompilerIDs.NestedType.ID);
        int written = cache.howLongAgo(t);
        if (written != -1) {
            writeField(CompilerIDs.NestedType.BACK_REFERENCE, written);
        }
        else {
            writeNestedField(CompilerIDs.NestedType.VALUE);
            IValueWriter.write(stream, vf, window, t);
            cache.write(t);
        }
        endMessage();
    }
    
    @Override
    public void writeField(int fieldId, long[] longs) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(longs.length * Long.BYTES);
        buf.asLongBuffer().put(longs);
        writeField(fieldId, buf.array());
    }

    @Override
    public void close() throws IOException {
        stream.close();
        if (cache != null) {
            WindowCacheFactory.getInstance().returnTrackLastWrittenReferenceEquality(cache);
            cache = null;
        }
    }

    @Override
    public void flush() throws IOException {
        stream.flush();
    }

    @Override
    public void startMessage(int messageId) throws IOException {
        stream.startMessage(messageId);
    }

    @Override
    public void writeField(int fieldId, int value) throws IOException {
        stream.writeField(fieldId, value);
    }

    @Override
    public void writeField(int fieldId, byte[] value) throws IOException {
        stream.writeField(fieldId, value);
    }

    @Override
    public void writeField(int fieldId, String value) throws IOException {
        stream.writeField(fieldId, value);
    }

    @Override
    public void writeField(int fieldId, int[] values) throws IOException {
        stream.writeField(fieldId, values);
    }

    @Override
    public void writeField(int fieldId, String[] values) throws IOException {
        stream.writeField(fieldId, values);
    }

    @Override
    public void writeNestedField(int fieldId) throws IOException {
        stream.writeNestedField(fieldId);
    }

    @Override
    public void writeRepeatedNestedField(int fieldId, int numberOfNestedElements) throws IOException {
        stream.writeRepeatedNestedField(fieldId, numberOfNestedElements);
    }

    @Override
    public void endMessage() throws IOException {
        stream.endMessage();
    }

}
