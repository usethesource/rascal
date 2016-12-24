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
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.value.io.binary.wire.FieldKind;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class RVMWireInputStream implements IRVMWireInputStream {
    
    private final IWireInputStream stream;
    private final Cache<Integer, Integer> intCache;

    public RVMWireInputStream(IWireInputStream stream) {
        this.stream = stream;
        intCache = Caffeine.newBuilder()
            .maximumSize(50_000)
            .build();
    }

    @Override
    public Map<String, Integer> readStringIntegerMap() throws IOException {
        assert stream.getFieldType() == FieldKind.INT && getInteger() == RVMWireOutputStream.MAP_STRING_INT;
        next();
        String[] strings = stream.getStrings();
        next();
        int[] ints = stream.getIntegers();
        Map<String, Integer> result = new HashMap<>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            result.put(strings[i], intCache.get(ints[i], io -> io));
        }
        return result;
    }

    @Override
    public Map<Integer, int[]> readIntIntArrayMap() throws IOException {
        assert stream.getFieldType() == FieldKind.INT && getInteger() == RVMWireOutputStream.MAP_INT_INT_ARRAY;
        next();
        int[] ints = stream.getIntegers();
        Map<Integer, int[]> result = new HashMap<>(ints.length);
        for (int i = 0; i < ints.length; i++) {
            next();
            result.put(ints[i], stream.getIntegers());
        }
        return result;
    }


    @Override
    public int next() throws IOException {
        return stream.next();
    }

    @Override
    public int current() {
        return stream.current();
    }

    @Override
    public int message() {
        return stream.message();
    }

    @Override
    public int field() {
        return stream.field();
    }

    @Override
    public int getFieldType() {
        return stream.getFieldType();
    }

    @Override
    public int getInteger() {
        return stream.getInteger();
    }

    @Override
    public String getString() {
        return stream.getString();
    }

    @Override
    public byte[] getBytes() {
        return stream.getBytes();
    }

    @Override
    public int getRepeatedType() {
        return stream.getRepeatedType();
    }

    @Override
    public int getRepeatedLength() {
        return stream.getRepeatedLength();
    }

    @Override
    public String[] getStrings() {
        return stream.getStrings();
    }

    @Override
    public int[] getIntegers() {
        return stream.getIntegers();
    }

    @Override
    public void skipMessage() throws IOException {
        stream.skipMessage();
    }

    @Override
    public void close() throws IOException {
        stream.close();
        intCache.invalidateAll();
    }


}
