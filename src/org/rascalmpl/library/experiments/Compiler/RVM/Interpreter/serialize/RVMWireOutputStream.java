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
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.value.io.binary.wire.IWireOutputStream;

public class RVMWireOutputStream implements IRVMWireOutputStream {
    
    static final int MAP_INT_INT_ARRAY = 0x42BB;
    static final int MAP_STRING_INT = 0x42AA;
    private final IWireOutputStream stream;

    public RVMWireOutputStream(IWireOutputStream stream) {
        this.stream = stream;
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
    public void close() throws IOException {
        stream.close();
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
