package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISetWriter;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactParseError;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.ResizingArray;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

import io.usethesource.capsule.TransientMap;
import io.usethesource.capsule.TrieMap_5Bits;

/**
 * RVMIValueReader is a binary deserializer for IValues and Types. The main public functions are:
 * - readType
 * - readValue
 */

public class RVMIValueReader {
	private transient static IValueFactory vf;
	
	private final static int DEFAULT_SHARED_VALUES_STORE_SIZE = 1024;
	private final static int DEFAULT_SHARED_TYPES_STORE_SIZE = 128;
	private final static int DEFAULT_SHARED_URIS_STORE_SIZE = 128;
	private final static int DEFAULT_SHARED_NAMES_STORE_SIZE = 128;
	
	transient private static ResizingArray<IValue> sharedValuesList;
	transient private static int currentSharedValueId;
	transient private static ResizingArray<Type> sharedTypesList;
	transient private static int currentSharedTypeId;
	transient private static ResizingArray<ISourceLocation> sharedLocsList;
	transient private static int currentSharedLocId;

	transient static private ResizingArray<String> sharedNamesList;
	transient static private int currentSharedNamesId;

	private RVMInputStream in;

	private TypeFactory tf;

	private TypeStore store;

	private RascalTypeFactory rtf;

	public RVMIValueReader(InputStream in, IValueFactory vfactory, TypeStore ts){
		this( new RVMInputStream(in), vfactory, ts);
	}
	
	public RVMIValueReader(RVMInputStream in, IValueFactory vfactory, TypeStore ts) {
		tf = TypeFactory.getInstance();
		vf = vfactory;
		this.in = in;
		store = ts;
		rtf = RascalTypeFactory.getInstance();

		store.extendStore(RascalValueFactory.getStore());
		
		sharedValuesList = new ResizingArray<>(DEFAULT_SHARED_VALUES_STORE_SIZE);
		currentSharedValueId = 0;
		sharedTypesList = new ResizingArray<>(DEFAULT_SHARED_TYPES_STORE_SIZE);
		currentSharedTypeId = 0;
		sharedLocsList = new ResizingArray<>(DEFAULT_SHARED_URIS_STORE_SIZE);
		currentSharedLocId = 0;
		sharedNamesList = new ResizingArray<>(DEFAULT_SHARED_NAMES_STORE_SIZE);
		currentSharedNamesId = 0;
	}
	
	public RVMInputStream getIn() {
		return in;
	}
	
	int readArity() throws IOException{
		return in.readByte();
	}
	
	int readLength() throws IOException{
		return in.readInt();
	}
	
	String readName() throws IOException{
		int o = in.readByte();
		if(o == VALUE.SHARED_NAME.ordinal()){
			int n = in.readInt();
			String res = sharedNamesList.get(n);
			if(res == null){
				throw new RuntimeException("SharedName not found: " + n);
			}
			return res;
		}
		if(o != VALUE.NAME.ordinal()){
			throw new RuntimeException("readName NAME expected, found " + o);
		}
		String s = in.readString();
		//System.out.println("readName: " + currentSharedNamesId + ",\n" + s);
		sharedNamesList.set(s, currentSharedNamesId++);
		return s;
	}
	
	String[] readNames() throws IOException{
		int n = readLength();
		String[] names = new String[n];
		for(int i = 0; i < n; i++){
			names[i] = readName();
		}
		return names;
	}
	
	private IInteger readBigInt() throws IOException{
		int length = in.readInt();
		byte[] valueData = new byte[length];
		in.read(valueData, 0, length);
		
		return vf.integer(new BigInteger(valueData).toString()); // TODO: add new constructor to vf.integer?
	}
	
	private IReal readReal() throws IOException{
		int length = in.readInt();
		byte[] unscaledValueData = new byte[length];
		in.read(unscaledValueData, 0, length);
		int scale = in.readInt();
		
		return vf.real(new BigDecimal(new BigInteger(unscaledValueData), scale).toString()); // The toString call kind of stinks.
	}
	
	private TransientMap<String, IValue> readKeywordParamsOrAnnos() throws IOException{
		TransientMap<String, IValue> kwParamsOrAnnos = TrieMap_5Bits.transientOf();
		int arity = readArity();
		for(int i = 0; i < arity; i++){
			String key = readName();
			IValue val = readValue();
			kwParamsOrAnnos.__put(key,  val);
		}
		return kwParamsOrAnnos;
	}
	
