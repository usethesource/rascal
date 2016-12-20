/** 
 * Copyright (c) 2016, paulklint, Centrum Wiskunde & Informatica (CWI) 
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
import java.nio.LongBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.rascalmpl.value.io.binary.wire.FieldKind;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;
import org.rascalmpl.value.io.binary.wire.IWireOutputStream;

public class RVMWireExtensions {

    public static void writeMap(IWireOutputStream out, int fieldId, Map<String, Integer> map) throws IOException{
        int n = map.size();
        String[] keys = new String[n];
        int[] values = new int[n];
        
        int i = 0;
        for(Entry<String, Integer> entry : map.entrySet()){
            keys[i] = entry.getKey();
            values[i] = entry.getValue();
            i++;
        }
        out.writeField(fieldId, keys);
        out.writeField(fieldId, values);
    }
    
    public static Map<String, Integer> readMap(IWireInputStream in) throws IOException{
        assert in.getFieldType() == FieldKind.REPEATED && in.getRepeatedType() == FieldKind.STRING;
        String[] keys = in.getStrings();
        int fieldId = in.field();
        in.next();
        assert in.field() == fieldId;
        int[] values = in.getIntegers();
        assert keys.length == values.length;
        Map<String, Integer> map = new HashMap<>();
        for(int i = 0; i < keys.length; i++){
            map.put(keys[i], values[i]);
        }
        return map;     
    }

    public static void writeLongs(IWireOutputStream out, int fieldId, long[] longs) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(longs.length & Long.BYTES);
        buf.asLongBuffer().put(longs);
        out.writeField(fieldId, buf.array());
    }

    public static long[] readLongs(IWireInputStream in) throws IOException{
        assert in.getFieldType() == FieldKind.BYTES;
        ByteBuffer buf = ByteBuffer.wrap(in.getBytes());
        return buf.asLongBuffer().array();
    }
}
