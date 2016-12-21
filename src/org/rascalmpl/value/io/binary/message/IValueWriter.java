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
import java.util.HashSet;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.stream.IValueOutputStream;
import org.rascalmpl.value.io.binary.util.PrePostIValueIterator;
import org.rascalmpl.value.io.binary.util.TrackLastWritten;
import org.rascalmpl.value.io.binary.util.WindowCacheFactory;
import org.rascalmpl.value.io.binary.util.WindowSizes;
import org.rascalmpl.value.io.binary.wire.IWireOutputStream;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.visitors.IValueVisitor;
import org.rascalmpl.values.ValueFactoryFactory;
            
/**
 * An utility class for the {@link IValueOutputStream}. Only directly use methods in this class if you have nested IValues in an exisiting {@link IWireOutputStream}.
 *
 */
public class IValueWriter {
    /**
     * Write an IValue to an exisiting wire stream. <br />
     * <br />
     * In most cases you want to use the {@linkplain IValueOutputStream}.
     *  
     * @param writer the wire writer to use
     * @param store the type store to use for looking up types to write
     * @param size the windo sizes to use
     * @param value the value to write
     * @throws IOException
     */
    public static void write(IWireOutputStream writer, TypeStore store, WindowSizes size, IValue value ) throws IOException {
        final WindowCacheFactory windowFactory = WindowCacheFactory.getInstance();
        TrackLastWritten<Type> typeCache = windowFactory.getTrackLastWrittenReferenceEquality(size.typeWindow);
        TrackLastWritten<IValue> valueCache = windowFactory.getTrackLastWrittenReferenceEquality(size.valueWindow);
        TrackLastWritten<ISourceLocation> uriCache = windowFactory.getTrackLastWrittenReferenceEquality(size.uriWindow);
        try {
            writeHeader(writer, size.valueWindow, size.typeWindow, size.uriWindow);
            write(writer, store, value, typeCache, valueCache, uriCache);
            writeEnd(writer);
        } finally {
            windowFactory.returnTrackLastWrittenReferenceEquality(typeCache);
            windowFactory.returnTrackLastWrittenReferenceEquality(valueCache);
            windowFactory.returnTrackLastWrittenReferenceEquality(uriCache);
        }
    }

    /**
     * Write an Type to an exisiting wire stream.
     *  
     * @param writer the wire writer to use
     * @param size the windo sizes to use
     * @param store the type store to use for looking up types to write
     * @param type the type to write
     * @throws IOException
     */
    public static void write(IWireOutputStream writer, TypeStore store, WindowSizes size, Type type) throws IOException {
        final WindowCacheFactory windowFactory = WindowCacheFactory.getInstance();
        TrackLastWritten<Type> typeCache = windowFactory.getTrackLastWrittenReferenceEquality(size.typeWindow);
        TrackLastWritten<IValue> valueCache = windowFactory.getTrackLastWrittenReferenceEquality(size.valueWindow);
        TrackLastWritten<ISourceLocation> uriCache = windowFactory.getTrackLastWrittenReferenceEquality(size.uriWindow);
        try {
            writeHeader(writer, size.valueWindow, size.typeWindow, size.uriWindow);
            write(writer, store, type, typeCache, valueCache, uriCache);
            writeEndType(writer);
        } finally {
            windowFactory.returnTrackLastWrittenReferenceEquality(typeCache);
            windowFactory.returnTrackLastWrittenReferenceEquality(valueCache);
            windowFactory.returnTrackLastWrittenReferenceEquality(uriCache);
        }
    }
    

    private static void writeHeader(IWireOutputStream writer, int valueWindowSize, int typeWindowSize, int uriWindowSize) throws IOException {
        writer.startMessage(IValueIDs.Header.ID);
        writer.writeField(IValueIDs.Header.VALUE_WINDOW, valueWindowSize);
        writer.writeField(IValueIDs.Header.TYPE_WINDOW, typeWindowSize);
        writer.writeField(IValueIDs.Header.SOURCE_LOCATION_WINDOW, uriWindowSize);
        writer.endMessage();
    }

