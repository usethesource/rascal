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
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.NewStyleReader.ReaderPosition;
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
 * NewRVMIValueReader is a binary deserializer for IValues and Types. The main public functions are:
 * - readType
 * - readValue
 */

public class NewRVMIValueReader {
	private transient static IValueFactory vf;
	
	private NewStyleReader reader;

	private TypeFactory tf;
	private TypeStore store;
	private RascalTypeFactory rtf;
	
	private final TrackLastRead<Type> typeWindow;
	private final TrackLastRead<IValue> valueWindow;
	private final TrackLastRead<ISourceLocation> uriWindow;
	
	public NewRVMIValueReader(InputStream in, IValueFactory vfactory, TypeStore ts) throws IOException {
		tf = TypeFactory.getInstance();
		vf = vfactory;
		
		byte[] currentHeader = new byte[NewRVMIValueWriter.header.length];
        in.read(currentHeader);
        if (!Arrays.equals(NewRVMIValueWriter.header, currentHeader)) {
            throw new IOException("Unsupported file");
        }
       
        int typeWindowSize = in.read();
        int valueWindowSize = in.read();
        int uriWindowSize = in.read();
      
        typeWindow = new LinearCircularLookupWindow<>(typeWindowSize * 1024);
        valueWindow = new LinearCircularLookupWindow<>(valueWindowSize * 1024);
        uriWindow = new LinearCircularLookupWindow<>(uriWindowSize * 1024);
		
		this.reader = new NewStyleReader(in);
		store = ts;
		rtf = RascalTypeFactory.getInstance();
		store.extendStore(RascalValueFactory.getStore());
	}
	
	public NewStyleReader getIn() {
		return reader;
	}

	String readName() throws IOException{
		// TODO: add assert
		reader.next();
		return reader.getString();
	}
	