	/**
	 * @return a type from the input stream. Types are shared when possible.
	 * @throws IOException
	 */
	Type readType() throws IOException{
		int op = in.readByte();
		
		TYPE start = TYPE.values()[op];
		
		if(start.equals(TYPE.SHARED_TYPE)){
			int n = in.readInt();
			Type res = sharedTypesList.get(n);
			if(res == null){
				throw new RuntimeException("sharedType not found: " + n);
			}
			return res;
		}
		
		Type t = readType1(op);
		sharedTypesList.set(t,  currentSharedTypeId++);
		return t;
	}
	
	@SuppressWarnings("deprecation")
	private Type readType1(int op) throws IOException{
		
		TYPE start = TYPE.values()[op];
		
		String [] fieldNames = null;
		String keyLabel = null;
		String valLabel = null;
		
		String name;
		Type typeParameters;
		int arity;
		Type elemType;
		
		switch(start){
		
		// Atomic types
		
		case BOOL:		
			return tf.boolType();
		case DATETIME:	
			return tf.dateTimeType();
		case INT:		
			return tf.integerType();
		case NODE:		
			return tf.nodeType();
		case NUMBER:	
			return tf.numberType();
		case RAT:		
			return tf.rationalType();
		case REAL:		
			return tf.realType();
		case LOC:		
			return tf.sourceLocationType();
		case STR:		
			return tf.stringType();
		case VALUE:		
			return tf.valueType();
		case VOID:		
			return tf.voidType();
		
		// Composite types
						
		case ADT:		
			name = readName();
			typeParameters = readType();
			//System.out.println("typeParameters: " + typeParameters);
			arity = typeParameters.getArity();
			if(arity > 0){
				Type targs[] = new Type[arity];
				for(int i = 0; i < arity; i++){
					targs[i] = typeParameters.getFieldType(i);
				}
				return tf.abstractDataType(store, name, targs);
			}
			return tf.abstractDataType(store, name);
		
		case ALIAS:		
			name = readName();
			Type aliasedType = readType();
			typeParameters = readType();
			return tf.aliasType(store, name, aliasedType, typeParameters);
		
		case CONSTRUCTOR_NAMED_FIELDS:
			fieldNames = readNames();
						// fall through to "constructor" case
			
		case CONSTRUCTOR: 	
			name = readName();
			arity = readArity();
			Type adtType = readType();

			Type declaredAdt = store.lookupAbstractDataType(name);

			if(declaredAdt != null){
				adtType = declaredAdt;
			}

			Type fieldTypes[] = new Type[arity];

			for(int i = 0; i < arity; i++){
				fieldTypes[i] = readType();
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
				typeAndNames[2 * i] =  fieldTypes[i];
				typeAndNames[2 * i + 1] = fieldNames[i];
			}

			Type res = store.lookupConstructor(adtType, name, tf.tupleType(typeAndNames));
			if(res == null){
				return tf.constructor(store, adtType, name, typeAndNames);
			} else {
				return res;
			}
						
		// External
		
		case FUNCTION:	
			Type returnType = readType();
			Type argumentTypes =  readType();
			Type keywordParameterTypes = readType();
			return rtf.functionType(returnType, argumentTypes, keywordParameterTypes);
		
		case REIFIED:	
			elemType = readType();
			elemType = elemType.getFieldType(0);
			res = rtf.reifiedType(elemType);
			return res;
		
		case OVERLOADED:
			int n = in.readInt();
			Set<FunctionType> alternatives = new HashSet<FunctionType>(n);
			for(int i = 0; i < n; i++){
				alternatives.add((FunctionType) readType());
			}
			return rtf.overloadedFunctionType(alternatives);

		case NONTERMINAL:
			IConstructor nt = (IConstructor) readValue();
			return rtf.nonTerminalType(nt);
		
		case LIST:		
			elemType = readType();
			return tf.listType(elemType);
		
		case MAP_NAMED_FIELDS:
			keyLabel  = readName();
			valLabel  = readName();
			// fall through to "map" case, both variables were already set to null.
			
		case MAP:		
			Type keyType = readType();
			Type valType = readType();
			if(keyLabel == null){
				return tf.mapType(keyType, valType);
			}
			return tf.mapType(keyType, keyLabel, valType, valLabel);
			
		case PARAMETER:	
			name = readName();
			Type bound = readType();
			return tf.parameterType(name, bound);
				
		case SET:		
			elemType = readType();
			return tf.setType(elemType);
						
		case TUPLE_NAMED_FIELDS:
			fieldNames = readNames();
			// fall through to "tuple" case, fieldNames was already set to null
						
		case TUPLE:		
			arity = readArity();
			Type[] elemTypes = new Type[arity];
			for(int i = 0; i < arity; i++){
				elemTypes[i] = readType();
			}

			if(fieldNames != null){
				return tf.tupleType(elemTypes, fieldNames);
			}
			return tf.tupleType(elemTypes);
						
		case SHARED_TYPE:
			n = in.readInt();
			res = sharedTypesList.get(n);
			if(res == null){
				throw new RuntimeException("readType: sharedType not found " + n);
			}
			return res;
		}
		throw new RuntimeException("readType: unhandled case " + start);
	}
	
