/** 
 * Copyright (c) 2016, Davy Landman, Paul Klint, Centrum Wiskunde & Informatica (CWI) 
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.ValueWireInputStream.ReaderPosition;
import org.rascalmpl.value.io.binary.util.LinearCircularLookupWindow;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.old.BinaryReader;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.tukaani.xz.XZInputStream;

import com.github.luben.zstd.ZstdInputStream;

import io.usethesource.capsule.TransientMap;
import io.usethesource.capsule.TrieMap_5Bits;

/**
 * Reader for binary serialized IValues and Types.
 *
 */
public class IValueReader implements AutoCloseable {
    private static final TypeFactory tf = TypeFactory.getInstance();
    private static final RascalTypeFactory rtf = RascalTypeFactory.getInstance();
    private final ValueWireInputStream reader;
    private final TypeStore ts;
    private final IValueFactory vf;
    private final boolean legacy;
    private final BinaryReader legacyReader;


    /**
     * this will consume the whole stream, or at least more than needed due to buffering, so don't use the InputStream afterwards
     */
    public IValueReader(InputStream in, IValueFactory vf, TypeStore ts) throws IOException {
        ts.extendStore(RascalValueFactory.getStore());
        this.ts = ts;
        this.vf = vf;
        byte[] currentHeader = new byte[IValueWriter.header.length];
        in.read(currentHeader);
        if (!Arrays.equals(IValueWriter.header, currentHeader)) {
            byte firstByte = currentHeader[0];
            // possible an old binary stream
            firstByte &= (BinaryReader.SHARED_FLAG ^ 0xFF); // remove the possible set shared bit
            if (BinaryReader.BOOL_HEADER <= firstByte && firstByte <= BinaryReader.IEEE754_ENCODED_DOUBLE_HEADER) {
                System.err.println("Old value format used, switching to legacy mode!");
                legacy = true;
                legacyReader = new BinaryReader(vf, ts, new SequenceInputStream(new ByteArrayInputStream(currentHeader), in));
                reader = null;
                return;
            }
            throw new IOException("Unsupported file");
        }
        legacy = false;
        legacyReader = null;

        int compression = in.read();
        switch (compression) {
            case IValueWriter.CompressionHeader.NONE:
                break;
            case IValueWriter.CompressionHeader.GZIP:
                in = new GZIPInputStream(in);
                break;
            case IValueWriter.CompressionHeader.XZ:
                in = new XZInputStream(in);
                break;
            case IValueWriter.CompressionHeader.ZSTD:
                in = new ZstdInputStream(in);
                break;
            default:
                throw new IOException("Unsupported compression in file");
        }

        reader = new ValueWireInputStream(in);
    }
    
    public IValue read() throws IOException {
        if (legacy) {
            return legacyReader.deserialize();
        }
        return read(reader, vf, ts);
    }
    
    @Override
    public void close() throws Exception {
        if (legacy) {
            legacyReader.close();
        }
        else {
            reader.close();
        }
    }
    
    /**
     * In most cases you want the other instance write method.
     * 
     * This static method is only for embedding in nested ValueWriteInputStreams
     * @param typeWindowSize should be the same size as used for writing
     * @param valueWindowSize should be the same size as used for writing
     * @param uriWindowSize should be the same size as used for writing
     */
    public static IValue read(ValueWireInputStream reader, IValueFactory vf, TypeStore ts) throws IOException {
        int typeWindowSize = 0;
        int valueWindowSize = 0;
        int uriWindowSize = 0;
        if (reader.next() != ReaderPosition.MESSAGE_START || reader.message() != IValueIDs.Header.ID) {
            throw new IOException("Missing header at start of stream");
        }
        while (reader.next() != ReaderPosition.MESSAGE_END) {
            switch (reader.field()) {
                case IValueIDs.Header.VALUE_WINDOW: valueWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.TYPE_WINDOW: typeWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.SOURCE_LOCATION_WINDOW: uriWindowSize = reader.getInteger();  break;
            }
        }
        return read(reader, vf, ts, getWindow(typeWindowSize), getWindow(valueWindowSize), getWindow(uriWindowSize));
    }
    

