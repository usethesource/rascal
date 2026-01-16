/*
 * Copyright (c) 2015-2025, NWO-I CWI and Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.ideservices;

import java.io.IOException;
import java.util.List;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.library.lang.json.internal.JsonValueReader;
import org.rascalmpl.library.lang.json.internal.JsonValueWriter;
import org.rascalmpl.util.base64.StreamingBase64;
import org.rascalmpl.values.IRascalValueFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ICollection;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IDateTime;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.INumber;
import io.usethesource.vallang.IRational;
import io.usethesource.vallang.IReal;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.binary.stream.IValueInputStream;
import io.usethesource.vallang.io.binary.stream.IValueOutputStream;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * This class can be used to configure Gson to automatically encode and decode IValues to/from JSON.
 * Only primitive IValues are supported; collections, tuples, and nodes are not supported as these values cannot be decoded automatically
 */
public class GsonUtils {
    private static final JsonValueWriter writer = new JsonValueWriter();
    private static final JsonValueReader reader = new JsonValueReader(IRascalValueFactory.getInstance(), new TypeStore(), new NullRascalMonitor(), null);
    private static final TypeFactory tf = TypeFactory.getInstance();
    
    private static final List<TypeMapping> typeMappings;

    /* Mappings from Java types to `vallang` types are declared here.
     * Subtypes should be declared before their supertypes; e.g., `Number` and `Value` appear last.
     */
    static {
        writer.setDatesAsInt(true);
        typeMappings = List.of(
            new TypeMapping(IBool.class, tf.boolType()),
            new TypeMapping(ICollection.class), // IList, IMap, ISet
            new TypeMapping(IConstructor.class),
            new TypeMapping(IDateTime.class, tf.dateTimeType()),
            new TypeMapping(IExternalValue.class),
            new TypeMapping(IInteger.class, tf.integerType()),
            new TypeMapping(INode.class),
            new TypeMapping(IRational.class, tf.rationalType()),
            new TypeMapping(IReal.class, tf.realType()),
            new TypeMapping(ISourceLocation.class, tf.sourceLocationType()),
            new TypeMapping(IString.class, tf.stringType()),
            new TypeMapping(ITuple.class),
            new TypeMapping(INumber.class, tf.numberType()),
            new TypeMapping(IValue.class, tf.valueType())
        );
    }

    public static enum ComplexTypeMode {
        /**
         * All values are serialized as JSON objects. Automatic deserialization is only supported for primitive types (`bool`,
         * `datetime`, `int`, `rat`, `real`, `loc`, `str`, `num`); more complex types cannot be automatically deserialized as
         * the type is not available at deserialization time.
         * 
         * Rationals are wrapped in an object with `rat` as key, to avoid an ambiguity between a rational's JSON representation
         * (a list) and a list as a sole argument on the JSON-RPC level. 
         */
        ENCODE_AS_JSON_OBJECT,

        /**
         * All values are serialized as a (binary) Base64-encoded string. An appropriate {@link TypeStore} must be provided for
         * deserialization with {@link ComplexTypeMode#base64Decode}.
         */
        ENCODE_AS_BASE64_STRING,

        /**
         * All values are serialized as a string.
         */
        ENCODE_AS_STRING,

        /**
         * Only primitive types are supported; more complex types are neither serialized nor deserialized.
         */
        NOT_SUPPORTED
    }

    private static class TypeMapping {
        private final Class<?> clazz;
        private final @Nullable Type type;
        private final boolean isPrimitive;

        public TypeMapping(Class<?> clazz) {
            this(clazz, null);
        }

        public TypeMapping(Class<?> clazz, Type type) {
            this(clazz, type, type != null);
        }
        
        public TypeMapping(Class<?> clazz, Type type, boolean isPrimitive) {
            this.clazz = clazz;
            this.type = type;
            this.isPrimitive = isPrimitive;
        }

        public boolean supports(Class<?> incoming) {
            return clazz.isAssignableFrom(incoming);
        }

        public <T> TypeAdapter<T> createAdapter(ComplexTypeMode complexTypeMode) {
            if (isPrimitive) {
                var needsWrapping = complexTypeMode == ComplexTypeMode.ENCODE_AS_JSON_OBJECT && type.isSubtypeOf(tf.rationalType());
                return new TypeAdapter<T>() {
                    @Override
                    public void write(JsonWriter out, T value) throws IOException {
                        if (needsWrapping) {
                            out.beginObject();
                            out.name("rat");
                        }
                        writer.write(out, (IValue) value);
                        if (needsWrapping) {
                            out.endObject();
                        }
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public T read(JsonReader in) throws IOException {
                        if (needsWrapping) {
                            in.beginObject();
                            in.nextName();
                        }
                        var ret = (T) reader.read(in, type);
                        if (needsWrapping) {
                            in.endObject();
                        }
                        return ret;
                    }
                };
            }
            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    switch (complexTypeMode) {
                        case ENCODE_AS_JSON_OBJECT:
                            writer.write(out, (IValue) value);
                            break;
                        case ENCODE_AS_BASE64_STRING:
                            out.value(base64Encode((IValue) value));
                            break;
                        case ENCODE_AS_STRING:
                            out.value(((IValue) value).toString());
                            break;
                        case NOT_SUPPORTED:
                            throw new IOException("Cannot write complex type " + value);
                        default:
                            throw new IllegalArgumentException("Unsupported complex type mode " + complexTypeMode);
                    }
                }

                @Override
                public T read(JsonReader in) throws IOException {
                    throw new IOException("Cannot handle complex type");
                }
            };
        }
    }

    public static void configureGson(GsonBuilder builder) {
        configureGson(builder, ComplexTypeMode.ENCODE_AS_JSON_OBJECT);
    }

    public static void configureGson(GsonBuilder builder, ComplexTypeMode complexTypeMode) {
        builder.registerTypeAdapterFactory(new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                var rawType = type.getRawType();
                if (!IValue.class.isAssignableFrom(rawType)) {
                    return null;
                }
                return typeMappings.stream()
                    .filter(m -> m.supports(rawType))
                    .findFirst()
                    .map(m -> m.<T>createAdapter(complexTypeMode))
                    .orElse(null);
            }
        });
    }

    public static String base64Encode(IValue value) {
        var builder = new StringBuilder();
        try (var encoder = StreamingBase64.encode(builder);
             var out = new IValueOutputStream(encoder, IRascalValueFactory.getInstance())) {
            out.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T extends IValue> T base64Decode(String string, TypeStore ts) {
        try (var decoder = StreamingBase64.decode(string);
             var in = new IValueInputStream(decoder, IRascalValueFactory.getInstance(), () -> ts)) {
            return (T) in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