    private static void write(final IWireOutputStream writer, final TypeStore store, final Type type, final TrackLastWritten<Type> typeCache, final TrackLastWritten<IValue> valueCache, final TrackLastWritten<ISourceLocation> uriCache) throws IOException {
        type.accept(new ITypeVisitor<Void, IOException>() {

            private boolean writeFromCache(Type type) throws IOException {
                int lastSeen = typeCache.howLongAgo(type);
                if (lastSeen != -1) { 
                    writeSingleValueMessage(writer, IValueIDs.PreviousType.ID, IValueIDs.PreviousType.HOW_LONG_AGO, lastSeen);
                    return true;
                }
                return false;
            }

            @Override
            public Void visitAbstractData(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                type.getTypeParameters().accept(this);
                writeSingleValueMessageBackReferenced(writer, IValueIDs.ADTType.ID, IValueIDs.ADTType.NAME, type.getName());
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitAlias(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                type.getAliased().accept(this);
                type.getTypeParameters().accept(this);
                writeSingleValueMessageBackReferenced(writer, IValueIDs.AliasType.ID, IValueIDs.AliasType.NAME, type.getName());
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitConstructor(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                type.getAbstractDataType().accept(this);
                type.getFieldTypes().accept(this);
                writeSingleValueMessageBackReferenced(writer, IValueIDs.ConstructorType.ID, IValueIDs.ConstructorType.NAME, type.getName());
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitExternal(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                IValueFactory vf = ValueFactoryFactory.getValueFactory();
                ISetWriter grammar = vf.setWriter();
                IConstructor symbol = type.asSymbol(vf, store, grammar, new HashSet<>());
                write(writer, store, grammar.done(), typeCache, valueCache, uriCache);
                write(writer, store, symbol, typeCache, valueCache, uriCache);
                writeEmptyMessageBackReferenced(writer, IValueIDs.ExternalType.ID);
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitList(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                type.getElementType().accept(this);
                writeEmptyMessageBackReferenced(writer, IValueIDs.ListType.ID);
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitMap(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                type.getKeyType().accept(this);
                type.getValueType().accept(this);
                writeEmptyMessageBackReferenced(writer, IValueIDs.MapType.ID);
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitParameter(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                type.getBound().accept(this);
                writeSingleValueMessageBackReferenced(writer, IValueIDs.ParameterType.ID, IValueIDs.ParameterType.NAME,type.getName());
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitSet(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                type.getElementType().accept(this);
                writeEmptyMessageBackReferenced(writer, IValueIDs.SetType.ID);
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitTuple(Type type) throws IOException {
                if (writeFromCache(type)) {
                    return null;
                }
                for(int i = 0; i < type.getArity(); i++){
                    type.getFieldType(i).accept(this);
                }
                writer.startMessage(IValueIDs.TupleType.ID);
                writeCanBeBackReferenced(writer);
                writer.writeField(IValueIDs.TupleType.ARITY, type.getArity());
                String[] fieldNames = type.getFieldNames();
                if(fieldNames != null){
                    writer.writeField(IValueIDs.TupleType.NAMES, fieldNames);
                }
                writer.endMessage();
                typeCache.write(type);
                return null;
            }

            @Override
            public Void visitBool(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.BoolType.ID);
                return null;
            }

            @Override
            public Void visitDateTime(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.DateTimeType.ID);
                return null;
            }

            @Override
            public Void visitInteger(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.IntegerType.ID);
                return null;
            }

            @Override
            public Void visitNode(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.NodeType.ID);
                return null;
            }

            @Override
            public Void visitNumber(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.NumberType.ID);
                return null;
            }

            @Override
            public Void visitRational(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.RationalType.ID);
                return null;
            }

            @Override
            public Void visitReal(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.RealType.ID);
                return null;
            }

            @Override
            public Void visitSourceLocation(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.SourceLocationType.ID);
                return null;
            }

            @Override
            public Void visitString(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.StringType.ID);
                return null;
            }

            @Override
            public Void visitValue(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.ValueType.ID);
                return null;
            }

            @Override
            public Void visitVoid(Type t) throws IOException {
                writer.writeEmptyMessage(IValueIDs.VoidType.ID);
                return null;
            }
        });
    }
    
    private static void writeSingleValueMessage(final IWireOutputStream writer, int messageID, int fieldId, int fieldValue) throws IOException {
        writer.startMessage(messageID);
        writer.writeField(fieldId, fieldValue);
        writer.endMessage();
    }
    private static void writeSingleValueMessageBackReferenced(final IWireOutputStream writer, int messageID, int fieldId, int fieldValue) throws IOException {
        writer.startMessage(messageID);
        writeCanBeBackReferenced(writer);
        writer.writeField(fieldId, fieldValue);
        writer.endMessage();
    }
    
    private static void writeSingleValueMessage(final IWireOutputStream writer, int messageID, int fieldId, String fieldValue) throws IOException {
        writer.startMessage(messageID);
        writer.writeField(fieldId, fieldValue);
        writer.endMessage();
    }
    private static void writeSingleValueMessageBackReferenced(final IWireOutputStream writer, int messageID, int fieldId, String fieldValue) throws IOException {
        writer.startMessage(messageID);
        writeCanBeBackReferenced(writer);
        writer.writeField(fieldId, fieldValue);
        writer.endMessage();
    }
    private static void writeEmptyMessageBackReferenced(final IWireOutputStream writer, int messageID) throws IOException {
        writer.startMessage(messageID);
        writeCanBeBackReferenced(writer);
        writer.endMessage();
    }
    private static void writeCanBeBackReferenced(final IWireOutputStream writer) throws IOException {
        writer.writeField(IValueIDs.Common.CAN_BE_BACK_REFERENCED, 1);
    }
    private static void writeEnd(IWireOutputStream writer) throws IOException {
        writer.writeEmptyMessage(IValueIDs.LastValue.ID);
    }

    private static void writeEndType(IWireOutputStream writer) throws IOException {
        writer.writeEmptyMessage(IValueIDs.LastType.ID);
    }

    private static final IInteger MININT = ValueFactoryFactory.getValueFactory().integer(Integer.MIN_VALUE);
    private static final IInteger MAXINT = ValueFactoryFactory.getValueFactory().integer(Integer.MAX_VALUE);
    
    private static void write(final IWireOutputStream writer, final TypeStore store, final IValue value, final TrackLastWritten<Type> typeCache, final TrackLastWritten<IValue> valueCache, final TrackLastWritten<ISourceLocation> uriCache) throws IOException {
        PrePostIValueIterator iter = new PrePostIValueIterator(value);
        
        // returns if the value should be put into the cache or not
        IValueVisitor<Boolean, IOException> visitWriter = new IValueVisitor<Boolean, IOException>() {

            private boolean writeFromCache(IValue val) throws IOException {
                int lastSeen = valueCache.howLongAgo(val);
                if (lastSeen != -1) {
                    writeSingleValueMessage(writer, IValueIDs.PreviousValue.ID, IValueIDs.PreviousValue.HOW_FAR_BACK, lastSeen);
                    iter.skipValue();
                    return true;
                }
                return false;
            }
            @Override
            public Boolean visitConstructor(IConstructor cons) throws IOException {
                if (writeFromCache(cons) || iter.atBeginning()) {
                    return false;
                }
                write(writer, store, cons.getUninstantiatedConstructorType(), typeCache, valueCache, uriCache);

                writer.startMessage(IValueIDs.ConstructorValue.ID);
                writeCanBeBackReferenced(writer);
                int arity = cons.arity();
                if (arity > 0) {
                    writer.writeField(IValueIDs.ConstructorValue.ARITY, arity);
                }
                if(cons.mayHaveKeywordParameters()){
                    if(cons.asWithKeywordParameters().hasParameters()){
                        writer.writeField(IValueIDs.ConstructorValue.KWPARAMS, cons.asWithKeywordParameters().getParameters().size());
                    }
                } else {
                    if(cons.asAnnotatable().hasAnnotations()){
                        writer.writeField(IValueIDs.ConstructorValue.ANNOS, cons.asAnnotatable().getAnnotations().size());
                    }
                }
                writer.endMessage();
                return true;
            }
            @Override
            public Boolean visitNode(INode node) throws IOException {
                if (writeFromCache(node) || iter.atBeginning()) {
                    return false;
                }
                writer.startMessage(IValueIDs.NodeValue.ID);
                writeCanBeBackReferenced(writer);
                writer.writeField(IValueIDs.NodeValue.NAME,  node.getName());
                writer.writeField(IValueIDs.NodeValue.ARITY, node.arity());
                if(node.mayHaveKeywordParameters()){
                    if(node.asWithKeywordParameters().hasParameters()){
                        writer.writeField(IValueIDs.NodeValue.KWPARAMS, node.asWithKeywordParameters().getParameters().size());
                    }
                } else {
                    if(node.asAnnotatable().hasAnnotations()){
                        writer.writeField(IValueIDs.NodeValue.ANNOS, node.asAnnotatable().getAnnotations().size());
                    }
                }
                writer.endMessage();
                return true;
            }
            @Override
            public Boolean visitList(IList o) throws IOException {
                if (writeFromCache(o) || iter.atBeginning()) {
                    return false;
                }
                if (o.length() > 0) {
                    writeSingleValueMessageBackReferenced(writer, IValueIDs.ListValue.ID, IValueIDs.ListValue.SIZE, o.length());
                }
                else {
                    writeEmptyMessageBackReferenced(writer, IValueIDs.ListValue.ID);
                }
                return true;
            }
            @Override
            public Boolean visitMap(IMap o) throws IOException {
                if (writeFromCache(o) || iter.atBeginning()) {
                    return false;
                }
                if (o.size() > 0) {
                    writeSingleValueMessageBackReferenced(writer, IValueIDs.MapValue.ID, IValueIDs.MapValue.SIZE, o.size());
                }
                else {
                    writeEmptyMessageBackReferenced(writer, IValueIDs.MapValue.ID);
                }
                return true;
            }
            @Override
            public Boolean visitSet(ISet o) throws IOException {
                if (writeFromCache(o) || iter.atBeginning()) {
                    return false;
                }
                if (o.size() > 0) {
                    writeSingleValueMessageBackReferenced(writer, IValueIDs.SetValue.ID, IValueIDs.SetValue.SIZE, o.size());
                }
                else {
                    writeEmptyMessageBackReferenced(writer, IValueIDs.SetValue.ID);
                }
                return true;
            }
            @Override
            public Boolean visitRational(IRational o) throws IOException {
                if (writeFromCache(o) || iter.atBeginning()) {
                    return false;
                }
                writeEmptyMessageBackReferenced(writer, IValueIDs.RationalValue.ID);
                return true;
            }
            @Override
            public Boolean visitTuple(ITuple o) throws IOException {
                if (writeFromCache(o) || iter.atBeginning()) {
                    return false;
                }
                 writeSingleValueMessageBackReferenced(writer, IValueIDs.TupleValue.ID, IValueIDs.TupleValue.SIZE, o.arity());
                 return true;
            }

            @Override
            public Boolean visitBoolean(IBool boolValue) throws IOException {
                if (boolValue.getValue()) {
                    writeSingleValueMessage(writer, IValueIDs.BoolValue.ID, IValueIDs.BoolValue.VALUE, 1);
                }
                else {
                    writer.writeEmptyMessage(IValueIDs.BoolValue.ID);
                }
                return false;
            }

            @Override
            public Boolean visitDateTime(IDateTime dateTime) throws IOException {
                writer.startMessage(IValueIDs.DateTimeValue.ID);

                if (!dateTime.isTime()) {
                    writer.writeField(IValueIDs.DateTimeValue.YEAR, dateTime.getYear());
                    writer.writeField(IValueIDs.DateTimeValue.MONTH, dateTime.getMonthOfYear());
                    writer.writeField(IValueIDs.DateTimeValue.DAY, dateTime.getDayOfMonth());
                }

                if (!dateTime.isDate()) {
                    writer.writeField(IValueIDs.DateTimeValue.HOUR, dateTime.getHourOfDay());
                    writer.writeField(IValueIDs.DateTimeValue.MINUTE, dateTime.getMinuteOfHour());
                    writer.writeField(IValueIDs.DateTimeValue.SECOND, dateTime.getSecondOfMinute());
                    writer.writeField(IValueIDs.DateTimeValue.MILLISECOND, dateTime.getMillisecondsOfSecond());

                    writer.writeField(IValueIDs.DateTimeValue.TZ_HOUR, dateTime.getTimezoneOffsetHours());
                    writer.writeField(IValueIDs.DateTimeValue.TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
                }
                writer.endMessage();
                return false;
            }
            @Override
            public Boolean visitInteger(IInteger ii) throws IOException {
                writer.startMessage(IValueIDs.IntegerValue.ID);
                if(ii. greaterEqual(MININT).getValue() && ii.lessEqual(MAXINT).getValue()){
                    writer.writeField(IValueIDs.IntegerValue.INTVALUE, ii.intValue());
                } 
                else {
                    writer.writeField(IValueIDs.IntegerValue.BIGVALUE, ii.getTwosComplementRepresentation());
                }
                writer.endMessage();
                return false;
            }


            @Override
            public Boolean visitReal(IReal o) throws IOException {
                writer.startMessage(IValueIDs.RealValue.ID);
                writer.writeField(IValueIDs.RealValue.CONTENT, o.unscaled().getTwosComplementRepresentation());
                writer.writeField(IValueIDs.RealValue.SCALE, o.scale());
                writer.endMessage();
                return false;
            }

            @Override
            public Boolean visitSourceLocation(ISourceLocation loc) throws IOException {
                writer.startMessage(IValueIDs.SourceLocationValue.ID);
                ISourceLocation uriPart = loc.top();
                int alreadyWritten = uriCache.howLongAgo(uriPart);
                if (alreadyWritten == -1) {
                    writer.writeField(IValueIDs.SourceLocationValue.SCHEME, uriPart.getScheme());
                    if (uriPart.hasAuthority()) {
                        writer.writeField(IValueIDs.SourceLocationValue.AUTHORITY, uriPart.getAuthority());
                    }
                    if (uriPart.hasPath()) {
                        writer.writeField(IValueIDs.SourceLocationValue.PATH, uriPart.getPath());
                    }
                    if (uriPart.hasQuery()) {
                        writer.writeField(IValueIDs.SourceLocationValue.QUERY,  uriPart.getQuery());
                    }
                    if (uriPart.hasFragment()) {
                        writer.writeField(IValueIDs.SourceLocationValue.FRAGMENT,  uriPart.getFragment());
                    }
                    uriCache.write(uriPart);
                }
                else {
                    writer.writeField(IValueIDs.SourceLocationValue.PREVIOUS_URI, alreadyWritten);
                }

                if(loc.hasOffsetLength()){
                    writer.writeField(IValueIDs.SourceLocationValue.OFFSET, loc.getOffset());
                    writer.writeField(IValueIDs.SourceLocationValue.LENGTH, loc.getLength());
                } 
                if(loc.hasLineColumn()){
                    writer.writeField(IValueIDs.SourceLocationValue.BEGINLINE, loc.getBeginLine());
                    writer.writeField(IValueIDs.SourceLocationValue.ENDLINE, loc.getEndLine());
                    writer.writeField(IValueIDs.SourceLocationValue.BEGINCOLUMN, loc.getBeginColumn());
                    writer.writeField(IValueIDs.SourceLocationValue.ENDCOLUMN, loc.getEndColumn());
                }
                writer.endMessage();
                return false;
            }

            @Override
            public Boolean visitString(IString o) throws IOException {
                writeSingleValueMessage(writer, IValueIDs.StringValue.ID, IValueIDs.StringValue.CONTENT, o.getValue());
                return false;
            }
            @Override
            public Boolean visitExternal(IExternalValue externalValue) throws IOException {
                throw new RuntimeException("Not supported yet");
            }
            @Override
            public Boolean visitListRelation(IList o) throws IOException {
                return visitList(o);
            }
            @Override
            public Boolean visitRelation(ISet o) throws IOException {
                return visitSet(o);
            }
        };

        while(iter.hasNext()){
            final IValue currentValue = iter.next();
            if (currentValue.accept(visitWriter)) {
                valueCache.write(currentValue);
            }
        }
    }


}