    private static <T> TrackLastRead<T> getWindow(int size) {
        if (size == 0) {
            return new TrackLastRead<T>() {
                @Override
                public void read(T obj) {
                }

                @Override
                public T lookBack(int elements) {
                    throw new IllegalArgumentException();
                }
                
            };
        }
        return new LinearCircularLookupWindow<>(size);
    }
    
    private static IValue read(final ValueWireInputStream reader, final IValueFactory vf, final TypeStore store, TrackLastRead<Type> typeWindow, TrackLastRead<IValue> valueWindow, TrackLastRead<ISourceLocation> uriWindow) throws IOException{

        ReaderStack<Type> tstack = new ReaderStack<>(Type.class, 100);
        ValueReaderStack vstack = new ValueReaderStack(1024);
        while(reader.next() == ReaderPosition.MESSAGE_START){
            int messageID = reader.message();
            if (messageID == IValueIDs.LastValue.ID) {
                if(vstack.size() == 1 && tstack.size() == 0){
                    return vstack.pop();
                }
                throw new IOException("End message before stack was ready to be ended");
            }
            if (IValueIDs.Ranges.TYPES_MIN <= messageID && messageID <= IValueIDs.Ranges.TYPES_MAX) {
                // types
                if (readType(reader, messageID, vf, store, typeWindow, valueWindow, uriWindow, tstack, vstack)) {
                    typeWindow.read(tstack.peek());
                }
            }
            else {
                if (readValue(reader, vf, store, typeWindow, valueWindow, uriWindow, tstack, vstack)) {
                    valueWindow.read(vstack.peek());
                }
            }
        }
        throw new AssertionError("We should be reading until an end message occurs that marks the end of the stream");
    }
    private static boolean readType(final ValueWireInputStream reader, int messageID, final IValueFactory vf, final TypeStore store, final TrackLastRead<Type> typeWindow, final TrackLastRead<IValue> valueWindow, final TrackLastRead<ISourceLocation> uriWindow, final ReaderStack<Type> tstack, final ValueReaderStack vstack) throws IOException{
        switch (messageID) {
            case IValueIDs.BoolType.ID:  
                reader.skipMessage(); // forward to the end
                tstack.push(tf.boolType());
                return false;

            case IValueIDs.DateTimeType.ID:    
                reader.skipMessage();
                tstack.push(tf.dateTimeType());
                return false;

            case IValueIDs.IntegerType.ID:     
                reader.skipMessage(); 
                tstack.push(tf.integerType());
                return false;

            case IValueIDs.NodeType.ID:        
                reader.skipMessage();
                tstack.push(tf.nodeType());
                return false;

            case IValueIDs.NumberType.ID:  
                reader.skipMessage();
                tstack.push(tf.numberType());
                return false;

            case IValueIDs.RationalType.ID:     
                reader.skipMessage();
                tstack.push(tf.rationalType());
                return false;

            case IValueIDs.RealType.ID:        
                reader.skipMessage();
                tstack.push(tf.realType());
                return false;

            case IValueIDs.SourceLocationType.ID:     
                reader.skipMessage();
                tstack.push(tf.sourceLocationType());
                return false;

            case IValueIDs.StringType.ID:     
                reader.skipMessage();
                tstack.push(tf.stringType());
                return false;

            case IValueIDs.ValueType.ID:       
                reader.skipMessage();
                tstack.push(tf.valueType());
                return false;

            case IValueIDs.VoidType.ID:        
                reader.skipMessage();
                tstack.push(tf.voidType());
                return false;

                // Composite types

            case IValueIDs.ADTType.ID: {   
                String name = null;
                boolean backReference = false;

                while (!reader.next().isEnd()) {
                    switch(reader.field()){
                        case IValueIDs.ADTType.NAME:
                            name = reader.getString(); break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; break;
                    }
                }

                assert name != null;

                Type typeParameters = tstack.pop();
                int arity = typeParameters.getArity();
                if(arity > 0){
                    Type targs[] = new Type[arity];
                    for(int i = 0; i < arity; i++){
                        targs[i] = typeParameters.getFieldType(i);
                    }
                    tstack.push(tf.abstractDataType(store, name, targs));
                } else {
                    tstack.push(tf.abstractDataType(store, name));
                }
                return backReference;
            }

            case IValueIDs.AliasType.ID:   {   
                String name = null;
                boolean backReference = false;

                while (!reader.next().isEnd()) {
                    switch(reader.field()){
                        case IValueIDs.AliasType.NAME:
                            name = reader.getString(); break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; break;
                    }
                }

                assert name != null;

                Type typeParameters = tstack.pop();
                Type aliasedType = tstack.pop();

                tstack.push(tf.aliasType(store, name, aliasedType, typeParameters));
                return backReference;
            }

            case IValueIDs.ConstructorType.ID:     {
                String name = null;
                boolean backReference = false;
                while (!reader.next().isEnd()) {
                    switch(reader.field()){
                        case IValueIDs.ConstructorType.NAME:
                            name = reader.getString(); break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; break;
                    }
                }

                assert name != null;

                Type fieldTypes = tstack.pop();
                Type adtType = tstack.pop();

                Type declaredAdt = store.lookupAbstractDataType(name);

                if(declaredAdt != null){
                    adtType = declaredAdt;
                }

                int arity = fieldTypes.getArity();
                String[] fieldNames = fieldTypes.getFieldNames();

                Type fieldTypesAr[] = new Type[arity];

                for(int i = 0; i < arity; i++){
                    fieldTypesAr[i] = fieldTypes.getFieldType(i);
                }

                Type result;
                if(fieldNames == null){
                    result = store.lookupConstructor(adtType, name, tf.tupleType(fieldTypesAr));
                    if(result == null) {
                        result = tf.constructor(store, adtType, name, fieldTypesAr);
                    }
                } else {
                    Object[] typeAndNames = new Object[2*arity];
                    for(int i = 0; i < arity; i++){
                        typeAndNames[2 * i] =  fieldTypesAr[i];
                        typeAndNames[2 * i + 1] = fieldNames[i];
                    }

                    result = store.lookupConstructor(adtType, name, tf.tupleType(typeAndNames));
                    if(result == null){
                        result = tf.constructor(store, adtType, name, typeAndNames);
                    }
                }
                tstack.push(result);
                return backReference;
            }

            // External

            case IValueIDs.FunctionType.ID:    {
                boolean backReference = skipMessageCheckBackReference(reader);

                Type keywordParameterTypes = tstack.pop();
                Type argumentTypes =  tstack.pop();
                Type returnType = tstack.pop();


                tstack.push(rtf.functionType(returnType, argumentTypes, keywordParameterTypes));
                return backReference;
            }

            case IValueIDs.ReifiedType.ID: {
                boolean backReference = skipMessageCheckBackReference(reader);
                Type elemType = tstack.pop();

                elemType = elemType.getFieldType(0);

                tstack.push(rtf.reifiedType(elemType));
                return backReference;
            }

            case IValueIDs.OverloadedType.ID: {
                Integer size = null;
                boolean backReference = false;

                while (!reader.next().isEnd()) {
                    switch (reader.field()){ 
                        case IValueIDs.OverloadedType.SIZE:
                            size =  reader.getInteger();
                            break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                    }
                }

                assert size != null;

                Set<FunctionType> alternatives = new HashSet<FunctionType>(size);
                for(int i = 0; i < size; i++){
                    alternatives.add((FunctionType) tstack.pop());
                }
                tstack.push(rtf.overloadedFunctionType(alternatives));
                return backReference;
            }

            case IValueIDs.NonTerminalType.ID: {
                boolean backReference = skipMessageCheckBackReference(reader);

                IConstructor nt = (IConstructor) vstack.pop();
                tstack.push(rtf.nonTerminalType(nt));
                return backReference;
            }

            case IValueIDs.ListType.ID:    {
                boolean backReference = skipMessageCheckBackReference(reader);

                Type elemType = tstack.pop();

                tstack.push(tf.listType(elemType));
                return backReference;
            }

            case IValueIDs.MapType.ID: {   
                String keyLabel = null;
                String valLabel = null;
                boolean backReference = false;

                while (!reader.next().isEnd()) {
                    switch(reader.field()){
                        case IValueIDs.MapType.KEY_LABEL:
                            keyLabel = reader.getString(); break;
                        case IValueIDs.MapType.VAL_LABEL:
                            valLabel = reader.getString(); break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                    }
                }

                Type valType = tstack.pop();
                Type keyType = tstack.pop();

                if(keyLabel == null){
                    tstack.push(tf.mapType(keyType, valType));
                } else {
                    assert valLabel != null;
                    tstack.push(tf.mapType(keyType, keyLabel, valType, valLabel));
                }
                return backReference;
            }

            case IValueIDs.ParameterType.ID:   {
                String name = null;
                boolean backReference = false;

                while (!reader.next().isEnd()) {
                    switch (reader.field()){ 
                        case IValueIDs.ParameterType.NAME:
                            name = reader.getString();
                            break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                    }
                }
                assert name != null;

                Type bound = tstack.pop();
                tstack.push(tf.parameterType(name, bound));
                return backReference;
            }

            case IValueIDs.SetType.ID: {
                boolean backReference = skipMessageCheckBackReference(reader);
                Type elemType = tstack.pop();

                tstack.push(tf.setType(elemType));
                return backReference;
            }

            case IValueIDs.TupleType.ID: {
                String [] fieldNames = null;

                boolean backReference = false;
                Integer arity = null;

                while (!reader.next().isEnd()) {
                    switch (reader.field()){ 
                        case IValueIDs.TupleType.ARITY:
                            arity =  reader.getInteger(); break;

                        case IValueIDs.TupleType.NAMES:
                            fieldNames = reader.getStrings();
                            break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                    }
                }

                assert arity != null;

                Type[] elemTypes = new Type[arity];
                for(int i = arity - 1; i >= 0; i--){
                    elemTypes[i] = tstack.pop();
                }

                if(fieldNames != null){
                    assert fieldNames.length == arity;
                    tstack.push(tf.tupleType(elemTypes, fieldNames));
                } else {
                    tstack.push(tf.tupleType(elemTypes));
                }
                return backReference;
            }

            case IValueIDs.PreviousType.ID: {
                Integer n = null;
                while (!reader.next().isEnd()) {
                    switch (reader.field()){ 
                        case IValueIDs.PreviousType.HOW_LONG_AGO:
                            n = reader.getInteger();
                            break;
                    }
                }

                assert n != null;

                Type type = typeWindow.lookBack(n);
                if(type == null){
                    throw new RuntimeException("Unexpected type cache miss");
                }
                //System.out.println("Previous type: " + type + ", " + n);
                tstack.push(type);  // do not cache type twice
                return false;
            }
            default:
                throw new IOException("Unexpected new message: " + reader.message());
        }
    }

