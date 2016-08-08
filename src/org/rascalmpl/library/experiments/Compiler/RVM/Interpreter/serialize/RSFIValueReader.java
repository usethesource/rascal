package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.RSFReader.ReaderPosition;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.LinearCircularLookupWindow;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.TrackLastRead;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.uptr.RascalValueFactory;

import io.usethesource.capsule.TransientMap;
import io.usethesource.capsule.TrieMap_5Bits;

/**
 * RSFIValueReader is a binary deserializer for IValues and Types. The main public function is:
 * - readValue
 */

public class RSFIValueReader {
	private transient static IValueFactory vf;
	
	private RSFReader reader;

	private TypeFactory tf;
	private TypeStore store;
	private RascalTypeFactory rtf;
	
	private final TrackLastRead<Type> typeWindow;
	private final TrackLastRead<IValue> valueWindow;
	private final TrackLastRead<ISourceLocation> uriWindow;
	
	public RSFIValueReader(InputStream in, IValueFactory vfactory, TypeStore ts) throws IOException {
		tf = TypeFactory.getInstance();
		vf = vfactory;
		
		byte[] currentHeader = new byte[RSFIValueWriter.header.length];
        in.read(currentHeader);
        if (!Arrays.equals(RSFIValueWriter.header, currentHeader)) {
            throw new IOException("Unsupported file");
        }
       
        int typeWindowSize = in.read();
        int valueWindowSize = in.read();
        int uriWindowSize = in.read();
      
        typeWindow = new LinearCircularLookupWindow<>(typeWindowSize * 1024);
        valueWindow = new LinearCircularLookupWindow<>(valueWindowSize * 1024);
        uriWindow = new LinearCircularLookupWindow<>(uriWindowSize * 1024);
		
		this.reader = new RSFReader(in);
		store = ts;
		rtf = RascalTypeFactory.getInstance();
		store.extendStore(RascalValueFactory.getStore());
	}
	
	public RSFReader getIn() {
		return reader;
	}

	private String readName() throws IOException{
		// TODO: add assert
		reader.next();
		return reader.getString();
	}
	
	public String[] readNames() throws IOException{
		reader.next();
		int n = (int)reader.getLong();
		String[] names = new String[n];
		for(int i = 0; i < n; i++){
			names[i] = readName();
		}
		return names;
	}
	
	private boolean not_at_end() throws IOException{
		return reader.next() != ReaderPosition.VALUE_END;
	}
	
	private void skip_until_end() throws IOException{
		while (not_at_end());
	}
	
	private void pushAndCache(ReaderStack<Type> stack, Type type) throws IOException{
	    stack.push(type);
	    typeWindow.read(type);
	}

    private void pushAndCache(ReaderStack<IValue> stack, IValue v) throws IOException{
		stack.push(v);
		valueWindow.read(v);
	}
	
	/**
	 * @return a value read from the input stream.
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
    public IValue readValue() throws IOException, URISyntaxException {

        ReaderStack<Type> tstack = new ReaderStack<Type>(Type.class, 100);
        ReaderStack<IValue> vstack = new ReaderStack<IValue>(IValue.class, 1024);

        try {
           
            while(reader.next() == ReaderPosition.VALUE_START){
                
                switch (reader.value()) {
                    
                    /********************************/
                    /*          Types               */
                    /********************************/
                    
                    case RSF.BoolType.ID:  
                        skip_until_end();
                        pushAndCache(tstack, tf.boolType());
                        break;

                    case RSF.DateTimeType.ID:    
                        skip_until_end();
                        pushAndCache(tstack, tf.dateTimeType());
                        break;

                    case RSF.IntegerType.ID:     
                        skip_until_end(); 
                        pushAndCache(tstack, tf.integerType());
                        break;

                    case RSF.NodeType.ID:        
                        skip_until_end();
                        pushAndCache(tstack, tf.nodeType());
                        break;

                    case RSF.NumberType.ID:  
                        skip_until_end();
                        pushAndCache(tstack, tf.numberType());
                        break;

                    case RSF.RationalType.ID:     
                        skip_until_end();
                        pushAndCache(tstack, tf.rationalType());
                        break;