	String[] readNames() throws IOException{
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
	
	void pushAndCache(ReaderStack<Type> stack, Type type) throws IOException{
	    stack.push(type);
	    typeWindow.read(type);
	}

    void pushAndCache(ReaderStack<IValue> stack, IValue v) throws IOException{
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
                    
                    case Ser.BOOL_TYPE:  
                        skip_until_end();
                        pushAndCache(tstack, tf.boolType());
                        break;

                    case Ser.DATETIME_TYPE:    
                        skip_until_end();
                        pushAndCache(tstack, tf.dateTimeType());
                        break;

                    case Ser.INT_TYPE:     
                        skip_until_end(); 
                        pushAndCache(tstack, tf.integerType());
                        break;

                    case Ser.NODE_TYPE:        
                        skip_until_end();
                        pushAndCache(tstack, tf.nodeType());
                        break;

                    case Ser.NUMBER_TYPE:  
                        skip_until_end();
                        pushAndCache(tstack, tf.numberType());
                        break;

                    case Ser.RATIONAL_TYPE:     
                        skip_until_end();
                        pushAndCache(tstack, tf.rationalType());
                        break;

                    case Ser.REAL_TYPE:        
                        skip_until_end();
                        pushAndCache(tstack, tf.realType());
                        break;

                    case Ser.LOC_TYPE:     
                        skip_until_end();
                        pushAndCache(tstack, tf.sourceLocationType());
                        break;

                    case Ser.STR_TYPE:     
                        skip_until_end();
                        pushAndCache(tstack, tf.stringType());
                        break;

                    case Ser.VALUE_TYPE:       
                        skip_until_end();
                        pushAndCache(tstack, tf.valueType());
                        break;

                    case Ser.VOID_TYPE:        
                        skip_until_end();
                        pushAndCache(tstack, tf.voidType());
                        break;

                    // Composite types

                    case Ser.ADT_TYPE: {   
                        String name = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.ADT_NAME:
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

                    case Ser.ALIAS_TYPE:   {   
                        String name = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.ALIAS_NAME:
                                    name = reader.getString(); break;
                            }
                        }
                        
                        assert name != null;
                        
                        Type typeParameters = tstack.pop();
                        Type aliasedType = tstack.pop();

                        pushAndCache(tstack, tf.aliasType(store, name, aliasedType, typeParameters));
                        break;
                    }
                    
                    case Ser.CONSTRUCTOR_TYPE:     {
                        String name = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.CONSTRUCTOR_NAME:
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

                    case Ser.FUNCTION_TYPE:    {
                        skip_until_end();

                        Type keywordParameterTypes = tstack.pop();
                        Type argumentTypes =  tstack.pop();
                        Type returnType = tstack.pop();;


                        pushAndCache(tstack, rtf.functionType(returnType, argumentTypes, keywordParameterTypes));
                        break;
                    }

                    case Ser.REIFIED_TYPE: {
                        skip_until_end();
                        Type elemType = tstack.pop();

                        elemType = elemType.getFieldType(0);
                        pushAndCache(tstack, rtf.reifiedType(elemType));
                        break;
                    }

                    case Ser.OVERLOADED_TYPE: {
                        Integer size = null;

                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case Ser.OVERLOADED_SIZE:
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

                    case Ser.NONTERMINAL_TYPE: {
                        skip_until_end();

                        IConstructor nt = (IConstructor) vstack.pop();
                        pushAndCache(tstack, rtf.nonTerminalType(nt));
                        break;
                    }

                    case Ser.LIST_TYPE:    {
                        skip_until_end();

                        Type elemType = tstack.pop();

                        pushAndCache(tstack, tf.listType(elemType));
                        break;
                    }

                    case Ser.MAP_TYPE: {   
                        String keyLabel = null;
                        String valLabel = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.MAP_KEY_LABEL:
                                    keyLabel = reader.getString(); break;
                                case Ser.MAP_VAL_LABEL:
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

                    case Ser.PARAMETER_TYPE:   {
                        String name = null;

                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case Ser.PARAMETER_NAME:
                                    name = reader.getString();
                                    break;
                            }
                        }
                        assert name != null;
                        
                        Type bound = tstack.pop();
                        pushAndCache(tstack, tf.parameterType(name, bound));
                        break;
                    }

                    case Ser.SET_TYPE: {
                        skip_until_end();
                        Type elemType = tstack.pop();

                        pushAndCache(tstack, tf.setType(elemType));
                        break;
                    }

                    case Ser.TUPLE_TYPE: {
                        String [] fieldNames = null;

                        Integer arity = null;

                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case Ser.TUPLE_ARITY:
                                    arity = (int) reader.getLong(); break;

                                case Ser.TUPLE_NAMES:
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

                    case Ser.PREVIOUS_TYPE_ID: {
                        Long n = null;
                        while (not_at_end()) {
                            switch (reader.field()){ 
                                case Ser.PREVIOUS_ID:
                                    n = reader.getLong();
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
                    
                    case Ser.BOOL_VALUE: {
                        Integer b = null;
                        while (not_at_end()) {
                            if(reader.field() == Ser.BOOL_BOOL){
                                b = (int) reader.getLong();
                            }
                        }
                        
                        assert b != null;

                        pushAndCache(vstack, vf.bool(b == 0 ? false : true));
                        break;
                    }

                    case Ser.CONSTRUCTOR_VALUE:	{
                        Integer arity = null;
                        int annos = 0;
                        int kwparams = 0;
                        TransientMap<String, IValue> kwParamsOrAnnos = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.CONSTRUCTOR_ARITY: arity = (int) reader.getLong(); break;
                                case Ser.CONSTRUCTOR_KWPARAMS: kwparams = (int)reader.getLong(); break;
                                case Ser.CONSTRUCTOR_ANNOS: annos = (int)reader.getLong(); break;
                            }
                        }
                        Type consType = tstack.pop();
                        
                        if( arity == null || consType == null){
                            System.out.println("Something wrong here");;
                        }
                        
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

                    case Ser.DATETIME_VALUE: {
                        int year = 0;
                        int month = 0;
                        int day = 0;

                        int hour = 0;
                        int minute = 0;
                        int second = 0;
                        int millisecond = 0;

                        int timeZoneHourOffset = 0;
                        int timeZoneMinuteOffset = 0;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.DATETIME_YEAR: year = (int)reader.getLong(); break;
                                case Ser.DATETIME_MONTH: month = (int)reader.getLong(); break;
                                case Ser.DATETIME_DAY: day = (int)reader.getLong(); break;
                                case Ser.DATETIME_HOUR: hour = (int)reader.getLong(); break;
                                case Ser.DATETIME_MINUTE: minute = (int)reader.getLong(); break;
                                case Ser.DATETIME_SECOND: second = (int)reader.getLong(); break;
                                case Ser.DATETIME_MILLISECOND: millisecond = (int)reader.getLong(); break;
                                case Ser.DATETIME_TZ_HOUR: timeZoneHourOffset = (int)reader.getLong(); break;
                                case Ser.DATETIME_TZ_MINUTE: timeZoneMinuteOffset = (int)reader.getLong(); break;
                            }
                        }

                        pushAndCache(vstack, vf.datetime(year, month, day, hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset));
                        break;
                    }

                    case Ser.DATE_VALUE: {
                        int year = 0;
                        int month = 0;
                        int day = 0;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.DATE_YEAR: year = (int)reader.getLong(); break;
                                case Ser.DATE_MONTH: month = (int)reader.getLong(); break;
                                case Ser.DATE_DAY: day = (int)reader.getLong(); break;
                            }
                        }

                        pushAndCache(vstack, vf.datetime(year, month, day));
                        break;
                    }

                    case Ser.TIME_VALUE: {
                        int hour = 0;
                        int minute = 0;
                        int second = 0;
                        int millisecond = 0;

                        int timeZoneHourOffset = 0;
                        int timeZoneMinuteOffset = 0;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.TIME_HOUR: hour = (int)reader.getLong(); break;
                                case Ser.TIME_MINUTE: minute = (int)reader.getLong(); break;
                                case Ser.TIME_SECOND: second = (int)reader.getLong(); break;
                                case Ser.TIME_MILLISECOND: millisecond = (int)reader.getLong(); break;
                                case Ser.TIME_TZ_HOUR: timeZoneHourOffset = (int)reader.getLong(); break;
                                case Ser.TIME_TZ_MINUTE: timeZoneMinuteOffset = (int)reader.getLong(); break;
                            }
                        }