    private static boolean readValue(final ValueWireInputStream reader, final IValueFactory vf, final TypeStore store, final TrackLastRead<Type> typeWindow, final TrackLastRead<IValue> valueWindow, final TrackLastRead<ISourceLocation> uriWindow, final ReaderStack<Type> tstack, final ValueReaderStack vstack) throws IOException{
        switch (reader.message()) {
            case IValueIDs.BoolValue.ID: return readBoolean(reader, vf, tstack, vstack);
            case IValueIDs.ConstructorValue.ID:    return readConstructor(reader, vf, tstack, vstack, 0);
            case IValueIDs.DateTimeValue.ID: return readDateTime(reader, vf, tstack, vstack);
            case IValueIDs.IntegerValue.ID: return readInteger(reader, vf, tstack, vstack);
            case IValueIDs.ListValue.ID: return readList(reader, vf, tstack, vstack, 0);
            case IValueIDs.SourceLocationValue.ID: return readSourceLocation(reader, vf, uriWindow, vstack);
            case IValueIDs.MapValue.ID: return readMap(reader, vf, vstack, 0);
            case IValueIDs.NodeValue.ID: return readNode(reader, vf, vstack);
            case IValueIDs.RationalValue.ID: return readRational(reader, vf, vstack);
            case IValueIDs.RealValue.ID: return readReal(reader, vf, vstack);
            case IValueIDs.SetValue.ID: return readSet(reader, vf, vstack, 0);
            case IValueIDs.StringValue.ID: return readString(reader, vf, vstack);
            case IValueIDs.TupleValue.ID: return readTuple(reader, vf, vstack);
            case IValueIDs.PreviousValue.ID: return readPreviousValue(reader, valueWindow, vstack);
            default:
                throw new IllegalArgumentException("readValue: " + reader.message());
        }
    }


