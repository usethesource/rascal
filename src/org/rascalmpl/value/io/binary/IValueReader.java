package org.rascalmpl.value.io.binary;

import java.io.IOException;
import java.io.InputStream;
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
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.io.binary.ValueWireInputStream.ReaderPosition;
import org.rascalmpl.value.io.binary.util.LinearCircularLookupWindow;
import org.rascalmpl.value.io.binary.util.TrackLastRead;
import org.rascalmpl.value.io.binary.util.TypeIteratorKind;
import org.rascalmpl.value.io.binary.util.ValueIteratorKind;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.tukaani.xz.XZInputStream;

import io.usethesource.capsule.TransientMap;
import io.usethesource.capsule.TrieMap_5Bits;

/**
 * RSFIValueReader is a binary deserializer for IValues and Types. The main public function is:
 * - readValue
 */

public class IValueReader {

	private static final TypeFactory tf = TypeFactory.getInstance();
	private static final RascalTypeFactory rtf = RascalTypeFactory.getInstance();
	
	/**
	 * this will consume the whole stream, or at least more than needed due to buffering
	 * @param in
	 * @param vf
	 * @param ts
	 * @param closeStream
	 * @return
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static IValue read(InputStream in, IValueFactory vf, TypeStore ts) throws IOException, URISyntaxException {
		byte[] currentHeader = new byte[IValueWriter.header.length];
        in.read(currentHeader);
        if (!Arrays.equals(IValueWriter.header, currentHeader)) {
            throw new IOException("Unsupported file");
        }

        TrackLastRead<Type> typeWindow = getWindow(in.read());
        TrackLastRead<IValue> valueWindow = getWindow(in.read());
        TrackLastRead<ISourceLocation> uriWindow = getWindow(in.read());
        
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
            default:
                throw new IOException("Unsupported compression in file");
        }

        ts.extendStore(RascalValueFactory.getStore());
		return read(new ValueWireInputStream(in), vf, ts, typeWindow, valueWindow, uriWindow);
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
	    return new LinearCircularLookupWindow<>(size * 1024);
    }
	
	private static void pushAndCache(final ReaderStack<Type> stack, final TrackLastRead<Type> typeWindow, final Type type) throws IOException{
	    stack.push(type);
	    if (TypeIteratorKind.getKind(type).isCompound()) {
	        typeWindow.read(type);
	    }
	}

	private static void pushAndCache(final ReaderStack<IValue> stack, final TrackLastRead<IValue> valueWindow, final IValue v) throws IOException{
		stack.push(v);
		if (ValueIteratorKind.getKind(v).isCompound()) {
		    valueWindow.read(v);
		}
	}
	
	private static IValue read(final ValueWireInputStream reader, final IValueFactory vf, final TypeStore store, TrackLastRead<Type> typeWindow, TrackLastRead<IValue> valueWindow, TrackLastRead<ISourceLocation> uriWindow) throws IOException, URISyntaxException{

        ReaderStack<Type> tstack = new ReaderStack<>(100);
        ReaderStack<IValue> vstack = new ReaderStack<>(1024);

        try {
           
            while(reader.next() == ReaderPosition.MESSAGE_START){
                
                switch (reader.message()) {
                    
                    /********************************/
                    /*          Types               */
                    /********************************/
                    
                    case IValueIDs.BoolType.ID:  
                        reader.skipMessage(); // forward to the end
                        pushAndCache(tstack, typeWindow, tf.boolType());
                        break;