                        pushAndCache(vstack, vf.time(hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset));
                        break;
                    }

                    case Ser.INT_VALUE: {
                        Long n = null;
                        while (not_at_end()) {
                            if(reader.field() == Ser.INT_INT){
                                n = reader.getLong();
                            }
                        }
                        
                        assert n != null;

                        pushAndCache(vstack, vf.integer(n));
                        break;
                    }

                    case Ser.BIGINT_VALUE: {
                        byte[] bytes = null;
                        while (not_at_end()) {
                            if(reader.field() == Ser.BIGINT_BIGINT){
                                bytes = reader.getBytes();
                            }
                        }

                        assert bytes != null;

                        pushAndCache(vstack, vf.integer(bytes));
                        break;
                    }

                    case Ser.LIST_VALUE: {
                        Integer size = null;
                        while (not_at_end()) {
                            if(reader.field() == Ser.LIST_SIZE){
                                size = (int) reader.getLong();
                            }
                        }
                        
                        assert size != null;

                        pushAndCache(vstack, vf.list(vstack.getChildren(size)));
                        break;
                    }

                    case Ser.LOC_VALUE: {
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
                                case Ser.LOC_PREVIOUS_URI: previousURI = (int)reader.getLong(); break;
                                case Ser.LOC_SCHEME: scheme = reader.getString(); break;
                                case Ser.LOC_AUTHORITY: authority = reader.getString(); break;
                                case Ser.LOC_PATH: path = reader.getString(); break;
                                case Ser.LOC_QUERY: query = reader.getString(); break;	
                                case Ser.LOC_FRAGMENT: fragment = reader.getString(); break;	
                                case Ser.LOC_OFFSET: offset = (int) reader.getLong(); break;
                                case Ser.LOC_LENGTH: length = (int) reader.getLong(); break;
                                case Ser.LOC_BEGINLINE: beginLine = (int) reader.getLong(); break;
                                case Ser.LOC_ENDLINE: endLine = (int) reader.getLong(); break;
                                case Ser.LOC_BEGINCOLUMN: beginColumn = (int) reader.getLong(); break;
                                case Ser.LOC_ENDCOLUMN: endColumn = (int) reader.getLong(); break;
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
                    case Ser.MAP_VALUE:	{
                        Long size = null;
                        while (not_at_end()) {
                            if(reader.field() == Ser.MAP_SIZE){
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

                    case Ser.NODE_VALUE:	{
                        String name = null;
                        Integer arity = null;
                        int annos = 0;
                        int kwparams = 0;
                        TransientMap<String, IValue> kwParamsOrAnnos = null;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.NODE_NAME: name = reader.getString(); break;
                                case Ser.NODE_ARITY: arity = (int)reader.getLong(); break;
                                case Ser.NODE_KWPARAMS: kwparams = (int)reader.getLong(); break;
                                case Ser.NODE_ANNOS: annos = (int)reader.getLong(); break;
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

                    case Ser.RATIONAL_VALUE: {
                        skip_until_end();
                        
                        IInteger denominator = (IInteger) vstack.pop();
                        IInteger numerator = (IInteger) vstack.pop();

                        pushAndCache(vstack, vf.rational(numerator, denominator));
                        break;
                    }

                    case Ser.REAL_VALUE: {
                        byte[] bytes = null;
                        int scale = 1;

                        while (not_at_end()) {
                            switch(reader.field()){
                                case Ser.REAL_SCALE:
                                    scale = (int) reader.getLong(); break;
                                case Ser.REAL_REAL:
                                    bytes = reader.getBytes(); break;
                            }
                        }

                        assert bytes != null;

                        pushAndCache(vstack, vf.real(new BigDecimal(new BigInteger(bytes), scale).toString())); // TODO: Improve this?
                        break;
                    }

                    case Ser.SET_VALUE: {
                        Integer size = 0;
                        while (not_at_end()) {
                            if(reader.field() == Ser.SET_SIZE){
                                size = (int) reader.getLong();
                            }
                        }

                        assert size != null;
                        
                        pushAndCache(vstack, vf.set(vstack.getChildren(size)));
                        break;
                    }

                    case Ser.STR_VALUE: {
                        String str = null;
                        while (not_at_end()) {
                            if(reader.field() == Ser.STR_STR){
                                str = reader.getString();
                            }
                        }
                        
                        assert str != null;
                        
                        IString istr = vf.string(str);
                        vstack.push(istr);;
                        // Already cached at wire level
                        break;
                    }

                    case Ser.TUPLE_VALUE: {
                        Integer len = 0;
                        while (not_at_end()) {
                            if(reader.field() == Ser.TUPLE_SIZE){
                                len = (int) reader.getLong();
                            }
                        }
                        
                        assert len != null;

                        pushAndCache(vstack, vf.tuple(vstack.getChildren(len)));
                        break;
                    }

                    case Ser.PREVIOUS_VALUE: {
                        Integer n = null;
                        while(not_at_end()){
                            if(reader.field() == Ser.PREVIOUS_VALUE_ID){
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

class ReaderStack<E> {
	private E[] elements;
	int capacity;
	private int sp = 0;
    private final Class<E> eclass;

	@SuppressWarnings("unchecked")
    ReaderStack(Class<E> eclass, int capacity){
		this.capacity = (int)Math.max(capacity, 16);
		elements = (E[]) Array.newInstance(eclass, this.capacity);
		this.eclass = eclass;
	}
	
	public void push(E leaf){
		if(sp == capacity - 1){
			grow();
		}
		elements[sp] = leaf;
		sp++;
	}
	
	public E pop(){
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
    public E[] getChildren(int childs){
		int from = sp - childs;
		E[] children = (E[]) Array.newInstance(eclass,childs);
		if(from >= 0){
			for(int i = 0; i < childs; i++){
				children[i] = elements[from + i];
			}
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
		E[] newElements = (E[]) Array.newInstance(eclass, newSize);
		System.arraycopy(elements, 0, newElements, 0, sp);
		elements = newElements;
	}
}