    private static boolean readPreviousValue(final ValueWireInputStream reader,
            final TrackLastRead<IValue> valueWindow, final ValueReaderStack vstack)
            throws IOException {
        Integer n = null;
        while(!reader.next().isEnd()){
            if(reader.field() == IValueIDs.PreviousValue.HOW_FAR_BACK){
                n =  reader.getInteger();
            }
        }

        assert n != null;

        IValue result = valueWindow.lookBack(n);
        if (result == null) {
            throw new IOException("Unexpected value cache miss");
        }
        vstack.push(result);   
        return false; // Dont cache value twice
    }


    private static boolean readTuple(final ValueWireInputStream reader, final IValueFactory vf,
            final ValueReaderStack vstack) throws IOException {
        Integer len = 0;
        boolean backReference = false;
        while (!reader.next().isEnd()) {
            if(reader.field() == IValueIDs.TupleValue.SIZE){
                len =  reader.getInteger();
            }
            else if (reader.field() == IValueIDs.Common.CAN_BE_BACK_REFERENCED) {
                backReference = true;
            }
        }

        assert len != null;

        vstack.push(vf.tuple(vstack.getChildren(new IValue[len])));
        return backReference;
    }


    private static boolean readString(final ValueWireInputStream reader, final IValueFactory vf,
            final ValueReaderStack vstack) throws IOException {
        String str = null;
        boolean backReference = false;
        while (!reader.next().isEnd()) {
            if(reader.field() == IValueIDs.StringValue.CONTENT){
                str = reader.getString();
            }
            else if (reader.field() == IValueIDs.Common.CAN_BE_BACK_REFERENCED) {
                backReference = true;
            }
        }

        assert str != null;

        vstack.push(vf.string(str));
        return backReference;
    }