                    case IValueIDs.DateTimeType.ID:    
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.dateTimeType());
                        break;

                    case IValueIDs.IntegerType.ID:     
                        reader.skipMessage(); 
                        pushAndCache(tstack, typeWindow, tf.integerType());
                        break;

                    case IValueIDs.NodeType.ID:        
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.nodeType());
                        break;

                    case IValueIDs.NumberType.ID:  
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.numberType());
                        break;

                    case IValueIDs.RationalType.ID:     
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.rationalType());
                        break;

                    case IValueIDs.RealType.ID:        
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.realType());
                        break;

                    case IValueIDs.SourceLocationType.ID:     
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.sourceLocationType());
                        break;

                    case IValueIDs.StringType.ID:     
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.stringType());
                        break;

                    case IValueIDs.ValueType.ID:       
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.valueType());
                        break;

                    case IValueIDs.VoidType.ID:        
                        reader.skipMessage();
                        pushAndCache(tstack, typeWindow, tf.voidType());
                        break;

                    // Composite types

                    case IValueIDs.ADTType.ID: {   
                        String name = null;

                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.ADTType.NAME:
                                    name = reader.getString(); break;
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
                            pushAndCache(tstack, typeWindow, tf.abstractDataType(store, name, targs));
                        } else {
                            pushAndCache(tstack, typeWindow, tf.abstractDataType(store, name));
                        }
                        break;
                    }

                    case IValueIDs.AliasType.ID:   {   
                        String name = null;

                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.AliasType.NAME:
                                    name = reader.getString(); break;
                            }
                        }
                        
                        assert name != null;
                        
                        Type typeParameters = tstack.pop();
                        Type aliasedType = tstack.pop();

                        pushAndCache(tstack, typeWindow, tf.aliasType(store, name, aliasedType, typeParameters));
                        break;
                    }
                    
                    case IValueIDs.ConstructorType.ID:     {
                        String name = null;

                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.ConstructorType.NAME:
                                    name = reader.getString(); break;
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

                        if(fieldNames == null){
                            Type res = store.lookupConstructor(adtType, name, tf.tupleType(fieldTypesAr));
                            if(res == null) {
                                pushAndCache(tstack, typeWindow, tf.constructor(store, adtType, name, fieldTypesAr));
                            } else {
                                pushAndCache(tstack, typeWindow, res);
                            }
                        } else {
                            Object[] typeAndNames = new Object[2*arity];
                            for(int i = 0; i < arity; i++){
                                typeAndNames[2 * i] =  fieldTypesAr[i];
                                typeAndNames[2 * i + 1] = fieldNames[i];
                            }

                            Type res = store.lookupConstructor(adtType, name, tf.tupleType(typeAndNames));
                            if(res == null){
                                pushAndCache(tstack, typeWindow, tf.constructor(store, adtType, name, typeAndNames));
                            } else {
                                pushAndCache(tstack, typeWindow, res);
                            }
                        }
                        break;
                    }

                    // External

                    case IValueIDs.FunctionType.ID:    {
                        reader.skipMessage();

                        Type keywordParameterTypes = tstack.pop();
                        Type argumentTypes =  tstack.pop();
                        Type returnType = tstack.pop();


                        pushAndCache(tstack, typeWindow, rtf.functionType(returnType, argumentTypes, keywordParameterTypes));
                        break;
                    }

                    case IValueIDs.ReifiedType.ID: {
                        reader.skipMessage();
                        Type elemType = tstack.pop();

                        elemType = elemType.getFieldType(0);
                        pushAndCache(tstack, typeWindow, rtf.reifiedType(elemType));
                        break;
                    }

                    case IValueIDs.OverloadedType.ID: {
                        Integer size = null;

                        while (!reader.next().isEnd()) {
                            switch (reader.field()){ 
                                case IValueIDs.OverloadedType.SIZE:
                                    size = (int) reader.getLong();
                                    break;
                            }
                        }

                        assert size != null;

                        Set<FunctionType> alternatives = new HashSet<FunctionType>(size);
                        for(int i = 0; i < size; i++){
                            alternatives.add((FunctionType) tstack.pop());
                        }
                        pushAndCache(tstack, typeWindow, rtf.overloadedFunctionType(alternatives));
                        break;
                    }

                    case IValueIDs.NonTerminalType.ID: {
                        reader.skipMessage();

                        IConstructor nt = (IConstructor) vstack.pop();
                        pushAndCache(tstack, typeWindow, rtf.nonTerminalType(nt));
                        break;
                    }

                    case IValueIDs.ListType.ID:    {
                        reader.skipMessage();

                        Type elemType = tstack.pop();

                        pushAndCache(tstack, typeWindow, tf.listType(elemType));
                        break;
                    }

                    case IValueIDs.MapType.ID: {   
                        String keyLabel = null;
                        String valLabel = null;

                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.MapType.KEY_LABEL:
                                    keyLabel = reader.getString(); break;
                                case IValueIDs.MapType.VAL_LABEL:
                                    valLabel = reader.getString(); break;
                            }
                        }

                        Type valType = tstack.pop();
                        Type keyType = tstack.pop();

                        if(keyLabel == null){
                            pushAndCache(tstack, typeWindow, tf.mapType(keyType, valType));
                        } else {
                            assert valLabel != null;
                            pushAndCache(tstack, typeWindow, tf.mapType(keyType, keyLabel, valType, valLabel));
                        }
                        break;
                    }

                    case IValueIDs.ParameterType.ID:   {
                        String name = null;

                        while (!reader.next().isEnd()) {
                            switch (reader.field()){ 
                                case IValueIDs.ParameterType.NAME:
                                    name = reader.getString();
                                    break;
                            }
                        }
                        assert name != null;
                        
                        Type bound = tstack.pop();
                        pushAndCache(tstack, typeWindow, tf.parameterType(name, bound));
                        break;
                    }

                    case IValueIDs.SetType.ID: {
                        reader.skipMessage();
                        Type elemType = tstack.pop();

                        pushAndCache(tstack, typeWindow, tf.setType(elemType));
                        break;
                    }

                    case IValueIDs.TupleType.ID: {
                        String [] fieldNames = null;

                        Integer arity = null;

                        while (!reader.next().isEnd()) {
                            switch (reader.field()){ 
                                case IValueIDs.TupleType.ARITY:
                                    arity = (int) reader.getLong(); break;

                                case IValueIDs.TupleType.NAMES:
                                    int n = (int) reader.getLong();
                                    fieldNames = new String[n];
                                    for(int i = 0; i < n; i++){
                                        reader.next();
                                        assert reader.field() == IValueIDs.TupleType.NAMES;
                                        fieldNames[i] = reader.getString();
                                    }
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
                            pushAndCache(tstack, typeWindow, tf.tupleType(elemTypes, fieldNames));
                        } else {
                            pushAndCache(tstack, typeWindow, tf.tupleType(elemTypes));
                        }
                        break;
                    }

                    case IValueIDs.PreviousType.ID: {
                        Long n = null;
                        while (!reader.next().isEnd()) {
                            switch (reader.field()){ 
                                case IValueIDs.PreviousType.HOW_LONG_AGO:
                                    n = reader.getLong();
                                    break;
                            }
                        }

                        assert n != null;
                        
                        Type type = typeWindow.lookBack(n.intValue());
                        if(type == null){
                            throw new RuntimeException("Unexpected type cache miss");
                        }
                        //System.out.println("Previous type: " + type + ", " + n);
                        tstack.push(type);  // do not cache type twice
                        break;
                    }
                    
                    
                    /********************************/
                    /*          Values              */
                    /********************************/
                    
                    case IValueIDs.BoolValue.ID: {
                        Integer b = null;
                        while (!reader.next().isEnd()) {
                            if(reader.field() == IValueIDs.BoolValue.VALUE){
                                b = (int) reader.getLong();
                            }
                        }
                        
                        assert b != null;

                        pushAndCache(vstack, valueWindow, vf.bool(b == 0 ? false : true));
                        break;
                    }

                    case IValueIDs.ConstructorValue.ID:	{
                        Integer arity = null;
                        int annos = 0;
                        int kwparams = 0;
                        TransientMap<String, IValue> kwParamsOrAnnos = null;

                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.ConstructorValue.ARITY: arity = (int) reader.getLong(); break;
                                case IValueIDs.ConstructorValue.KWPARAMS: kwparams = (int)reader.getLong(); break;
                                case IValueIDs.ConstructorValue.ANNOS: annos = (int)reader.getLong(); break;
                            }
                        }
                        
                        assert arity != null;
                        
                        Type consType = tstack.pop();
                        
                        IConstructor cons;
                        if(annos > 0){
                            kwParamsOrAnnos = TrieMap_5Bits.transientOf();
                            for(int i = 0; i < annos; i++){
                                IValue val = vstack.pop();
                                IString ikey = (IString) vstack.pop();
                                kwParamsOrAnnos.__put(ikey.getValue(),  val);
                            }
                            cons =  vf.constructor(consType, vstack.getChildren(new IValue[arity])).asAnnotatable().setAnnotations(kwParamsOrAnnos);
                        } else if(kwparams > 0){
                            kwParamsOrAnnos = TrieMap_5Bits.transientOf();
                            for(int i = 0; i < kwparams; i++){
                                IValue val = vstack.pop();
                                IString ikey = (IString) vstack.pop();
                                kwParamsOrAnnos.__put(ikey.getValue(),  val);
                            }
                            cons = vf.constructor(consType, vstack.getChildren(new IValue[arity]), kwParamsOrAnnos);
                        } else {
                            cons = vf.constructor(consType, vstack.getChildren(new IValue[arity]));
                        }

                        pushAndCache(vstack, valueWindow, cons);
                        break;
                    }

                    case IValueIDs.DateTimeValue.ID: {
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
                                case IValueIDs.DateTimeValue.YEAR: year = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.MONTH: month = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.DAY: day = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.HOUR: hour = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.MINUTE: minute = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.SECOND: second = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.MILLISECOND: millisecond = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.TZ_HOUR: timeZoneHourOffset = (int)reader.getLong(); break;
                                case IValueIDs.DateTimeValue.TZ_MINUTE: timeZoneMinuteOffset = (int)reader.getLong(); break;
                            }
                        }
                        
                        
                        if (hour != null && year != null) {
                            pushAndCache(vstack, valueWindow, vf.datetime(year, month, day, hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset));
                        }
                        else if (hour != null) {
                            pushAndCache(vstack, valueWindow, vf.time(hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset));
                        }
                        else {
                            assert year != null;
                            pushAndCache(vstack, valueWindow, vf.datetime(year, month, day));
                        }

                        break;
                    }

                    case IValueIDs.IntegerValue.ID: {
                        Integer small = null;
                        byte[] big = null;
                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.IntegerValue.INTVALUE:  small = (int) reader.getLong(); break;
                                case IValueIDs.IntegerValue.BIGVALUE:    big = reader.getBytes(); break;
                            }
                        }
                        
                        if(small != null){
                            pushAndCache(vstack, valueWindow, vf.integer(small));
                        } else if(big != null){
                            pushAndCache(vstack, valueWindow, vf.integer(big));
                        } else {
                            throw new RuntimeException("Missing field in INT_VALUE");
                        }

                        break;
                    }

                    case IValueIDs.ListValue.ID: {
                        Integer size = null;
                        while (!reader.next().isEnd()) {
                            if(reader.field() == IValueIDs.ListValue.SIZE){
                                size = (int) reader.getLong();
                            }
                        }
                        
                        assert size != null;

                        pushAndCache(vstack,valueWindow,  vf.list(vstack.getChildren(new IValue[size])));
                        break;
                    }

                    case IValueIDs.SourceLocationValue.ID: {
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
                                case IValueIDs.SourceLocationValue.PREVIOUS_URI: previousURI = (int)reader.getLong(); break;
                                case IValueIDs.SourceLocationValue.SCHEME: scheme = reader.getString(); break;
                                case IValueIDs.SourceLocationValue.AUTHORITY: authority = reader.getString(); break;
                                case IValueIDs.SourceLocationValue.PATH: path = reader.getString(); break;
                                case IValueIDs.SourceLocationValue.QUERY: query = reader.getString(); break;	
                                case IValueIDs.SourceLocationValue.FRAGMENT: fragment = reader.getString(); break;	
                                case IValueIDs.SourceLocationValue.OFFSET: offset = (int) reader.getLong(); break;
                                case IValueIDs.SourceLocationValue.LENGTH: length = (int) reader.getLong(); break;
                                case IValueIDs.SourceLocationValue.BEGINLINE: beginLine = (int) reader.getLong(); break;
                                case IValueIDs.SourceLocationValue.ENDLINE: endLine = (int) reader.getLong(); break;
                                case IValueIDs.SourceLocationValue.BEGINCOLUMN: beginColumn = (int) reader.getLong(); break;
                                case IValueIDs.SourceLocationValue.ENDCOLUMN: endColumn = (int) reader.getLong(); break;
                            }
                        }
                        ISourceLocation loc;
                        if (previousURI != -1) {
                            loc = uriWindow.lookBack(previousURI);
                        } 
                        else {
                            loc = vf.sourceLocation(scheme, authority, path, query, fragment);
                            uriWindow.read(loc);
                        }

                        if(beginLine >= 0){
                            assert offset >= 0 && length >= 0 && endLine >= 0 && beginColumn >= 0 && endColumn >= 0;
                            loc = vf.sourceLocation(loc, offset, length, beginLine, endLine, beginColumn, endColumn);
                        } else if (offset >= 0){
                            assert length >= 0;
                            loc = vf.sourceLocation(loc, offset, length);
                        }

                        pushAndCache(vstack, valueWindow, loc);
                        break;

                    }
                    case IValueIDs.MapValue.ID: {
                        Long size = null;
                        while (!reader.next().isEnd()) {
                            if(reader.field() == IValueIDs.MapValue.SIZE){
                                size = reader.getLong();
                            }
                        }
                        
                        assert size != null;
                        
                        IMapWriter mw = vf.mapWriter();
                        for(int i = 0; i < size; i++){
                            IValue val = vstack.pop();
                            IValue key = vstack.pop();
                            mw.put(key, val);
                        }

                        pushAndCache(vstack, valueWindow, mw.done());
                        break;
                    }

                    case IValueIDs.NodeValue.ID: {
                        String name = null;
                        Integer arity = null;
                        int annos = 0;
                        int kwparams = 0;
                        TransientMap<String, IValue> kwParamsOrAnnos = null;

                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.NodeValue.NAME: name = reader.getString(); break;
                                case IValueIDs.NodeValue.ARITY: arity = (int)reader.getLong(); break;
                                case IValueIDs.NodeValue.KWPARAMS: kwparams = (int)reader.getLong(); break;
                                case IValueIDs.NodeValue.ANNOS: annos = (int)reader.getLong(); break;
                            }
                        }
                        
                        assert name != null && arity != null;
                        
                        INode node;
                        if(annos > 0){
                            kwParamsOrAnnos = TrieMap_5Bits.transientOf();
                            for(int i = 0; i < annos; i++){
                                IValue val = vstack.pop();
                                IString ikey = (IString) vstack.pop();
                                kwParamsOrAnnos.__put(ikey.getValue(),  val);
                            }
                            node =  vf.node(name, vstack.getChildren(new IValue[arity])).asAnnotatable().setAnnotations(kwParamsOrAnnos);
                        } else if(kwparams > 0){
                            kwParamsOrAnnos = TrieMap_5Bits.transientOf();
                            for(int i = 0; i < kwparams; i++){
                                IValue val = vstack.pop();
                                IString ikey = (IString) vstack.pop();
                                kwParamsOrAnnos.__put(ikey.getValue(),  val);
                            }
                            node = vf.node(name, vstack.getChildren(new IValue[arity]), kwParamsOrAnnos);
                        } else {
                            node = vf.node(name, vstack.getChildren(new IValue[arity]));
                        }

                        pushAndCache(vstack, valueWindow, node);
                        break;
                    }

                    case IValueIDs.RationalValue.ID: {
                        reader.skipMessage();
                        
                        IInteger denominator = (IInteger) vstack.pop();
                        IInteger numerator = (IInteger) vstack.pop();

                        pushAndCache(vstack, valueWindow, vf.rational(numerator, denominator));
                        break;
                    }

                    case IValueIDs.RealValue.ID: {
                        byte[] bytes = null;
                        Integer scale = null;

                        while (!reader.next().isEnd()) {
                            switch(reader.field()){
                                case IValueIDs.RealValue.SCALE:
                                    scale = (int) reader.getLong(); break;
                                case IValueIDs.RealValue.CONTENT:
                                    bytes = reader.getBytes(); break;
                            }
                        }

                        assert bytes != null && scale != null;

                        pushAndCache(vstack, valueWindow, vf.real(new BigDecimal(new BigInteger(bytes), scale).toString())); // TODO: Improve this?
                        break;
                    }

                    case IValueIDs.SetValue.ID: {
                        Integer size = 0;
                        while (!reader.next().isEnd()) {
                            if(reader.field() == IValueIDs.SetValue.SIZE){
                                size = (int) reader.getLong();
                            }
                        }

                        assert size != null;
                        
                        pushAndCache(vstack, valueWindow, vf.set(vstack.getChildren(new IValue[size])));
                        break;
                    }

                    case IValueIDs.StringValue.ID: {
                        String str = null;
                        while (!reader.next().isEnd()) {
                            if(reader.field() == IValueIDs.StringValue.CONTENT){
                                str = reader.getString();
                            }
                        }
                        
                        assert str != null;
                        
                        IString istr = vf.string(str);
                        vstack.push(istr);;
                        // Already cached at wire level
                        break;
                    }

                    case IValueIDs.TupleValue.ID: {
                        Integer len = 0;
                        while (!reader.next().isEnd()) {
                            if(reader.field() == IValueIDs.TupleValue.SIZE){
                                len = (int) reader.getLong();
                            }
                        }
                        
                        assert len != null;

                        pushAndCache(vstack, valueWindow, vf.tuple(vstack.getChildren(new IValue[len])));
                        break;
                    }

                    case IValueIDs.PreviousValue.ID: {
                        Integer n = null;
                        while(!reader.next().isEnd()){
                            if(reader.field() == IValueIDs.PreviousValue.HOW_FAR_BACK){
                                n = (int) reader.getLong();
                            }
                        }
                        
                        assert n != null;

                        IValue result = valueWindow.lookBack(n);
                        if (result == null) {
                            throw new IOException("Unexpected value cache miss");
                        }
                        vstack.push(result);    // Dont cache value twice
                        break;
                    }

                    default:
                        throw new IllegalArgumentException("readValue: " + reader.message());
                }
            }
            if(vstack.size() == 1){
                return vstack.pop();
            }
            else {
                throw new IOException("Premature EOF while reading value 1: " + reader.current());
            }
            
        } catch (IOException e) {
           if(vstack.size() == 1){
                return vstack.pop();
            } else {
                throw new IOException("Premature EOF while reading value 2: " + reader.current());
            }
        }
    }
}

