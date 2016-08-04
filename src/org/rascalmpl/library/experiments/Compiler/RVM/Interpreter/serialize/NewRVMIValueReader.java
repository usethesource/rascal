package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.io.InputStream;
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
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
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
      
        typeWindow = new LinearCircularLookupWindow<>(typeWindowSize * 1024);
        valueWindow = new LinearCircularLookupWindow<>(valueWindowSize * 1024);
		
		this.reader = new NewStyleReader(in);
		store = ts;
		rtf = RascalTypeFactory.getInstance();
		store.extendStore(RascalValueFactory.getStore());
	}
	
	public NewStyleReader getIn() {
		return reader;
	}

	String readName() throws IOException{
		ReaderPosition x = reader.next();
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
	
	/**
	 * @return a type from the input stream. Types are shared when possible.
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	Type readType() throws IOException, URISyntaxException{
		Type type = readType1();
		typeWindow.read(type);
		return type;
	}
	
	@SuppressWarnings("deprecation")
	private Type readType1() throws IOException, URISyntaxException{

		assert reader.current() == ReaderPosition.VALUE_START;

		reader.next();

		Type[] zeroElemTypes = new Type[0];
		String[] zeroFieldNames = new String[0];
	
		switch(reader.value()){

		// Atomic types

		case SType.BOOL:	
			skip_until_end();	return tf.boolType();
		case SType.DATETIME:	
			skip_until_end();return tf.dateTimeType();
		case SType.INT:		
			skip_until_end();return tf.integerType();
		case SType.NODE:		
			skip_until_end();return tf.nodeType();
		case SType.NUMBER:	
			skip_until_end();return tf.numberType();
		case SType.RAT:		
			skip_until_end();return tf.rationalType();
		case SType.REAL:		
			skip_until_end();return tf.realType();
		case SType.LOC:		
			skip_until_end();return tf.sourceLocationType();
		case SType.STR:		
			skip_until_end();return tf.stringType();
		case SType.VALUE:		
			skip_until_end();return tf.valueType();
		case SType.VOID:		
			skip_until_end();return tf.voidType();

			// Composite types

		case SType.ADT:	{	
			String name = "";
			Type typeParameters = tf.voidType();
			
			while (not_at_end()) {
				switch(reader.field()){
				case SType.ADT_NAME:
					name = reader.getString(); break;
				case SType.ADT_TYPE_PARAMETERS:
					typeParameters = readType(); break;
				}
			}
			int arity = typeParameters.getArity();
			if(arity > 0){
				Type targs[] = new Type[arity];
				for(int i = 0; i < arity; i++){
					targs[i] = typeParameters.getFieldType(i);
				}
				return tf.abstractDataType(store, name, targs);
			}
			return tf.abstractDataType(store, name);
		}

		case SType.ALIAS:	{	
			String name = "";
			Type aliasedType = tf.valueType();
			Type typeParameters = tf.voidType();
			
			while (not_at_end()) {
				switch(reader.field()){
				case SType.ALIAS_NAME:
					name = reader.getString(); break;
				case SType.ALIAS_ALIASED:
					aliasedType = readType(); break;
				case SType.ALIAS_TYPE_PARAMETERS:
					typeParameters = readType(); break;
				}
			}
			
			return tf.aliasType(store, name, aliasedType, typeParameters);
		}
		case SType.CONSTRUCTOR: 	{
			String name = "";
			int arity = 0;
			String [] fieldNames = zeroFieldNames;
			Type adtType = tf.voidType();
			Type fieldTypes = tf.voidType();
			
			while (not_at_end()) {
				switch(reader.field()){
				case SType.CONSTRUCTOR_NAME:
					name = reader.getString(); break;
				case SType.CONSTRUCTOR_ABSTRACT_DATA_TYPE:
					adtType = readType(); break;
				case SType.CONSTRUCTOR_TYPE:
					fieldTypes = readType(); break;
				}
			}

			Type declaredAdt = store.lookupAbstractDataType(name);

			if(declaredAdt != null){
				adtType = declaredAdt;
			}
			
			arity = fieldTypes.getArity();
			fieldNames = fieldTypes.getFieldNames();

			Type fieldTypesAr[] = new Type[arity];
			
			for(int i = 0; i < arity; i++){
				fieldTypesAr[i] = fieldTypes.getFieldType(i);
			}

			if(fieldNames == null){
				Type res = store.lookupConstructor(adtType, name, tf.tupleType(fieldTypes));
				if(res == null) {
					return tf.constructor(store, adtType, name, fieldTypes);
				} else {
					return res;
				}
			}
			Object[] typeAndNames = new Object[2*arity];
			for(int i = 0; i < arity; i++){
				typeAndNames[2 * i] =  fieldTypesAr[i];
				typeAndNames[2 * i + 1] = fieldNames[i];
			}

			Type res = store.lookupConstructor(adtType, name, tf.tupleType(typeAndNames));
			if(res == null){
				return tf.constructor(store, adtType, name, typeAndNames);
			} else {
				return res;
			}
		}

			// External

		case SType.FUNCTION:	{
			Type returnType = null;
			Type argumentTypes =  null;
			Type keywordParameterTypes = null;
			
			while (not_at_end()) {
				switch(reader.field()){
				case SType.FUNCTION_RETURN_TYPE:
					returnType = readType(); break;
				case SType.FUNCTION_ARGUMENT_TYPES:
					argumentTypes = readType(); break;
				case SType.FUNCTION_KEYWORD_PARAMETER_TYPES:
					keywordParameterTypes = readType(); break;
				}
			}
			assert returnType != null && argumentTypes != null;
			
			return rtf.functionType(returnType, argumentTypes, keywordParameterTypes);
		}

		case SType.REIFIED: {
			Type elemType = null;
			while (not_at_end()) {
				if(reader.field() == SType.REIFIED_ELEMENT_TYPE){
					elemType = readType(); break;
				}
			}
			assert elemType != null;
			elemType = elemType.getFieldType(0);
			return rtf.reifiedType(elemType);
		}

		case SType.OVERLOADED: {
			int arity = 0;
			Type[] elemTypes = null;
			
			while (not_at_end()) {
				switch (reader.field()){ 
				case SType.OVERLOADED_TYPES:
					arity = (int) reader.getLong();
					elemTypes = new Type[arity];
					for(int i = 0; i < arity; i++){
						elemTypes[i] = readType();
					}
					break;
				}
			}
			
			Set<FunctionType> alternatives = new HashSet<FunctionType>(arity);
			for(int i = 0; i < arity; i++){
				alternatives.add((FunctionType) elemTypes[i]);
			}
			return rtf.overloadedFunctionType(alternatives);
		}

		case SType.NONTERMINAL: {
			IConstructor nt = null;
			while (not_at_end()) {
				if (reader.field() == SType.NONTERMINAL_CONSTRUCTOR){
					nt = (IConstructor) readValue(); break;
				}
			}
			assert nt != null;
			return rtf.nonTerminalType(nt);
		}
			
		case SType.LIST:	{
			Type elemType = tf.voidType();
			
			while (not_at_end()) {
				if (reader.field() == SType.LIST_ELEMENT_TYPE) {
					elemType = readType();
				}
			}
			return tf.listType(elemType);
		}

		case SType.MAP: {	
			String keyLabel = null;
			String valLabel = null;
			Type keyType = tf.voidType();
			Type valType = tf.voidType();
			
			while (not_at_end()) {
				switch(reader.field()){
				case SType.MAP_KEY_LABEL:
					keyLabel = reader.getString(); break;
				case SType.MAP_VAL_LABEL:
					valLabel = reader.getString(); break;
				case SType.MAP_KEY_TYPE:
					keyType = readType(); break;	
				case SType.MAP_VAL_TYPE:
					valType = readType(); break;
				}
			}
			
			if(keyLabel == null){
				return tf.mapType(keyType, valType);
			}
			return tf.mapType(keyType, keyLabel, valType, valLabel);
		}

		case SType.PARAMETER:	{
			String name = "";
			Type bound = tf.valueType();
			
			while (not_at_end()) {
				switch (reader.field()){ 
				case SType.PARAMETER_NAME:
					name = reader.getString();
					break;
				case SType.PARAMETER_BOUND:
					bound = readType(); break;
				}
			}
			return tf.parameterType(name, bound);
		}

		case SType.SET: {
			Type elemType = tf.voidType();
			while (not_at_end()) {
				if (reader.field() == SType.SET_ELEMENT_TYPE) {
					elemType = readType();
				}
			}
			return tf.setType(elemType);
		}

		case SType.TUPLE: {
			String [] fieldNames = null;
			Type[] elemTypes = zeroElemTypes;
			int arity = 0;

			while (not_at_end()) {
				switch (reader.field()){ 
				case SType.TUPLE_TYPES:
					arity = (int) reader.getLong();
					elemTypes = new Type[arity];
					for(int i = 0; i < arity; i++){
						elemTypes[i] = readType();
					}
					break;
				case SType.TUPLE_NAMES:
					int n = (int) reader.getLong();
					fieldNames = new String[n];
					for(int i = 0; i < n; i++){
						reader.next();
						fieldNames[i] = reader.getString();
					}
				}
			}

			if(fieldNames != null){
				assert fieldNames.length == arity;
				return tf.tupleType(elemTypes, fieldNames);
			}
			return tf.tupleType(elemTypes);
		}

		case SType.PREVIOUS: {
			int n = -1;
			while (not_at_end()) {
				switch (reader.field()){ 
				case SType.PREVIOUS_TYPE:
					n = (int)reader.getLong();
				}
			}
			
			Type type = typeWindow.lookBack(n);
			if(type == null){
				throw new RuntimeException("Unexpected type cache miss");
			}
			return type;
		}
		}
		throw new RuntimeException("readType: unhandled case " + reader.value());
	}
	
	/**
	 * @return a value read from the input stream.
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public IValue readValue() throws IOException, URISyntaxException{
		reader.next();
		assert reader.current() == ReaderPosition.VALUE_START;

		ReaderStack stack = new ReaderStack(1024);
		while(true){

			switch (reader.value()) {

			case SValue.BOOL: {
				int b = 0;
				while (not_at_end()) {
					if(reader.field() == SValue.BOOL_VALUE){
						b = (int) reader.getLong();
					}
				}

				IValue bval = vf.bool(b == 0 ? false : true);
				stack.push(bval);
				valueWindow.read(bval);
				reader.next();
				break;
			}
			
			case SValue.CONSTRUCTOR:	{
				int arity = 0;
				int annos = 0;
				int kwparams = 0;
				Type consType = tf.voidType();
				TransientMap<String, IValue> kwParamsOrAnnos = null;

				while (not_at_end()) {
					switch(reader.field()){
					case SValue.CONSTRUCTOR_ARITY: arity = (int)reader.getLong(); break;
					case SValue.CONSTRUCTOR_KWPARAMS: kwparams = (int)reader.getLong(); break;
					case SValue.CONSTRUCTOR_ANNOS: annos = (int)reader.getLong(); break;
					case SValue.CONSTRUCTOR_TYPE: consType = readType(); break;
					}
				}
				IConstructor cons;
				if(annos > 0){
					kwParamsOrAnnos = TrieMap_5Bits.transientOf();
					for(int i = 0; i < annos; i++){
						IValue val = stack.pop();
						IString ikey = (IString) stack.pop();
						kwParamsOrAnnos.__put(ikey.getValue(),  val);
					}
					cons =  vf.constructor(consType, stack.getChildren(arity)).asAnnotatable().setAnnotations(kwParamsOrAnnos);
				} else if(kwparams > 0){
					kwParamsOrAnnos = TrieMap_5Bits.transientOf();
					for(int i = 0; i < kwparams; i++){
						IValue val = stack.pop();
						IString ikey = (IString) stack.pop();
						kwParamsOrAnnos.__put(ikey.getValue(),  val);
					}
					cons = vf.constructor(consType, stack.getChildren(arity), kwParamsOrAnnos);
				} else {
					cons = vf.constructor(consType, stack.getChildren(arity));
				}

				stack.push(cons);
				valueWindow.read(cons);
				reader.next();
				break;
			}

			case SValue.DATETIME: {
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
					case SValue.DATETIME_YEAR: year = (int)reader.getLong(); break;
					case SValue.DATETIME_MONTH: month = (int)reader.getLong(); break;
					case SValue.DATETIME_DAY: day = (int)reader.getLong(); break;
					case SValue.DATETIME_HOUR: hour = (int)reader.getLong(); break;
					case SValue.DATETIME_MINUTE: minute = (int)reader.getLong(); break;
					case SValue.DATETIME_SECOND: second = (int)reader.getLong(); break;
					case SValue.DATETIME_MILLISECOND: millisecond = (int)reader.getLong(); break;
					case SValue.DATETIME_TZ_HOUR: timeZoneHourOffset = (int)reader.getLong(); break;
					case SValue.DATETIME_TZ_MINUTE: timeZoneMinuteOffset = (int)reader.getLong(); break;
					}
				}

				IDateTime dateTime = vf.datetime(year, month, day, hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);
				stack.push(dateTime);
				valueWindow.read(dateTime);
				reader.next();
				break;
			}

			case SValue.DATE: {
				int year = 0;
				int month = 0;
				int day = 0;

				while (not_at_end()) {
					switch(reader.field()){
					case SValue.DATE_YEAR: year = (int)reader.getLong(); break;
					case SValue.DATE_MONTH: month = (int)reader.getLong(); break;
					case SValue.DATE_DAY: day = (int)reader.getLong(); break;
					}
				}

				IDateTime dateTime = vf.datetime(year, month, day);
				stack.push(dateTime);
				valueWindow.read(dateTime);
				reader.next();
				break;
			}

			case SValue.TIME: {
				int hour = 0;
				int minute = 0;
				int second = 0;
				int millisecond = 0;

				int timeZoneHourOffset = 0;
				int timeZoneMinuteOffset = 0;

				while (not_at_end()) {
					switch(reader.field()){
					case SValue.TIME_HOUR: hour = (int)reader.getLong(); break;
					case SValue.TIME_MINUTE: minute = (int)reader.getLong(); break;
					case SValue.TIME_SECOND: second = (int)reader.getLong(); break;
					case SValue.TIME_MILLISECOND: millisecond = (int)reader.getLong(); break;
					case SValue.TIME_TZ_HOUR: timeZoneHourOffset = (int)reader.getLong(); break;
					case SValue.TIME_TZ_MINUTE: timeZoneMinuteOffset = (int)reader.getLong(); break;
					}
				}

				IDateTime dateTime = vf.time(hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);

				stack.push(dateTime);
				valueWindow.read(dateTime);
				reader.next();
				break;
			}

//		case FUNCTION:
//			break;

		case SValue.INT: {
			long n = 0;
			while (not_at_end()) {
				if(reader.field() == SValue.INT_VALUE){
					n = reader.getLong();
				}
			}
			IInteger ii = vf.integer(n);
			stack.push(ii);
			valueWindow.read(ii);
			reader.next();
			break;
		}
			
		case SValue.BIGINT: {
			byte[] bytes = null;
			while (not_at_end()) {
				if(reader.field() == SValue.BIGINT_VALUE){
					bytes = reader.getBytes();
				}
			}
			assert bytes != null;
			IInteger ii = vf.integer(bytes);
			stack.push(ii);
			valueWindow.read(ii);
			reader.next();
			break;
		}
		
		case SValue.LIST: {
			int len = 0;
			while (not_at_end()) {
				if(reader.field() == SValue.LIST_SIZE){
					len = (int) reader.getLong();
				}
			}
			IList lst = vf.list(stack.getChildren(len));
			stack.push(lst);
			valueWindow.read(lst);
			reader.next();
			break;
		}

		case SValue.LOC: {
			String scheme = null;
			String authority = "";
			String path = "";
			String query = null;
			String fragment = null;
			int offset = -1;
			int length = -1;
			int beginLine = -1;
			int endLine = -1;
			int beginColumn = -1;
			int endColumn = -1;
			while (not_at_end()) {
				switch(reader.field()){
				case SValue.LOC_SCHEME: scheme = reader.getString(); break;
				case SValue.LOC_AUTHORITY: authority = reader.getString(); break;
				case SValue.LOC_PATH: path = reader.getString(); break;
				case SValue.LOC_QUERY: query = reader.getString(); break;	
				case SValue.LOC_FRAGMENT: fragment = reader.getString(); break;	
				case SValue.LOC_OFFSET: offset = (int) reader.getLong(); break;
				case SValue.LOC_LENGTH: length = (int) reader.getLong(); break;
				case SValue.LOC_BEGINLINE: beginLine = (int) reader.getLong(); break;
				case SValue.LOC_ENDLINE: endLine = (int) reader.getLong(); break;
				case SValue.LOC_BEGINCOLUMN: beginColumn = (int) reader.getLong(); break;
				case SValue.LOC_ENDCOLUMN: endColumn = (int) reader.getLong(); break;
				}
			}

			ISourceLocation baseLoc = vf.sourceLocation(scheme, authority, path, query, fragment);
			
			ISourceLocation loc;
			
			if(beginLine >= 0){
				assert offset >= 0 && length >= 0 && endLine >= 0 && beginColumn >= 0 && endColumn >= 0;
				loc = vf.sourceLocation(baseLoc, offset, length, beginLine, endLine, beginColumn, endColumn);
			} else if (offset >= 0){
				assert length >= 0;
				loc = vf.sourceLocation(baseLoc, offset, length);
			} else {
				loc = baseLoc;
			}
			stack.push(loc);
			valueWindow.read(loc);;
			reader.next();
			break;
			
		}
		case SValue.MAP:	{
			int len = 0;
			while (not_at_end()) {
				if(reader.field() == SValue.MAP_SIZE){
					len = (int) reader.getLong();
				}
			}
			IMapWriter mw = vf.mapWriter();
			for(int i = 0; i < len; i++){
				IValue val = stack.pop();
				IValue key = stack.pop();
				mw.put(key, val);
			}
			
			IMap map = mw.done();
			stack.push(map);
			valueWindow.read(map);
			reader.next();
			break;
		}

		case SValue.NODE:	{
			String name = "";
			int arity = 0;
			int annos = 0;
			int kwparams = 0;
			TransientMap<String, IValue> kwParamsOrAnnos = null;

			while (not_at_end()) {
				switch(reader.field()){
				case SValue.NODE_NAME: name = reader.getString(); break;
				case SValue.NODE_ARITY: arity = (int)reader.getLong(); break;
				case SValue.NODE_KWPARAMS: kwparams = (int)reader.getLong(); break;
				case SValue.NODE_ANNOS: annos = (int)reader.getLong(); break;
				}
			}
			INode node;
			if(annos > 0){
				kwParamsOrAnnos = TrieMap_5Bits.transientOf();
				for(int i = 0; i < annos; i++){
					IValue val = stack.pop();
					IString ikey = (IString) stack.pop();
					kwParamsOrAnnos.__put(ikey.getValue(),  val);
				}
				node =  vf.node(name, stack.getChildren(arity)).asAnnotatable().setAnnotations(kwParamsOrAnnos);
			} else if(kwparams > 0){
				kwParamsOrAnnos = TrieMap_5Bits.transientOf();
				for(int i = 0; i < kwparams; i++){
					IValue val = stack.pop();
					IString ikey = (IString) stack.pop();
					kwParamsOrAnnos.__put(ikey.getValue(),  val);
				}
				node = vf.node(name, stack.getChildren(arity), kwParamsOrAnnos);
			} else {
				node = vf.node(name, stack.getChildren(arity));
			}
			
			stack.push(node);
			valueWindow.read(node);
			reader.next();
			break;
		}
		
		case SValue.RAT: {
			IInteger numerator = null;
			IInteger denominator = null;
			while (not_at_end()) {
				switch(reader.field()){
				case SValue.RAT_NUMERATOR:
					numerator = (IInteger) readValue(); break;
				case SValue.RAT_DENOMINATOR:
					denominator = (IInteger) readValue(); break;
				}
			}
			assert numerator != null & denominator != null;
			
			IRational rat = vf.rational(numerator, denominator);
			stack.push(rat);
			valueWindow.read(rat);
			reader.next();
			break;
		}

		case SValue.REAL: {
			byte[] bytes = null;
			int scale = 1;

			while (not_at_end()) {
				switch(reader.field()){
				case SValue.REAL_SCALE:
					scale = (int) reader.getLong(); break;
				case SValue.REAL_VALUE:
					bytes = reader.getBytes(); break;
				}
			}
			assert bytes != null;
			IReal real = vf.real(new BigDecimal(new BigInteger(bytes), scale).toString()); // TODO: Improve this
			stack.push(real);
			valueWindow.read(real);
			reader.next();
			break;
		}
		
		case SValue.SET: {
			int len = 0;
			while (not_at_end()) {
				if(reader.field() == SValue.SET_SIZE){
					len = (int) reader.getLong();
				}
			}
			ISet set = vf.set(stack.getChildren(len));
			stack.push(set);
			valueWindow.read(set);
			reader.next();
			break;
		}

		case SValue.STR: {
			String str = null;
			while (not_at_end()) {
				if(reader.field() == SValue.STR_VALUE){
					str = reader.getString();
				}
			}
			assert str != null;
			IString istr = vf.string(str);
			stack.push(istr);;
			// Already cached at wire level
			reader.next();
			break;
		}
		
		case SValue.TUPLE: {
			int len = 0;
			while (not_at_end()) {
				if(reader.field() == SValue.TUPLE_SIZE){
					len = (int) reader.getLong();
				}
			}
			ITuple tuple = vf.tuple(stack.getChildren(len));
			stack.push(tuple);
			valueWindow.read(tuple);
			reader.next();
			break;
		}
		
		case SValue.PREVIOUS: {
			int n = -1;
			while(not_at_end()){
				if(reader.field() == SValue.PREVIOUS_VALUE){
					n = (int) reader.getLong();
				}
			}
			
			IValue result = valueWindow.lookBack(n);
			if (result == null) {
				throw new IOException("Unexpected value cache miss");
			}
			stack.push(result);
			reader.next();
			break;
		}
			
		case SValue.END_OF_VALUE: {
			assert stack.size() == 1;
			skip_until_end();
			return stack.pop();
		}
		
		default:
			throw new IllegalArgumentException("readValue: " + reader.value());
			}

		}
	}
//	
//	public static void main(String[] args) throws IOException {
//
//		int N = 1;
//
//		OutputStream fileOut;
//
//		TypeStore typeStore = RascalValueFactory.getStore();
//		TypeFactory tf = TypeFactory.getInstance();
//		IValueFactory vf = ValueFactoryFactory.getValueFactory();
//
//		ISourceLocation fileLoc = null;
//		ISourceLocation locVal1 = null;
//		ISourceLocation locVal2 = null;
//		
//		IDateTime dt = vf.datetime(1234567);
//		System.out.println(dt);
//		
//		IReal r = vf.real(9.87654321);
//		
//		try {
//			fileLoc = vf.sourceLocation("home", "", "file.fst");
//			locVal1 = vf.sourceLocation(fileLoc, 100, 12);
//			locVal2 = vf.sourceLocation(fileLoc, 100, 12, 1, 2, 3, 4);
//		} catch (URISyntaxException e) {
//			System.err.println("Cannot create default location: " + e.getMessage());
//		}
//
//		fileOut = URIResolverRegistry.getInstance().getOutputStream(fileLoc, false);
//
//		ISetWriter w = vf.setWriter();
//		w.insert(vf.integer(10));
//		w.insert(vf.integer(42));
//		
//		Type adt = tf.abstractDataType(typeStore, "D");
//		Type fcons = tf.constructor(typeStore, adt, "f", tf.setType(tf.integerType()), "n");
//		
//		HashMap<String,IValue> kwParams = new HashMap<>();
//		kwParams.put("zzz", vf.string("pqr"));
//		
//		IValue start = dt;
//				//vf.integer("12345678901234567890");
//		//vf.tuple(vf.integer(42), vf.integer(42));
//				//vf.constructor(fcons, w.done()).asWithKeywordParameters().setParameters(kwParams);
//		Type startType = fcons;
//		
//		long startTime = Timing.getCpuTime();
//
//		RVMIValueWriter out = new RVMIValueWriter(fileOut);
//		
//		for(int i = 0; i < N; i++){
//			//out.writeType(startType);zx
//			out.writeValue(start);
//			out.writeValue(start);
//		}
//		out.close();
//
//		long endWrite = Timing.getCpuTime();
//
//		System.out.println("Writing " + N + " values " +  (endWrite - startTime)/1000000 + " msec");
//
//		NewRVMIValueReader in = null;
//		IValue outVal = start;
//		IValue outVal2 = null;
//		Type outType;
//		try {
//			ISourceLocation compIn = fileLoc;
//			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
//			in = new NewRVMIValueReader(fileIn, vf, typeStore, 10, 10);
//			for(int i = 0; i < N; i++){
//				
////				outType = in.readType();
////				System.out.println(outType);
//				
//				outVal = in.readValue();
//				outVal2 = in.readValue();
//				System.out.println(outVal + ", " + outVal.equals(outVal2));
//				
//			}
//			in.close();
//			in = null;
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new IOException(e.getMessage());
//		} 
//		finally {
//			if(in != null){
//				in.close();
//			}
//  		}
//		long endRead = Timing.getCpuTime();
//		//System.out.println(outVal);
//		System.out.println("Reading " + N + " value " +  (endRead - endWrite)/1000000 + " msec");
//	}

//	private void close() throws IOException {
//		basicIn.close();
//	}

	
}

class ReaderStack {
	private IValue[] elements;
	int capacity;
	private int sp = 0;

	ReaderStack(int capacity){
		this.capacity = (int)Math.max(capacity, 16);
		elements = new IValue[this.capacity];
	}
	
	public void push(IValue leaf){
		if(sp == capacity - 1){
			grow();
		}
		elements[sp] = leaf;
		sp++;
	}
	
	public IValue pop(){
		if(sp > 0){
			sp--;
			return elements[sp];
		}
		throw new RuntimeException("Empty Stack");
	}
	
	public int size(){
		return sp;
	}
	
	public IValue[] getChildren(int childs){
		int from = sp - childs;
		IValue[] children = new IValue[childs];
		if(from >= 0){
			for(int i = 0; i < childs; i++){
				children[i] = elements[from + i];
			}
			sp = from;
			return children;
		}
		throw new RuntimeException("Empty Stack");
	}
	
	private void grow() {
		int newSize = (int)Math.min(capacity * 2L, 0x7FFFFFF7); // max array size used by array list
		assert capacity <= newSize;
		capacity = newSize;
		IValue[] newElements = new IValue[newSize];
		System.arraycopy(elements, 0, newElements, 0, sp);
		elements = newElements;
	}
}
