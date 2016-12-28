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
package org.rascalmpl.value.io.binary.message;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Collections;

import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.stream.IValueInputStream;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.binary.util.WindowCacheFactory;
import org.rascalmpl.value.io.binary.wire.IWireInputStream;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import io.usethesource.capsule.ImmutableMap;
import io.usethesource.capsule.TransientMap;
import io.usethesource.capsule.TrieMap_5Bits;

/**
 * An utility class for the {@link IValueInputStream}. Only directly use methods in this class if you have nested IValues in an exisiting {@link IWireInputStream}.
 *
 */
public class IValueReader2 {
    private static final TypeFactory tf = TypeFactory.getInstance();

    /**
     * Read an value from the wire reader. <br/>
     * <br/>
     * In most cases you want to use the {@linkplain IValueInputStream}!
     */
    public static IValue readValue(IWireInputStream reader, IValueFactory vf) throws IOException {
        int typeWindowSize = 0;
        int valueWindowSize = 0;
        int uriWindowSize = 0;
        if (reader.next() != IWireInputStream.MESSAGE_START || reader.message() != IValueIDs.Header.ID) {
            throw new IOException("Missing header at start of stream");
        }
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch (reader.field()) {
                case IValueIDs.Header.VALUE_WINDOW: valueWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.TYPE_WINDOW: typeWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.SOURCE_LOCATION_WINDOW: uriWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.VALUE: {
                    IValueReader2 valueReader = new IValueReader2(vf, new TypeStore(), typeWindowSize, valueWindowSize, uriWindowSize);
                    try {
                        IValue result = valueReader.readValue(reader);
                        reader.skipMessage();
                        return result;
                    } finally {
                        valueReader.done();
                    }
                }
                default:
                    reader.skipNestedField();
                    break;
            }
        }
        throw new IOException("Missing Value in the stream");
    }

    /**
     * Read an type from the wire reader. 
     */
    public static Type readType(IWireInputStream reader, IValueFactory vf) throws IOException {
        int typeWindowSize = 0;
        int valueWindowSize = 0;
        int uriWindowSize = 0;
        if (reader.next() != IWireInputStream.MESSAGE_START || reader.message() != IValueIDs.Header.ID) {
            throw new IOException("Missing header at start of stream");
        }
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch (reader.field()) {
                case IValueIDs.Header.VALUE_WINDOW: valueWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.TYPE_WINDOW: typeWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.SOURCE_LOCATION_WINDOW: uriWindowSize = reader.getInteger();  break;
                case IValueIDs.Header.TYPE: {
                    IValueReader2 valueReader = new IValueReader2(vf, new TypeStore(), typeWindowSize, valueWindowSize, uriWindowSize);
                    try {
                        Type result = valueReader.readType(reader);
                        reader.skipMessage();
                        return result;
                    } finally {
                        valueReader.done();
                    }
                }
                default:
                    reader.skipNestedField();
                    break;
            }
        }
        throw new IOException("Missing Type in the stream");
    }

    private IValueReader2(IValueFactory vf, TypeStore store, int typeWindowSize, int valueWindowSize, int uriWindowSize) {
        WindowCacheFactory windowFactory = WindowCacheFactory.getInstance();
        typeWindow = windowFactory.getTrackLastRead(typeWindowSize);
        valueWindow = windowFactory.getTrackLastRead(valueWindowSize);
        uriWindow = windowFactory.getTrackLastRead(uriWindowSize);
        this.vf = vf;
        this.store = store;
    }

    private void done() {
        WindowCacheFactory windowFactory = WindowCacheFactory.getInstance();
        windowFactory.returnTrackLastRead(typeWindow);
        windowFactory.returnTrackLastRead(valueWindow);
        windowFactory.returnTrackLastRead(uriWindow);
    }
    private final IValueFactory vf;
    private final TypeStore store;
    private final TrackLastRead<Type> typeWindow;
    private final TrackLastRead<IValue> valueWindow;
    private final TrackLastRead<ISourceLocation> uriWindow;
    
    private Type readType(final IWireInputStream reader) throws IOException{
        reader.next();
        switch (reader.message()) {
            case IValueIDs.BoolType.ID:  
                reader.skipMessage(); // forward to the end
                return tf.boolType();

            case IValueIDs.DateTimeType.ID:    
                reader.skipMessage();
                return tf.dateTimeType();

            case IValueIDs.IntegerType.ID:     
                reader.skipMessage(); 
                return tf.integerType();

            case IValueIDs.NodeType.ID:        
                reader.skipMessage();
                return tf.nodeType();

            case IValueIDs.NumberType.ID:  
                reader.skipMessage();
                return tf.numberType();

            case IValueIDs.RationalType.ID:     
                reader.skipMessage();
                return tf.rationalType();

            case IValueIDs.RealType.ID:        
                reader.skipMessage();
                return tf.realType();

            case IValueIDs.SourceLocationType.ID:     
                reader.skipMessage();
                return tf.sourceLocationType();

            case IValueIDs.StringType.ID:     
                reader.skipMessage();
                return tf.stringType();

            case IValueIDs.ValueType.ID:       
                reader.skipMessage();
                return tf.valueType();

            case IValueIDs.VoidType.ID:        
                reader.skipMessage();
                return tf.voidType();

                // Composite types

            case IValueIDs.ADTType.ID: {   
                String name = null;
                boolean backReference = false;
                Type typeParam = null;

                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch(reader.field()){
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.ADTType.NAME:
                            name = reader.getString(); 
                            break;
                        case IValueIDs.ADTType.TYPE_PARAMS:
                            typeParam = readType(reader);
                            break;
                        default:
                            reader.skipNestedField();
                            break;
                    }
                }

                assert name != null && typeParam != null;

                return returnAndStore(backReference, typeWindow, tf.abstractDataTypeFromTuple(store, name, typeParam));
            }

            case IValueIDs.AliasType.ID:   {   
                String name = null;
                boolean backReference = false;
                Type typeParameters = null;
                Type aliasedType = null;

                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch(reader.field()){
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.AliasType.NAME:
                            name = reader.getString(); 
                            break;
                        case IValueIDs.AliasType.ALIASED:
                            aliasedType = readType(reader);
                            break;
                        case IValueIDs.AliasType.TYPE_PARAMS:
                            typeParameters = readType(reader);
                            break;
                        default:
                            reader.skipNestedField();
                            break;
                    }
                }

                assert name != null;

                return returnAndStore(backReference, typeWindow, tf.aliasType(store, name, aliasedType, typeParameters));
            }

            case IValueIDs.ConstructorType.ID:     {
                String name = null;
                boolean backReference = false;
                Type fieldTypes = null;
                Type adtType = null;
                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch(reader.field()){
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.ConstructorType.NAME:
                            name = reader.getString(); 
                            break;
                        case IValueIDs.ConstructorType.ADT:
                            adtType = readType(reader);
                            break;
                        case IValueIDs.ConstructorType.FIELD_TYPES:
                            fieldTypes = readType(reader);
                            break;
                    }
                }

                assert name != null;

                return returnAndStore(backReference, typeWindow, tf.constructorFromTuple(store, adtType, name, fieldTypes));
            }

            // External
            case IValueIDs.ExternalType.ID: {

                boolean backReference = false; 
                IConstructor symbol = null;
                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch(reader.field()){
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.ExternalType.SYMBOL:
                            symbol = (IConstructor) readValue(reader);
                            break;
                    }
                }

                return returnAndStore(backReference, typeWindow, tf.fromSymbol(symbol, new TypeStore(), p -> Collections.emptySet()));
            }

            case IValueIDs.ListType.ID:    {
                boolean backReference = false;
                Type elemType = null;
                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch(reader.field()){
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.ListType.ELEMENT_TYPE:
                            elemType = readType(reader);
                            break;
                    }
                }
                return returnAndStore(backReference, typeWindow, tf.listType(elemType));
            }

            case IValueIDs.MapType.ID: {   
                boolean backReference = false;
                Type valType = null;
                Type keyType = null;
                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch(reader.field()){
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.MapType.KEY_TYPE:
                            keyType = readType(reader);
                            break;
                        case IValueIDs.MapType.VALUE_TYPE:
                            valType = readType(reader);
                            break;
                    }
                }
                return returnAndStore(backReference, typeWindow, tf.mapType(keyType, valType));
            }

            case IValueIDs.ParameterType.ID:   {
                String name = null;
                boolean backReference = false;
                Type bound = null;

                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch (reader.field()){ 
                        case IValueIDs.ParameterType.NAME:
                            name = reader.getString();
                            break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.ParameterType.BOUND:
                            bound = readType(reader);
                            break;
                            
                    }
                }
                assert name != null;

                return returnAndStore(backReference, typeWindow, tf.parameterType(name, bound));
            }

            case IValueIDs.SetType.ID: {
                boolean backReference = false;
                Type elemType = null;
                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch(reader.field()){
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                        case IValueIDs.SetType.ELEMENT_TYPE:
                            elemType = readType(reader);
                            break;
                    }
                }
                return returnAndStore(backReference, typeWindow, tf.setType(elemType));
            }

            case IValueIDs.TupleType.ID: {
                boolean backReference = false;
                String [] fieldNames = null;
                Type[] elemTypes = null;

                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch (reader.field()){ 
                        case IValueIDs.TupleType.NAMES:
                            fieldNames = reader.getStrings();
                            break;
                        case IValueIDs.TupleType.TYPES:
                            elemTypes = new Type[reader.getRepeatedLength()];
                            for (int i = 0; i < elemTypes.length; i++) {
                                elemTypes[i] = readType(reader);
                            }
                            break;
                        case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                            backReference = true; 
                            break;
                    }
                }

                if(fieldNames != null){
                    assert fieldNames.length == elemTypes.length;
                    return returnAndStore(backReference, typeWindow, tf.tupleType(elemTypes, fieldNames));
                } else {
                    return returnAndStore(backReference, typeWindow, tf.tupleType(elemTypes));
                }
            }

            case IValueIDs.PreviousType.ID: {
                Integer n = null;
                while (reader.next() != IWireInputStream.MESSAGE_END) {
                    switch (reader.field()){ 
                        case IValueIDs.PreviousType.HOW_LONG_AGO:
                            n = reader.getInteger();
                            break;
                        default:
                            reader.skipNestedField();
                            break;
                    }
                }

                assert n != null;

                Type type = typeWindow.lookBack(n);
                if(type == null){
                    throw new RuntimeException("Unexpected type cache miss");
                }
                return type;
            }
            default:
                throw new IOException("Unexpected new message: " + reader.message());
        }
    }

    private static <T> T returnAndStore(boolean backReferenced, TrackLastRead<T> window, T value) {
        if (backReferenced) {
            window.read(value);
        }
        return value;
    }


    private IValue readValue(final IWireInputStream reader) throws IOException{
        reader.next();
        assert reader.current() == IWireInputStream.MESSAGE_START;
        switch (reader.message()) {
            case IValueIDs.BoolValue.ID: return readBoolean(reader);
            case IValueIDs.ConstructorValue.ID:    return readConstructor(reader);
            case IValueIDs.DateTimeValue.ID: return readDateTime(reader);
            case IValueIDs.IntegerValue.ID: return readInteger(reader);
            case IValueIDs.ListValue.ID: return readList(reader);
            case IValueIDs.SourceLocationValue.ID: return readSourceLocation(reader);
            case IValueIDs.MapValue.ID: return readMap(reader);
            case IValueIDs.NodeValue.ID: return readNode(reader);
            case IValueIDs.RationalValue.ID: return readRational(reader);
            case IValueIDs.RealValue.ID: return readReal(reader);
            case IValueIDs.SetValue.ID: return readSet(reader);
            case IValueIDs.StringValue.ID: return readString(reader);
            case IValueIDs.TupleValue.ID: return readTuple(reader);
            case IValueIDs.PreviousValue.ID: return readPreviousValue(reader);
            default:
                throw new IllegalArgumentException("readValue: " + reader.message());
        }
    }

    private IValue readPreviousValue(final IWireInputStream reader) throws IOException {
        int n = -1;
        while(reader.next() != IWireInputStream.MESSAGE_END){
            if(reader.field() == IValueIDs.PreviousValue.HOW_FAR_BACK){
                n = reader.getInteger();
                reader.skipMessage();
                break;
            }
        }

        assert n != -1;

        IValue result = valueWindow.lookBack(n);
        if (result == null) {
            throw new IOException("Unexpected value cache miss");
        }
        return result;   
    }


    private IValue readTuple(final IWireInputStream reader) throws IOException {
        boolean backReference = false;
        IValue[] children = new IValue[0];
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()) {
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: 
                    backReference = true; 
                    break;
                case IValueIDs.TupleValue.CHILDREN:
                    children = new IValue[reader.getRepeatedLength()];
                    for (int i = 0; i < children.length; i++) {
                        children[i] = readValue(reader);
                    }
                    break;
                default:
                    reader.skipNestedField();
                    break;
            }
        }
        return returnAndStore(backReference, valueWindow, vf.tuple(children));
    }

    private IValue readSet(final IWireInputStream reader) throws IOException {
        ISet result = null;
        boolean backReference = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()) {
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: 
                    backReference = true; 
                    break;
                case IValueIDs.SetValue.ELEMENTS:
                    int size = reader.getRepeatedLength();
                    switch (size) {
                        case 0:
                            result = vf.set();
                            break;
                        case 1:
                            result = vf.set(readValue(reader));
                            break;
                        case 2:
                            result = vf.set(readValue(reader), readValue(reader));
                            break;
                        default:
                            ISetWriter writer = vf.setWriter();
                            for (int i = 0; i < size; i++) {
                                writer.insert(readValue(reader));
                            }
                            result = writer.done();
                            break;
                    }
                    break;
            }
        }
        return returnAndStore(backReference, valueWindow, result);
    }

    private IValue readList(final IWireInputStream reader) throws IOException {
        IList result = null;
        boolean backReference = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()) {
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: 
                    backReference = true; 
                    break;
                case IValueIDs.ListValue.ELEMENTS:
                    int size = reader.getRepeatedLength();
                    switch (size) {
                        case 0:
                            result = vf.list();
                            break;
                        case 1:
                            result = vf.list(readValue(reader));
                            break;
                        case 2:
                            IValue first = readValue(reader);
                            IValue second = readValue(reader);
                            result = vf.list(first, second);
                            break;
                        default:
                            IListWriter writer = vf.listWriter();
                            for (int i = 0; i < size; i++) {
                                writer.append(readValue(reader));
                            }
                            result = writer.done();
                            break;
                    }
                    break;
            }
        }
        return returnAndStore(backReference, valueWindow, result);
    }

    private IValue readMap(final IWireInputStream reader) throws IOException {
        IMapWriter result = vf.mapWriter();
        boolean backReference = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()) {
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: 
                    backReference = true; 
                    break;
                case IValueIDs.MapValue.KV_PAIRS:
                    int size = reader.getRepeatedLength();
                    for (int i = 0; i < size; i += 2) {
                        IValue key = readValue(reader);
                        IValue value = readValue(reader);
                        result.put(key, value);;
                    }
                    break;
            }
        }
        return returnAndStore(backReference, valueWindow, result.done());
    }

    private IValue readNode(final IWireInputStream reader) throws IOException {
        String name = null;
        IValue[] children = new IValue[0];
        ImmutableMap<String, IValue> annos = null;
        ImmutableMap<String, IValue> kwParams = null;


        boolean backReference = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()){
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: 
                    backReference = true; 
                    break;
                case IValueIDs.NodeValue.NAME: 
                    name = reader.getString(); 
                    break;
                case IValueIDs.NodeValue.PARAMS: 
                    children = new IValue[reader.getRepeatedLength()];
                    for (int i = 0; i < children.length; i++) {
                        children[i] = readValue(reader);
                    }
                    break;
                case IValueIDs.NodeValue.KWPARAMS: 
                    kwParams = readNamedValues(reader);
                    break;
                case IValueIDs.NodeValue.ANNOS: 
                    annos = readNamedValues(reader);
                    break;
            }
        }
        assert name != null;
        
        INode node;
        if (annos != null) {
            node =  vf.node(name, children).asAnnotatable().setAnnotations(annos);
        }
        else if (kwParams != null) {
            node = vf.node(name, children, kwParams);
        }
        else {
            node = vf.node(name, children);
        }
        return returnAndStore(backReference, valueWindow, node);
    }

    @SuppressWarnings("deprecation")
    private IValue readConstructor(final IWireInputStream reader) throws IOException {
        Type type = null;
        IValue[] children = new IValue[0];
        ImmutableMap<String, IValue> annos = null;
        ImmutableMap<String, IValue> kwParams = null;


        boolean backReference = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()){
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: 
                    backReference = true; 
                    break;
                case IValueIDs.ConstructorValue.TYPE: 
                    type = readType(reader);
                    break;
                case IValueIDs.ConstructorValue.PARAMS: 
                    children = new IValue[reader.getRepeatedLength()];
                    for (int i = 0; i < children.length; i++) {
                        children[i] = readValue(reader);
                    }
                    break;
                case IValueIDs.ConstructorValue.KWPARAMS: 
                    kwParams = readNamedValues(reader);
                    break;
                case IValueIDs.ConstructorValue.ANNOS: 
                    annos = readNamedValues(reader);
                    break;
            }
        }
        
        IConstructor constr;
        if (annos != null) {
            constr =  vf.constructor(type, annos, children);
        }
        else if (kwParams != null) {
            constr = vf.constructor(type, children, kwParams);
        }
        else {
            constr = vf.constructor(type, children);
        }
        return returnAndStore(backReference, valueWindow, constr);
    }




    private ImmutableMap<String, IValue> readNamedValues(IWireInputStream reader) throws IOException {
        TransientMap<String, IValue> result = TrieMap_5Bits.transientOf();
        String[] names = null;
        reader.next();
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()){
                case IValueIDs.NamedValues.NAMES:
                    names = reader.getStrings();
                    break;
                case IValueIDs.NamedValues.VALUES:
                    assert names != null && names.length == reader.getRepeatedLength();
                    for (String name: names) {
                        result.__put(name, readValue(reader));
                    }
                    break;
            }
        }
        return result.freeze();
    }

    private IValue readString(final IWireInputStream reader) throws IOException {
        String str = null;
        boolean backReference = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()) {
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED: 
                    backReference = true; 
                    break;
                case IValueIDs.StringValue.CONTENT: 
                    str = reader.getString(); 
                    break;
                default:
                    reader.skipNestedField();
                    break;
            }
        }

        assert str != null;

        return returnAndStore(backReference, valueWindow, vf.string(str));
    }


    private IValue readReal(final IWireInputStream reader) throws IOException {
        byte[] bytes = null;
        Integer scale = null;

        boolean backReference = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()){
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                    backReference = true;
                    break;
                case IValueIDs.RealValue.SCALE:
                    scale = reader.getInteger(); 
                    break;
                case IValueIDs.RealValue.CONTENT:
                    bytes = reader.getBytes(); 
                    break;
                default:
                    reader.skipNestedField();
                    break;
            }
        }

        assert bytes != null && scale != null;

        return returnAndStore(backReference, valueWindow, vf.real(new BigDecimal(new BigInteger(bytes), scale).toString())); // TODO: Improve this?
    }


    private IValue readRational(final IWireInputStream reader) throws IOException {
        boolean backReference = false;
        IInteger denominator = null;
        IInteger numerator = null;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()){
                case IValueIDs.Common.CAN_BE_BACK_REFERENCED:
                    backReference = true;
                    break;
                case IValueIDs.RationalValue.DENOMINATOR:
                    denominator = (IInteger) readValue(reader);
                    break;
                case IValueIDs.RationalValue.NUMERATOR:
                    numerator = (IInteger) readValue(reader);
                    break;
                default:
                    reader.skipNestedField();
                    break;
            }
        }

        return returnAndStore(backReference, valueWindow, vf.rational(numerator, denominator));
    }




    private IValue readSourceLocation(final IWireInputStream reader) throws IOException {
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
        while (reader.next() != IWireInputStream.MESSAGE_END) {
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
        return loc;
    }



    private IValue readInteger(final IWireInputStream reader) throws IOException {
        Integer small = null;
        byte[] big = null;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            switch(reader.field()){
                case IValueIDs.IntegerValue.INTVALUE:  small = reader.getInteger(); break;
                case IValueIDs.IntegerValue.BIGVALUE:    big = reader.getBytes(); break;
            }
        }

        if(small != null){
            return vf.integer(small);
        } else if(big != null){
            return vf.integer(big);
        } else {
            throw new RuntimeException("Missing field in INT_VALUE");
        }
    }


    private IValue readDateTime(final IWireInputStream reader) throws IOException {
        Integer year = null;;
        Integer month = null;
        Integer day = null;

        Integer hour = null;
        Integer minute = null;
        Integer second = null;
        Integer millisecond = null;

        Integer timeZoneHourOffset = null;
        Integer timeZoneMinuteOffset = null;

        while (reader.next() != IWireInputStream.MESSAGE_END) {
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


        if (hour != null && year != null) {
            return vf.datetime(year, month, day, hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);
        }
        else if (hour != null) {
            return vf.time(hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);
        }
        else {
            assert year != null;
            return vf.datetime(year, month, day);
        }
    }




    private IValue readBoolean(final IWireInputStream reader) throws IOException {
        boolean value = false;
        while (reader.next() != IWireInputStream.MESSAGE_END) {
            if(reader.field() == IValueIDs.BoolValue.VALUE){
                value = true;
            }
            else {
                reader.skipNestedField();
            }
        }

        return vf.bool(value);
    }
}