                    case RSF.RealType.ID:        
                        skip_until_end();
                        pushAndCache(tstack, tf.realType());
                        break;

                    case RSF.SourceLocationType.ID:     
                        skip_until_end();
                        pushAndCache(tstack, tf.sourceLocationType());
                        break;

                    case RSF.StringType.ID:     
                        skip_until_end();
                        pushAndCache(tstack, tf.stringType());
                        break;

                    case RSF.ValueType.ID:       
                        skip_until_end();
                        pushAndCache(tstack, tf.valueType());
                        break;

                    case RSF.VoidType.ID:        
                        skip_until_end();
                        pushAndCache(tstack, tf.voidType());
                        break;

                    // Composite types

                    case RSF.ADTType.ID: {   
                        String name = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.ADTType.NAME:
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
                            pushAndCache(tstack, tf.abstractDataType(store, name, targs));
                        } else {
                            pushAndCache(tstack, tf.abstractDataType(store, name));
                        }
                        break;
                    }

                    case RSF.AliasType.ID:   {   
                        String name = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.AliasType.NAME:
                                    name = reader.getString(); break;
                            }
                        }
                        
                        assert name != null;
                        
                        Type typeParameters = tstack.pop();
                        Type aliasedType = tstack.pop();

                        pushAndCache(tstack, tf.aliasType(store, name, aliasedType, typeParameters));
                        break;
                    }
                    
                    case RSF.ConstructorType.ID:     {
                        String name = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.ConstructorType.NAME:
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
                                pushAndCache(tstack, tf.constructor(store, adtType, name, fieldTypesAr));
                            } else {
                                pushAndCache(tstack, res);
                            }
                        } else {
                            Object[] typeAndNames = new Object[2*arity];
                            for(int i = 0; i < arity; i++){
                                typeAndNames[2 * i] =  fieldTypesAr[i];
                                typeAndNames[2 * i + 1] = fieldNames[i];
                            }

                            Type res = store.lookupConstructor(adtType, name, tf.tupleType(typeAndNames));
                            if(res == null){
                                pushAndCache(tstack, tf.constructor(store, adtType, name, typeAndNames));
                            } else {
                                pushAndCache(tstack, res);
                            }
                        }
                        break;
                    }

                    // External

                    case RSF.FunctionType.ID:    {
                        skip_until_end();

                        Type keywordParameterTypes = tstack.pop();
                        Type argumentTypes =  tstack.pop();
                        Type returnType = tstack.pop();;


                        pushAndCache(tstack, rtf.functionType(returnType, argumentTypes, keywordParameterTypes));
                        break;
                    }

                    case RSF.ReifiedType.ID: {
                        skip_until_end();
                        Type elemType = tstack.pop();

                        elemType = elemType.getFieldType(0);
                        pushAndCache(tstack, rtf.reifiedType(elemType));
                        break;
                    }

                    case RSF.OverloadedType.ID: {
                        Integer size = null;

                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case RSF.OverloadedType.SIZE:
                                    size = (int) reader.getLong();
                                    break;
                            }
                        }

                        assert size != null;

                        Set<FunctionType> alternatives = new HashSet<FunctionType>(size);
                        for(int i = 0; i < size; i++){
                            alternatives.add((FunctionType) tstack.pop());
                        }
                        pushAndCache(tstack, rtf.overloadedFunctionType(alternatives));
                        break;
                    }

                    case RSF.NonTerminalType.ID: {
                        skip_until_end();

                        IConstructor nt = (IConstructor) vstack.pop();
                        pushAndCache(tstack, rtf.nonTerminalType(nt));
                        break;
                    }

                    case RSF.ListType.ID:    {
                        skip_until_end();

                        Type elemType = tstack.pop();

                        pushAndCache(tstack, tf.listType(elemType));
                        break;
                    }

                    case RSF.MapType.ID: {   
                        String keyLabel = null;
                        String valLabel = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.MapType.KEY_LABEL:
                                    keyLabel = reader.getString(); break;
                                case RSF.MapType.VAL_LABEL:
                                    valLabel = reader.getString(); break;
                            }
                        }

                        Type valType = tstack.pop();
                        Type keyType = tstack.pop();

                        if(keyLabel == null){
                            pushAndCache(tstack, tf.mapType(keyType, valType));
                        } else {
                            assert valLabel != null;
                            pushAndCache(tstack, tf.mapType(keyType, keyLabel, valType, valLabel));
                        }
                        break;
                    }

                    case RSF.ParameterType.ID:   {
                        String name = null;

                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case RSF.ParameterType.NAME:
                                    name = reader.getString();
                                    break;
                            }
                        }
                        assert name != null;
                        
                        Type bound = tstack.pop();
                        pushAndCache(tstack, tf.parameterType(name, bound));
                        break;
                    }

                    case RSF.SetType.ID: {
                        skip_until_end();
                        Type elemType = tstack.pop();

                        pushAndCache(tstack, tf.setType(elemType));
                        break;
                    }

                    case RSF.TupleType.ID: {
                        String [] fieldNames = null;

                        Integer arity = null;

                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case RSF.TupleType.ARITY:
                                    arity = (int) reader.getLong(); break;

                                case RSF.TupleType.NAMES:
                                    int n = (int) reader.getLong();
                                    fieldNames = new String[n];
                                    for(int i = 0; i < n; i++){
                                        reader.next();
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
                            pushAndCache(tstack, tf.tupleType(elemTypes, fieldNames));
                        } else {
                            pushAndCache(tstack, tf.tupleType(elemTypes));
                        }
                        break;
                    }

                    case RSF.PreviousType.ID: {
                        Long n = null;
                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case RSF.PreviousType.HOW_LONG_AGO:
                                    n = reader.getLong();
                                    break;
                            }
                        }

                        assert n != null;
                        
                        Type type = typeWindow.lookBack(n.intValue());
                        if(type == null){
                            throw new RuntimeException("Unexpected type cache miss");
                        }
                        System.out.println("Previous type: " + type + ", " + n);
                        tstack.push(type);  // do not cache type twice
                        break;
                    }
                    
                    
                    /********************************/
                    /*          Values              */
                    /********************************/
                    
                    case RSF.BoolValue.ID: {
                        Integer b = null;
                        while (not_at_end()) {
                            if(reader.field() == RSF.BoolValue.VALUE){
                                b = (int) reader.getLong();
                            }
                        }
                        
                        assert b != null;

                        pushAndCache(vstack, vf.bool(b == 0 ? false : true));
                        break;
                    }

                    case RSF.ConstructorValue.ID:	{
                        Integer arity = null;
                        int annos = 0;
                        int kwparams = 0;
                        TransientMap<String, IValue> kwParamsOrAnnos = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.ConstructorValue.ARITY: arity = (int) reader.getLong(); break;
                                case RSF.ConstructorValue.KWPARAMS: kwparams = (int)reader.getLong(); break;
                                case RSF.ConstructorValue.ANNOS: annos = (int)reader.getLong(); break;
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
                            cons =  vf.constructor(consType, vstack.getChildren(arity)).asAnnotatable().setAnnotations(kwParamsOrAnnos);
                        } else if(kwparams > 0){
                            kwParamsOrAnnos = TrieMap_5Bits.transientOf();
                            for(int i = 0; i < kwparams; i++){
                                IValue val = vstack.pop();
                                IString ikey = (IString) vstack.pop();
                                kwParamsOrAnnos.__put(ikey.getValue(),  val);
                            }
                            cons = vf.constructor(consType, vstack.getChildren(arity), kwParamsOrAnnos);
                        } else {
                            cons = vf.constructor(consType, vstack.getChildren(arity));
                        }

                        pushAndCache(vstack, cons);
                        break;
                    }

                    case RSF.DateTimeValue.ID: {
                        Integer year = null;;
                        Integer month = null;
                        Integer day = null;

                        Integer hour = null;
                        Integer minute = null;
                        Integer second = null;
                        Integer millisecond = null;

                        Integer timeZoneHourOffset = null;
                        Integer timeZoneMinuteOffset = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.DateTimeValue.YEAR: year = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.MONTH: month = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.DAY: day = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.HOUR: hour = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.MINUTE: minute = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.SECOND: second = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.MILLISECOND: millisecond = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.TZ_HOUR: timeZoneHourOffset = (int)reader.getLong(); break;
                                case RSF.DateTimeValue.TZ_MINUTE: timeZoneMinuteOffset = (int)reader.getLong(); break;
                            }
                        }
                        
                        
                        if (hour != null && year != null) {
                            pushAndCache(vstack, vf.datetime(year, month, day, hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset));
                        }
                        else if (hour != null) {
                            pushAndCache(vstack, vf.time(hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset));
                        }
                        else {
                            assert year != null;
                            pushAndCache(vstack, vf.datetime(year, month, day));
                        }

                        break;
                    }

                    case RSF.IntegerValue.ID: {
                        Integer small = null;
                        byte[] big = null;
                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.IntegerValue.INTVALUE:  small = (int) reader.getLong(); break;
                                case RSF.IntegerValue.BIGVALUE:    big = reader.getBytes(); break;
                            }
                        }
                        
                        if(small != null){
                            pushAndCache(vstack, vf.integer(small));
                        } else if(big != null){
                            pushAndCache(vstack, vf.integer(big));
                        } else {
                            throw new RuntimeException("Missing field in INT_VALUE");
                        }

                        break;
                    }

                    case RSF.ListValue.ID: {
                        Integer size = null;
                        while (not_at_end()) {
                            if(reader.field() == RSF.ListValue.SIZE){
                                size = (int) reader.getLong();
                            }
                        }
                        
                        assert size != null;

                        pushAndCache(vstack, vf.list(vstack.getChildren(size)));
                        break;
                    }

                    case RSF.SourceLocationValue.ID: {
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
                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.SourceLocationValue.PREVIOUS_URI: previousURI = (int)reader.getLong(); break;
                                case RSF.SourceLocationValue.SCHEME: scheme = reader.getString(); break;
                                case RSF.SourceLocationValue.AUTHORITY: authority = reader.getString(); break;
                                case RSF.SourceLocationValue.PATH: path = reader.getString(); break;
                                case RSF.SourceLocationValue.QUERY: query = reader.getString(); break;	
                                case RSF.SourceLocationValue.FRAGMENT: fragment = reader.getString(); break;	
                                case RSF.SourceLocationValue.OFFSET: offset = (int) reader.getLong(); break;
                                case RSF.SourceLocationValue.LENGTH: length = (int) reader.getLong(); break;
                                case RSF.SourceLocationValue.BEGINLINE: beginLine = (int) reader.getLong(); break;
                                case RSF.SourceLocationValue.ENDLINE: endLine = (int) reader.getLong(); break;
                                case RSF.SourceLocationValue.BEGINCOLUMN: beginColumn = (int) reader.getLong(); break;
                                case RSF.SourceLocationValue.ENDCOLUMN: endColumn = (int) reader.getLong(); break;
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

                        pushAndCache(vstack, loc);
                        break;

                    }
                    case RSF.MapValue.ID: {
                        Long size = null;
                        while (not_at_end()) {
                            if(reader.field() == RSF.MapValue.SIZE){
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

                        pushAndCache(vstack, mw.done());
                        break;
                    }

                    case RSF.NodeValue.ID: {
                        String name = null;
                        Integer arity = null;
                        int annos = 0;
                        int kwparams = 0;
                        TransientMap<String, IValue> kwParamsOrAnnos = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.NodeValue.NAME: name = reader.getString(); break;
                                case RSF.NodeValue.ARITY: arity = (int)reader.getLong(); break;
                                case RSF.NodeValue.KWPARAMS: kwparams = (int)reader.getLong(); break;
                                case RSF.NodeValue.ANNOS: annos = (int)reader.getLong(); break;
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
                            node =  vf.node(name, vstack.getChildren(arity)).asAnnotatable().setAnnotations(kwParamsOrAnnos);
                        } else if(kwparams > 0){
                            kwParamsOrAnnos = TrieMap_5Bits.transientOf();
                            for(int i = 0; i < kwparams; i++){
                                IValue val = vstack.pop();
                                IString ikey = (IString) vstack.pop();
                                kwParamsOrAnnos.__put(ikey.getValue(),  val);
                            }
                            node = vf.node(name, vstack.getChildren(arity), kwParamsOrAnnos);
                        } else {
                            node = vf.node(name, vstack.getChildren(arity));
                        }

                        pushAndCache(vstack, node);
                        break;
                    }

                    case RSF.RationalValue.ID: {
                        skip_until_end();
                        
                        IInteger denominator = (IInteger) vstack.pop();
                        IInteger numerator = (IInteger) vstack.pop();

                        pushAndCache(vstack, vf.rational(numerator, denominator));
                        break;
                    }

                    case RSF.RealValue.ID: {
                        byte[] bytes = null;
                        Integer scale = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case RSF.RealValue.SCALE:
                                    scale = (int) reader.getLong(); break;
                                case RSF.RealValue.CONTENT:
                                    bytes = reader.getBytes(); break;
                            }
                        }

                        assert bytes != null && scale != null;

                        pushAndCache(vstack, vf.real(new BigDecimal(new BigInteger(bytes), scale).toString())); // TODO: Improve this?
                        break;
                    }

                    case RSF.SetValue.ID: {
                        Integer size = 0;
                        while (not_at_end()) {
                            if(reader.field() == RSF.SetValue.SIZE){
                                size = (int) reader.getLong();
                            }
                        }

                        assert size != null;
                        
                        pushAndCache(vstack, vf.set(vstack.getChildren(size)));
                        break;
                    }

                    case RSF.StringValue.ID: {
                        String str = null;
                        while (not_at_end()) {
                            if(reader.field() == RSF.StringValue.CONTENT){
                                str = reader.getString();
                            }
                        }
                        
                        assert str != null;
                        
                        IString istr = vf.string(str);
                        vstack.push(istr);;
                        // Already cached at wire level
                        break;
                    }

                    case RSF.TupleValue.ID: {
                        Integer len = 0;
                        while (not_at_end()) {
                            if(reader.field() == RSF.TupleValue.SIZE){
                                len = (int) reader.getLong();
                            }
                        }
                        
                        assert len != null;

                        pushAndCache(vstack, vf.tuple(vstack.getChildren(len)));
                        break;
                    }

                    case RSF.PreviousValue.ID: {
                        Integer n = null;
                        while(not_at_end()){
                            if(reader.field() == RSF.PreviousValue.HOW_FAR_BACK){
                                n = (int) reader.getLong();
                            }
                        }
                        
                        assert n != null;

                        IValue result = valueWindow.lookBack(n);
                        if (result == null) {
                            throw new IOException("Unexpected value cache miss");
                        }
                        System.out.println("PREVIOUS value: " + result + ", " + n);
                        vstack.push(result);    // Dont cache value twice
                        break;
                    }

                    default:
                        throw new IllegalArgumentException("readValue: " + reader.value());
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
	private Elem[] elements;
	int capacity;
	private int sp = 0;
    private final Class<Elem> eclass;

	@SuppressWarnings("unchecked")
    ReaderStack(Class<Elem> eclass, int capacity){
		this.capacity = (int)Math.max(capacity, 16);
		elements = (Elem[]) Array.newInstance(eclass, this.capacity);
		this.eclass = eclass;
	}
	
	public void push(Elem elem){
		if(sp == capacity - 1){
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
	
	@SuppressWarnings("unchecked")
	public Elem[] getChildren(int arity){
	    int from = sp - arity;
	    Elem[] children = (Elem[]) Array.newInstance(eclass, arity);
	    if(from >= 0){
	        System.arraycopy(elements, from, children, 0, arity);
	        sp = from;
	        return children;
	    }
	    throw new RuntimeException("Empty Stack");
	}
	
	@SuppressWarnings("unchecked")
    private void grow() {
		int newSize = (int)Math.min(capacity * 2L, 0x7FFFFFF7); // max array size used by array list
		assert capacity <= newSize;
		capacity = newSize;
		Elem[] newElements = (Elem[]) Array.newInstance(eclass, newSize);
		System.arraycopy(elements, 0, newElements, 0, sp);
		elements = newElements;
	}
}