class ReaderStack<Elem> {
	private Object[] elements;
	private int sp = 0;

	@SuppressWarnings("unchecked")
    ReaderStack(int capacity){
		elements = new Object[(int)Math.max(capacity, 16)];
	}
	
	public void push(Elem elem){
		if(sp == elements.length - 1){
			grow();
		}
		elements[sp] = elem;
		sp++;
	}
	
	@SuppressWarnings("unchecked")
    public Elem pop(){
		if(sp > 0){
			sp--;
			return (Elem) elements[sp];
		}
		throw new RuntimeException("Empty Stack");
	}
	
	public int size(){
		return sp;
	}
	
	@SuppressWarnings("unchecked")
	public Elem[] getChildren(Elem[] target){
	    if (target.length == 0) {
	        return target;
	    }
	    int from = sp - target.length;
	    if(from >= 0){
	        final Object[] elements = this.elements;
	        switch(target.length) {
	            // unrolled arrayCopy for the small arities
	            case 1:
	                target[0] = (Elem) elements[from + 0];
	                break;
	            case 2:
	                target[0] = (Elem) elements[from + 0];
	                target[1] = (Elem) elements[from + 1];
	                break;
	            case 3:
	                target[0] = (Elem) elements[from + 0];
	                target[1] = (Elem) elements[from + 1];
	                target[2] = (Elem) elements[from + 2];
	                break;
	            case 4:
	                target[0] = (Elem) elements[from + 0];
	                target[1] = (Elem) elements[from + 1];
	                target[2] = (Elem) elements[from + 2];
	                target[3] = (Elem) elements[from + 3];
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
		int newSize = (int)Math.min(elements.length * 2L, 0x7FFFFFF7); // max array size used by array list
		assert elements.length <= newSize;
		elements = Arrays.copyOf(elements, newSize);
	}
}