    private static boolean readSet(final ValueWireInputStream reader, final IValueFactory vf,
            final ValueReaderStack vstack, int defaultSize) throws IOException {
        int size = defaultSize;
        boolean backReference = false;
        while (!reader.next().isEnd()) {
            if(reader.field() == IValueIDs.SetValue.SIZE){
                size = reader.getInteger();
            }
            else if (reader.field() == IValueIDs.Common.CAN_BE_BACK_REFERENCED) {
                backReference = true;
            }
        }

        vstack.push(vstack.popSet(vf, size));
        return backReference;
    }


    private static boolean readReal(final ValueWireInputStream reader, final IValueFactory vf,
            final ValueReaderStack vstack) throws IOException {
        byte[] bytes = null;
        Integer scale = null;

        boolean backReference = false;
        while (!reader.next().isEnd()) {
            switch(reader.field()){
                case IValueIDs.RealValue.SCALE:
                    scale = reader.getInteger(); break;
                case IValueIDs.RealValue.CONTENT:
                    bytes = reader.getBytes(); break;
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                    backReference = true;
                    break;
            }
        }

        assert bytes != null && scale != null;

        vstack.push(vf.real(new BigDecimal(new BigInteger(bytes), scale).toString())); // TODO: Improve this?
        return backReference;
    }


    private static boolean readRational(final ValueWireInputStream reader, final IValueFactory vf,
            final ValueReaderStack vstack) throws IOException {
        boolean backReference;
        backReference = skipMessageCheckBackReference(reader);

        IInteger denominator = (IInteger) vstack.pop();
        IInteger numerator = (IInteger) vstack.pop();

        vstack.push(vf.rational(numerator, denominator));
        return backReference;
    }


