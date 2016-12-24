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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RVMWireExtensions.ThrowingBiConsumer;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;
import org.rascalmpl.value.io.binary.wire.FieldKind;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;
import org.rascalmpl.value.io.binary.wire.IWireOutputStream;
import org.rascalmpl.value.type.Type;

public class RVMWireExtensions {
    public static void writeLongs(IWireOutputStream out, int fieldId, long[] longs) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(longs.length * Long.BYTES);
        buf.asLongBuffer().put(longs);
        out.writeField(fieldId, buf.array());
    }

    public static long[] readLongs(IWireInputStream in) throws IOException{
        assert in.getFieldType() == FieldKind.REPEATED && in.getRepeatedType() == FieldKind.Repeated.BYTES;
        ByteBuffer buf = ByteBuffer.wrap(in.getBytes());
        long[] result = new long[buf.capacity() / Long.BYTES];
        buf.asLongBuffer().get(result);
        return result;
    }
    
    @FunctionalInterface
    public static interface ThrowingBiConsumer<T, U, E extends Exception> extends BiConsumer<T,U> {
        void throwingAccept(T t, U u) throws E;
        @Override
        default void accept(T t, U u) {
            try {
                throwingAccept(t, u);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FunctionalInterface
    public static interface ThrowingBiFunction<T, U, R, E extends Exception> extends BiFunction<T,U, R> {
        R throwingApply(T t, U u) throws E;
        @Override
        default R apply(T t, U u) {
            try {
                return throwingApply(t, u);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> void nestedOrReference(IWireOutputStream out, int fieldId, T obj, TrackLastWritten<Object> lastWritten,
        ThrowingBiConsumer<IWireOutputStream, T, IOException> writeNested) throws IOException {
        int written = lastWritten.howLongAgo(obj);
        if (written != -1) {
            out.writeField(fieldId, written);
        }
        else {
            out.writeNestedField(fieldId);
            writeNested.throwingAccept(out, obj);
            lastWritten.write(obj);
        }
    }

    public static <T> void nestedOrReferenceRepeated(IWireOutputStream out, int fieldId, Collection<T> objs, TrackLastWritten<Object> lastWritten,
        ThrowingBiConsumer<IWireOutputStream, T, IOException> writeNested) throws IOException {
        out.writeField(fieldId, objs.size());
        for (T o : objs) {
            nestedOrReference(out, fieldId, o, lastWritten, writeNested);
        }
    }
    public static <T> void nestedOrReferenceRepeated(IWireOutputStream out, int fieldId, T[] objs, TrackLastWritten<Object> lastWritten,
        ThrowingBiConsumer<IWireOutputStream, T, IOException> writeNested) throws IOException {
        out.writeField(fieldId, objs.length);
        for (T o : objs) {
            nestedOrReference(out, fieldId, o, lastWritten, writeNested);
        }
    }

    public static <T> T readNestedOrReference(IWireInputStream in, IValueFactory vf, TrackLastRead<Object> lastRead,
        ThrowingBiFunction<IWireInputStream, IValueFactory, T, IOException> reader) throws IOException {
        if (in.getFieldType() == FieldKind.INT) {
            return (T) lastRead.lookBack(in.getInteger());
        }
        else {
           T result = reader.throwingApply(in, vf); 
           lastRead.read(result);
           return result;
        }
    }

    public static <T> ArrayList<T> readNestedOrReferenceRepeatedList(IWireInputStream in, IValueFactory vf, TrackLastRead<Object> lastRead,
        ThrowingBiFunction<IWireInputStream, IValueFactory, T, IOException> reader) throws IOException {
        int arity = in.getInteger();
        ArrayList<T> result = new ArrayList<T>(arity);
        for (int i = 0; i < arity; i++) {
            in.next();
            result.add(readNestedOrReference(in, vf, lastRead, reader));
        }
        return result;
    }
    public static <T> T[] readNestedOrReferenceRepeatedArray(IWireInputStream in, IValueFactory vf, TrackLastRead<Object> lastRead,
        ThrowingBiFunction<IWireInputStream, IValueFactory, T, IOException> reader,
        Function<Integer, T[]> constructArray) throws IOException {
        int arity = in.getInteger();
        T[] result = constructArray.apply(arity);
        for (int i = 0; i < arity; i++) {
            in.next();
            result[i]= readNestedOrReference(in, vf, lastRead, reader);
        }
        return result;
    }

}