	IValue[] readValues() throws IOException{
		int arity = readArity();
		IValue[] vals = new IValue[arity];
		for(int i = 0; i < arity; i++){
			vals[i] = readValue();
		}
		return vals;
	}
	
	/**
	 * @return a value read from the input stream.
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public IValue readValue() throws IOException{
		
		VALUE start = VALUE.values()[in.readByte()];
		
		switch(start){
		
		case BOOL:
			return vf.bool((boolean) in.readBool());
			
		case CONSTRUCTOR:
			Type consType = readType();
			IValue[] args = readValues();
			return vf.constructor(consType, args);
			
		case CONSTRUCTOR_ANNOTATIONS:
			TransientMap<String, IValue> kwParams = readKeywordParamsOrAnnos();
			consType = readType();
			args = readValues();
			return vf.constructor(consType, args).asAnnotatable().setAnnotations(kwParams);
			
		case CONSTRUCTOR_KEYWORDS:
			TransientMap<String, IValue> annos = readKeywordParamsOrAnnos();
			consType = readType();
			args = readValues();
			return vf.constructor(consType, args).asWithKeywordParameters().setParameters(annos);
			
		case DATE_TIME:
			int year = in.readInt();
			int month = in.readInt();
			int day = in.readInt();
			
			int hour = in.readInt();
			int minute = in.readInt();
			int second = in.readInt();
			int millisecond = in.readInt();
			
			int timeZoneHourOffset = in.readInt();
			int timeZoneMinuteOffset = in.readInt();
			return vf.datetime(year, month, day, hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);

		case DATE:
			year = in.readInt();
			month = in.readInt();
			day = in.readInt();
			return vf.date(year, month, day);
			
		case TIME:
			hour = in.readInt();
			minute = in.readInt();
			second = in.readInt();
			millisecond = in.readInt();
			timeZoneHourOffset = in.readInt();
			timeZoneMinuteOffset = in.readInt();
			return vf.time(hour, minute, second, millisecond, timeZoneHourOffset, timeZoneMinuteOffset);
			
		case FUNCTION:
			break;
			
		case BIG_INT:
			return readBigInt();
			
		case INT:
			return vf.integer(in.readInt());
			
		case INT_10:
			return vf.integer(-10);
		case INT_9:
			return vf.integer(-9);
		case INT_8:
			return vf.integer(-8);	
		case INT_7:
			return vf.integer(-7);
		case INT_6:
			return vf.integer(-6);
		case INT_5:
			return vf.integer(-5);
		case INT_4:
			return vf.integer(-4);
		case INT_3:
			return vf.integer(-3);
		case INT_2:
			return vf.integer(-2);
		case INT_1:
			return vf.integer(-1);
		case INT0:
			return vf.integer(0);
		case INT1:
			return vf.integer(1);
		case INT2:
			return vf.integer(2);
		case INT3:
			return vf.integer(3);
		case INT4:
			return vf.integer(4);
		case INT5:
			return vf.integer(5);
		case INT6:
			return vf.integer(6);
		case INT7:
			return vf.integer(7);
		case INT8:
			return vf.integer(8);
		case INT9:
			return vf.integer(9);
		case INT10:
			return vf.integer(10);
			
		case LIST:
			Type elmType = readType();
			int len = readLength();
			IListWriter wl = vf.listWriter(elmType);
			for(int i = 0; i < len; i++){
				wl.append(readValue());
			}
			return wl.done();
			
		case LOC:
			ISourceLocation path = (ISourceLocation) readValue();
			int offset = in.readInt();
			int length = -1;
			if(offset >= 0){
				length = in.readInt();
			} else {
				return path;
			}
			int beginLine = in.readShort();
			if(beginLine >= 0){
				int endLine = in.readShort();
				int beginColumn = in.readShort();
				int endColumn = in.readShort();
				return vf.sourceLocation(path, offset, length, beginLine, endLine, beginColumn, endColumn);
			}
			return vf.sourceLocation(path, offset, length);
			
		case MAP:
			Type type = readType();
			len = readLength();
			IMapWriter wm = vf.mapWriter(type);
			for(int i = 0; i < len; i++){
				wm.put(readValue(), readValue());
			}
			return wm.done();
		
		case NODE:
			String name = readName();
			args = readValues();
			return vf.node(name, args);
			
		case NODE_ANNOTATIONS:
			annos = readKeywordParamsOrAnnos();
			name = readName();
			args = readValues();
			return vf.node(name, args).asAnnotatable().setAnnotations(annos);
			
		case NODE_KEYWORDS:
			kwParams = readKeywordParamsOrAnnos();
			name = readName();
			args = readValues();
			return vf.node(name, args, kwParams);
			
		case RAT:
			IInteger numerator = (IInteger) readValue();
			IInteger denominator = (IInteger) readValue();
			return vf.rational(numerator, denominator);
			
		case REAL:
			return readReal();
			
		case SET:
			elmType = readType();
			len = readLength();
			ISetWriter ws = vf.setWriter(elmType);
			for(int i = 0; i < len; i++){
				ws.insert(readValue());
			}
			return ws.done();
				
		case STR:
			IString is = vf.string(in.readString());
			sharedValuesList.set(is,  currentSharedValueId++);
			return is;
			
		case SHARED_STR:
			return (IString) sharedValuesList.get(in.readInt());
			
		case URI:
			try {
				path = vf.sourceLocation(new URI(in.readString()));
			} catch(URISyntaxException e){
				throw new FactParseError("Illegal URI", e); // Can't happen.
			}
			sharedLocsList.set(path,  currentSharedLocId++);
			return path;
			
		case SHARED_URI:
			int n = in.readInt();
			path = sharedLocsList.get(n);
			if(path == null){
				throw new RuntimeException("SharedLoc not found: " + n);
			}
			return path;
			
		case TUPLE:
			type = readType();
			IValue[] elems = readValues();
			return vf.tuple(type, elems);
			
		default:
			break;
		
		}
		throw new IllegalArgumentException("readValue, start = " + start);
	}
	
	public static void main(String[] args) throws IOException {

		int N = 1;

		OutputStream fileOut;

		TypeStore typeStore = RascalValueFactory.getStore();
		TypeFactory tf = TypeFactory.getInstance();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();

		ISourceLocation fileLoc = null;
		ISourceLocation locVal1 = null;
		ISourceLocation locVal2 = null;
		
		IDateTime dt = vf.datetime(1234567);
		System.out.println(dt);
		
		IReal r = vf.real(9.87654321);
		
		try {
			fileLoc = vf.sourceLocation("home", "", "file.fst");
			locVal1 = vf.sourceLocation(fileLoc, 100, 12);
			locVal2 = vf.sourceLocation(fileLoc, 100, 12, 1, 2, 3, 4);
		} catch (URISyntaxException e) {
			System.err.println("Cannot create default location: " + e.getMessage());
		}

		fileOut = URIResolverRegistry.getInstance().getOutputStream(fileLoc, false);

		ISetWriter w = vf.setWriter();
		w.insert(vf.integer(10));
		w.insert(vf.integer(42));
		
		Type adt = tf.abstractDataType(typeStore, "D");
		Type fcons = tf.constructor(typeStore, adt, "f", tf.setType(tf.integerType()), "n");
		
		HashMap<String,IValue> kwParams = new HashMap<>();
		kwParams.put("zzz", vf.string("pqr"));
		
		IValue start = vf.integer("12345678901234567890");
		//vf.tuple(vf.integer(42), vf.integer(42));
				//vf.constructor(fcons, w.done()).asWithKeywordParameters().setParameters(kwParams);
		Type startType = fcons;
		
		long startTime = Timing.getCpuTime();

		RVMIValueWriter out = new RVMIValueWriter(fileOut);
		
		for(int i = 0; i < N; i++){
			//out.writeType(startType);zx
			out.writeValue(start);
			out.writeValue(start);
		}
		out.close();

		long endWrite = Timing.getCpuTime();

		System.out.println("Writing " + N + " values " +  (endWrite - startTime)/1000000 + " msec");

		RVMIValueReader in = null;
		IValue outVal = start;
		IValue outVal2 = null;
		Type outType;
		try {
			ISourceLocation compIn = fileLoc;
			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
			in = new RVMIValueReader(fileIn, vf, typeStore);
			for(int i = 0; i < N; i++){
				
//				outType = in.readType();
//				System.out.println(outType);
				
				outVal = in.readValue();
				outVal2 = in.readValue();
				System.out.println(outVal + ", " + outVal.equals(outVal2));
				
			}
			in.close();
			in = null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} 
		finally {
			if(in != null){
				in.close();
			}
  		}
		long endRead = Timing.getCpuTime();
		//System.out.println(outVal);
		System.out.println("Reading " + N + " value " +  (endRead - endWrite)/1000000 + " msec");
	}

	private void close() throws IOException {
		in.close();
	}

	
}