    private static boolean readNode(final ValueWireInputStream reader, final IValueFactory vf,
            final ValueReaderStack vstack) throws IOException {
        String name = null;
        Integer arity = null;
        int annos = 0;
        int kwparams = 0;

        boolean backReference = false;
        while (!reader.next().isEnd()) {
            switch(reader.field()){
                case IValueIDs.NodeValue.NAME: name = reader.getString(); break;
                case IValueIDs.NodeValue.ARITY: arity = reader.getInteger(); break;
                case IValueIDs.NodeValue.KWPARAMS: kwparams = reader.getInteger(); break;
                case IValueIDs.NodeValue.ANNOS: annos = reader.getInteger(); break;
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: backReference = true; break;
            }
        }

        assert name != null && arity != null;

        INode node;
        if(annos > 0){
            TransientMap<String, IValue> annoResult = TrieMap_5Bits.transientOf();
            for(int i = 0; i < annos; i++){
                IValue val = vstack.pop();
                IString ikey = (IString) vstack.pop();
                annoResult.__put(ikey.getValue(),  val);
            }
            node =  vf.node(name, vstack.getChildren(new IValue[arity])).asAnnotatable().setAnnotations(annoResult.freeze());
        } else if(kwparams > 0){
            TransientMap<String, IValue> kwResult = TrieMap_5Bits.transientOf();
            for(int i = 0; i < kwparams; i++){
                IValue val = vstack.pop();
                IString ikey = (IString) vstack.pop();
                kwResult.__put(ikey.getValue(),  val);
            }
            node = vf.node(name, vstack.getChildren(new IValue[arity]), kwResult);
        } else {
            node = vf.node(name, vstack.getChildren(new IValue[arity]));
        }

        vstack.push(node);
        return backReference;
    }


    private static boolean readMap(final ValueWireInputStream reader, final IValueFactory vf,
            final ValueReaderStack vstack, int defaultSize) throws IOException {
        int size = defaultSize;
        boolean backReference = false;
        while (!reader.next().isEnd()) {
            if(reader.field() == IValueIDs.MapValue.SIZE){
                size = reader.getInteger();
            }
            else if (reader.field() == IValueIDs.Common.CAN_BE_BACK_REFERENCED) {
                backReference = true;
            }
        }

        assert size >= 0;

        IMapWriter mw = vf.mapWriter();
        for(int i = 0; i < size; i++){
            IValue val = vstack.pop();
            IValue key = vstack.pop();
            mw.put(key, val);
        }

        vstack.push(mw.done());
        return backReference;
    }


    private static boolean readSourceLocation(final ValueWireInputStream reader, final IValueFactory vf,
            final TrackLastRead<ISourceLocation> uriWindow, final ValueReaderStack vstack)
            throws IOException {
        String scheme = null;
        String authority = "";
        String path = "";
        String query = null;
        String fragment = null;
        int previousURI = -1;
        int offset = -1;
        int length = -1;
        int beginLine = -1;
        int endLine = -1;
        int beginColumn = -1;
        int endColumn = -1;
        while (!reader.next().isEnd()) {
            switch(reader.field()){
                case IValueIDs.SourceLocationValue.PREVIOUS_URI: previousURI = reader.getInteger(); break;
                case IValueIDs.SourceLocationValue.SCHEME: scheme = reader.getString(); break;
                case IValueIDs.SourceLocationValue.AUTHORITY: authority = reader.getString(); break;
                case IValueIDs.SourceLocationValue.PATH: path = reader.getString(); break;
                case IValueIDs.SourceLocationValue.QUERY: query = reader.getString(); break;    
                case IValueIDs.SourceLocationValue.FRAGMENT: fragment = reader.getString(); break;    
                case IValueIDs.SourceLocationValue.OFFSET: offset = reader.getInteger(); break;
                case IValueIDs.SourceLocationValue.LENGTH: length = reader.getInteger(); break;
                case IValueIDs.SourceLocationValue.BEGINLINE: beginLine = reader.getInteger(); break;
                case IValueIDs.SourceLocationValue.ENDLINE: endLine = reader.getInteger(); break;
                case IValueIDs.SourceLocationValue.BEGINCOLUMN: beginColumn = reader.getInteger(); break;
                case IValueIDs.SourceLocationValue.ENDCOLUMN: endColumn = reader.getInteger(); break;
            }
        }
        ISourceLocation loc;
        if (previousURI != -1) {
            loc = uriWindow.lookBack(previousURI);
        } 
        else {
            try {
                loc = vf.sourceLocation(scheme, authority, path, query, fragment);
            } catch (URISyntaxException e) {
                throw new IOException(e);
            }
            uriWindow.read(loc);
        }

        if(beginLine >= 0){
            assert offset >= 0 && length >= 0 && endLine >= 0 && beginColumn >= 0 && endColumn >= 0;
            loc = vf.sourceLocation(loc, offset, length, beginLine, endLine, beginColumn, endColumn);
        } else if (offset >= 0){
            assert length >= 0;
            loc = vf.sourceLocation(loc, offset, length);
        }

        vstack.push(loc);
        return false;
    }


    private static boolean readList(final ValueWireInputStream reader, final IValueFactory vf,
            final ReaderStack<Type> tstack, ValueReaderStack vstack, int defaultSize) throws IOException {
        int size = defaultSize;
        boolean backReference = false;
        while (!reader.next().isEnd()) {
            if(reader.field() == IValueIDs.ListValue.SIZE){
                size = reader.getInteger();
            }
            else if (reader.field() == IValueIDs.Common.CAN_BE_BACK_REFERENCED) {
                backReference = true;
            }
        }

        vstack.push(vstack.popList(vf, size));
        return backReference;
    }


    private static boolean readInteger(final ValueWireInputStream reader, final IValueFactory vf,
            ReaderStack<Type> tstack, final ValueReaderStack vstack) throws IOException {
        Integer small = null;
        byte[] big = null;
        while (!reader.next().isEnd()) {
            switch(reader.field()){
                case IValueIDs.IntegerValue.INTVALUE:  small = reader.getInteger(); break;
                case IValueIDs.IntegerValue.BIGVALUE:    big = reader.getBytes(); break;
            }
        }

        if(small != null){
            vstack.push(vf.integer(small));
        } else if(big != null){
            vstack.push(vf.integer(big));
        } else {
            throw new RuntimeException("Missing field in INT_VALUE");
        }
        return false;
    }


    private static boolean readDateTime(final ValueWireInputStream reader, final IValueFactory vf,
            ReaderStack<Type> tstack, final ValueReaderStack vstack) throws IOException {
        Integer year = null;;
        Integer month = null;
        Integer day = null;

        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer millisecond = null;

        Integer timeZoneHourOffset = null;
        Integer timeZoneMinuteOffset = null;

        while (!reader.next().isEnd()) {
            switch(reader.field()){
                case IValueIDs.DateTimeValue.YEAR: year = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.MONTH: month = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.DAY: day = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.HOUR: hour = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.MINUTE: minute = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.SECOND: second = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.MILLISECOND: millisecond = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.TZ_HOUR: timeZoneHourOffset = reader.getInteger(); break;
                case IValueIDs.DateTimeValue.TZ_MINUTE: timeZoneMinuteOffset = reader.getInteger(); break;
            }
        }


        IValue result;
        if (hour != null && year != null) {
            result = vf.datetime(year, month, day, hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);
        }
        else if (hour != null) {
            result = vf.time(hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);
        }
        else {
            assert year != null;
            result = vf.datetime(year, month, day);
        }
        vstack.push(result);
        return false;
    }


    private static boolean readConstructor(final ValueWireInputStream reader, final IValueFactory vf,
            final ReaderStack<Type> tstack, final ValueReaderStack vstack, int defaultArity)
            throws IOException {
        int arity = defaultArity;
        int annos = 0;
        int kwparams = 0;

        boolean backReference = false;
        while (!reader.next().isEnd()) {
            switch(reader.field()){
                case IValueIDs.ConstructorValue.ARITY: arity =  reader.getInteger(); break;
                case IValueIDs.ConstructorValue.KWPARAMS: kwparams = reader.getInteger(); break;
                case IValueIDs.ConstructorValue.ANNOS: annos = reader.getInteger(); break;
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: backReference = true; break;
            }
        }

        Type consType = tstack.pop();

        IConstructor cons;
        if(annos > 0){
            TransientMap<String, IValue> annosResult = TrieMap_5Bits.transientOf();
            for(int i = 0; i < annos; i++){
                IValue val = vstack.pop();
                IString ikey = (IString) vstack.pop();
                annosResult.__put(ikey.getValue(),  val);
            }
            cons =  vf.constructor(consType, annosResult.freeze(), vstack.getChildren(new IValue[arity]));
        } else if(kwparams > 0){
            TransientMap<String, IValue> kwResult = TrieMap_5Bits.transientOf();
            for(int i = 0; i < kwparams; i++){
                IValue val = vstack.pop();
                IString ikey = (IString) vstack.pop();
                kwResult.__put(ikey.getValue(),  val);
            }
            cons = vf.constructor(consType, vstack.getChildren(new IValue[arity]), kwResult.freeze());
        } else if (arity > 0) {
            cons = vf.constructor(consType, vstack.getChildren(new IValue[arity]));
        }
        else {
            cons = vf.constructor(consType);
        }
        vstack.push(cons);
        return backReference;
    }


    private static boolean readBoolean(final ValueWireInputStream reader, final IValueFactory vf,
            ReaderStack<Type> tstack, final ValueReaderStack vstack) throws IOException {
        boolean value = false;
        while (!reader.next().isEnd()) {
            if(reader.field() == IValueIDs.BoolValue.VALUE){
                value =  true;
            }
        }

        vstack.push(vf.bool(value));
        return false;
    }


    


    private static boolean skipMessageCheckBackReference(final ValueWireInputStream reader) throws IOException {
        boolean backReference = false;
        while (!reader.next().isEnd()) {
            switch(reader.field()){
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                    backReference = true; break;
            }
        }
        return backReference;
    }

    private static class ReaderStack<Elem> {
        protected Elem[] elements;
        protected int sp = 0;

        @SuppressWarnings("unchecked")
        ReaderStack(Class<Elem> c, int capacity){
            elements = (Elem[]) Array.newInstance(c, Math.max(capacity, 16));
        }

        public Elem peek() {
            return elements[sp - 1];
        }

        public void push(Elem elem){
            if(sp == elements.length - 1){
                grow();
            }
            elements[sp] = elem;
            sp++;
        }

        public Elem pop(){
            if(sp > 0){
                sp--;
                return elements[sp];
            }
            throw new RuntimeException("Empty Stack");
        }

        public int size(){
            return sp;
        }

        public Elem[] getChildren(Elem[] target){
            if (target.length == 0) {
                return target;
            }
            int from = sp - target.length;
            if(from >= 0){
                final Elem[] elements = this.elements;
                switch(target.length) {
                    // unrolled arrayCopy for the small arities
                    case 1:
                        target[0] = elements[from + 0];
                        break;
                    case 2:
                        target[0] = elements[from + 0];
                        target[1] = elements[from + 1];
                        break;
                    case 3:
                        target[0] = elements[from + 0];
                        target[1] = elements[from + 1];
                        target[2] = elements[from + 2];
                        break;
                    case 4:
                        target[0] = elements[from + 0];
                        target[1] = elements[from + 1];
                        target[2] = elements[from + 2];
                        target[3] = elements[from + 3];
                        break;

                    default:
                        System.arraycopy(elements, from, target, 0, target.length);
                        break;
                }
                sp = from;
                return target;
            }
            throw new RuntimeException("Empty Stack");
        }

        private void grow() {
            int newSize = (int) Math.min(elements.length * 2L, 0x7FFFFFF7); // max array size used by array list
            assert elements.length <= newSize;
            elements = Arrays.copyOf(elements, newSize);
        }
    }

    private static class ValueReaderStack extends ReaderStack<IValue> {

        public ValueReaderStack(int capacity) {
            super(IValue.class, capacity);
        }

        public IList popList(IValueFactory vf, int size) {
            if (size == 0) {
                return vf.list();
            }

            if(sp < size){
                throw new RuntimeException("Empty Stack");
            }
            sp -= size;
            IListWriter result = vf.listWriter(); 
            result.insert(elements, sp, size);
            return result.done();
        }

        public ISet popSet(IValueFactory vf, int size) {
            if (size == 0) {
                return vf.set();
            }
            if (sp < size) {
                throw new RuntimeException("Empty Stack");
            }
            ISetWriter result = vf.setWriter();
            int oldSp = sp;
            sp -= size;
            for (int i = sp; i < oldSp; i++) {
                result.insert(elements[i]);
            }
            return result.done();
        }
    }
}

