package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.nustaq.serialization.FSTBasicObjectSerializer;
import org.nustaq.serialization.FSTClazzInfo;
import org.nustaq.serialization.FSTClazzInfo.FSTFieldInfo;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListWriter;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
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
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.value.util.IndexedSet;
import org.rascalmpl.value.util.ResizingArray;
import org.rascalmpl.value.visitors.IValueVisitor;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;

import io.usethesource.capsule.TransientMap;
import io.usethesource.capsule.TrieMap_5Bits;

/**
 * FSTSerializableIValue acts as a serializer and wrapper for IValues
 * - On writing a FSTSerializableIValue oject is written
 * - On reading, the wrapped IValue is returned.
 */

enum VALUE {REAL, INT, 
	        INT_10, INT_9,INT_8, INT_7, INT_6, INT_5, INT_4,INT_3, INT_2, INT_1,
	        INT0, INT1, INT2, INT3, INT4, INT5, INT6, INT7, INT8, INT9, INT10,
	        RAT, LIST, LIST_END, MAP,  MAP_END, SET, SET_END, LOC, STR, NAME,
	        NODE, NODE_ANNOTATIONS, NODE_KEYWORDS, NODE_END,
	        CONSTRUCTOR, CONSTRUCTOR_ANNOTATIONS, CONSTRUCTOR_KEYWORDS, CONSTRUCTOR_END,
	        TUPLE, TUPLE_END, BOOL, FUNCTION, DATE, TIME, DATE_TIME, 
	        SERIALIZED, END_SERIALIZED, SHARED_NAME, SHARED_STR, SHARED_TYPE, SHARED_URI};

public class FSTSerializableIValue2 extends FSTBasicObjectSerializer implements Serializable   {
	
	private static final long serialVersionUID = 6704614265562953320L;
	
	private transient static IValueFactory vf;
	
	private final static int DEFAULT_SHARED_VALUES_STORE_SIZE = 1024;
	private final static int DEFAULT_SHARED_TYPES_STORE_SIZE = 128;
	private final static int DEFAULT_SHARED_URIS_STORE_SIZE = 128;
	private final static int DEFAULT_SHARED_NAMES_STORE_SIZE = 128;
	
	private final static int MAX_BYTE = 127;

	transient private static IndexedSet<IValue> sharedValues;
	transient private static IndexedSet<Type> sharedTypes;
	transient private static IndexedSet<URI> sharedURIs;
	transient private static IndexedSet<String> sharedNames;
	
	transient private static ResizingArray<IValue> sharedValuesList;
	transient private static int currentSharedValueId;
	transient private static ResizingArray<Type> sharedTypesList;
	transient private static int currentSharedTypeId;
	transient private static ResizingArray<URI> sharedURIsList;
	transient private static int currentSharedURIId;

	transient static private ResizingArray<String> sharedNamesList;
	transient static private int currentSharedNamesId;

	public static void initSerialization(IValueFactory vfactory, TypeStore ts){
		FSTSerializableType.initSerialization(vfactory, ts);
		vf = vfactory;
//		store = ts;
//		store.extendStore(RascalValueFactory.getStore());
		
		sharedValues = new IndexedSet<>();
		sharedTypes = new IndexedSet<>();
		sharedURIs = new IndexedSet<>();
		sharedNames = new IndexedSet<>();
		
		sharedValuesList = new ResizingArray<>(DEFAULT_SHARED_VALUES_STORE_SIZE);
		currentSharedValueId = 0;
		sharedTypesList = new ResizingArray<>(DEFAULT_SHARED_TYPES_STORE_SIZE);
		currentSharedTypeId = 0;
		sharedURIsList = new ResizingArray<>(DEFAULT_SHARED_URIS_STORE_SIZE);
		currentSharedURIId = 0;
		sharedNamesList = new ResizingArray<>(DEFAULT_SHARED_NAMES_STORE_SIZE);
		currentSharedNamesId = 0;
	}
	
	private transient IValue value;
	
	/**
	 *  Constructor used for registration of this serializer
	 */
	public FSTSerializableIValue2() {
	}


	/**
	 * Constructor used to wrap an IValue to be serialized
	 */
	public FSTSerializableIValue2(IValue value) {
		this.value = value;
	}
	
	IValue getValue(){
		return value;
	}
	
	private void writeType(final FSTObjectOutput out, Type t) throws IOException{
		int typeId = sharedTypes.store(t);
		if(typeId >= 0){
			out.writeObject(VALUE.SHARED_TYPE);
			out.writeShort(typeId);
		} else {
			out.writeObject(new FSTSerializableType(t));
		}
	}
	
	private Type readType(final FSTObjectInput in) throws ClassNotFoundException, IOException{
		
		Object o = in.readObject();
		if(o.equals(VALUE.SHARED_TYPE)){
			return sharedTypesList.get(in.readShort());
		}
		if(o instanceof Type){
			Type t = (Type) o;
			sharedTypesList.set(t,  currentSharedTypeId++);
			return t;
		}
		Type t = ((FSTSerializableType) o).getType();
		sharedTypesList.set(t,  currentSharedTypeId++);
		return t;
	}
	
	private void writeName(final FSTObjectOutput out, String s) throws IOException{
		int stringId = sharedNames.store(s);
		if(stringId >= 0){
		   out.writeObject(VALUE.SHARED_NAME);
		   out.writeShort(stringId);
		} else {
			out.writeObject(VALUE.NAME);
			out.writeObject(s);
		}
	}
	
	private String readName(final FSTObjectInput in) throws ClassNotFoundException, IOException{
		Object o = in.readObject();
		if(o.equals(VALUE.SHARED_NAME)){
			return sharedNamesList.get(in.readShort());
		}
		String s = (String) in.readObject();
		sharedNamesList.set(s, currentSharedNamesId++);
		return s;
	}
	
	private void writeString(final FSTObjectOutput out, IString s) throws IOException{
		int stringId = sharedValues.store(s);
		if(stringId >= 0){
			out.writeObject(VALUE.SHARED_STR);
			out.writeShort(stringId);
		} else {
			out.writeObject(VALUE.STR);
			out.writeObject(s.getValue());
		}
	}
	
	private IString readString(final FSTObjectInput in) throws ClassNotFoundException, IOException{
		Object o = in.readObject();
		if(o.equals(VALUE.SHARED_STR)){
			return (IString) sharedValuesList.get(in.readShort());
		}
		IString s = vf.string((String) o);
		sharedValuesList.set(s,  currentSharedValueId++);
		return s;
	}
	
	public void writeFInt(final FSTObjectOutput out, int anInt) throws IOException {
		// -128 = short byte, -127 == 4 byte
		if (anInt > -127 && anInt <= 127) {
			out.writeByte(anInt);
		} else if (anInt >= Short.MIN_VALUE && anInt <= Short.MAX_VALUE) {
			out.writeByte((byte) -128);
			out.writeByte((byte) (anInt >>> 0));
			out.writeByte((byte) (anInt >>> 8));
		} else {
			out.writeByte((byte) -127);
			out.writeByte((byte) ((anInt >>> 0) & 0xFF));
			out.writeByte((byte) ((anInt >>>  8) & 0xFF));
			out.writeByte((byte) ((anInt >>> 16) & 0xFF));
		}
	}
	 
	   public int readFInt(final FSTObjectInput in) throws IOException {
		   final byte head = in.readByte();
		   // -128 = short byte, -127 == 4 byte
		   if (head > -127 && head <= 127) {
			   return head;
		   }
		   if (head == -128) {
			   int ch1 = (in.readByte() + 256) & 0xff;
			   int ch2 = (in.readByte() + 256) & 0xff;

			   return (short) ((ch2 << 8) + (ch1 << 0));
		   } else {
			   int ch1 = (in.readByte() + 256) & 0xff;
			   int ch2 = (in.readByte() + 256) & 0xff;
			   int ch3 = (in.readByte() + 256) & 0xff;
			   int ch4 = (in.readByte() + 256) & 0xff;
			   int res = (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
			   return res;
		   }
	   }
	
	private void writeURI(final FSTObjectOutput out, URI uri) throws IOException{
		int uriId = sharedURIs.store(uri);
		if(uriId >= 0){
			out.writeObject(VALUE.SHARED_URI);
			out.writeShort(uriId);
		} else {
			out.writeObject(uri);
		}
	}
	
	private URI readURI(final FSTObjectInput in) throws ClassNotFoundException, IOException{
		Object o = in.readObject();
		if(o.equals(VALUE.SHARED_URI)){
			return sharedURIsList.get(in.readShort());
		}
		URI u = (URI) o;
		sharedURIsList.set(u,  currentSharedURIId++);
		return u;
	}
	
	private void writeArity(final FSTObjectOutput out, int arity) throws IOException{
		if(arity >= MAX_BYTE){
			throw new IllegalArgumentException("Arity " + arity + " not supported");
		}
		out.writeByte(arity);
	}
	
	private int readArity(final FSTObjectInput in) throws IOException{
		return in.readByte();
	}
	
	private void writeLength(final FSTObjectOutput out, int length) throws IOException{
		writeFInt(out, length);
	}
	
	private int readLength(final FSTObjectInput in) throws IOException{
		return readFInt(in);
	}
	
//	private void writeValue(final FSTObjectOutput out, IValue v) throws IOException{
//		out.writeObject(new FSTSerializableIValue2(v));
//	}
	
	private void writeKeywordParams(final FSTObjectOutput out, Map<String, IValue> kwParams) throws IOException{
		writeArity(out, kwParams.size());
		for (Map.Entry<String, IValue> param : kwParams.entrySet()) {
			writeName(out, param.getKey());
			writeValue(out, param.getValue());
		}
	}
	
	private void writeAnnos(final FSTObjectOutput out, Map<String, IValue> annos) throws IOException{
		writeArity(out, annos.size());
		for (Map.Entry<String, IValue> param : annos.entrySet()) {
			writeName(out, param.getKey());
			writeValue(out, param.getValue());
		}
	}
	
	@Override
	public void writeObject(FSTObjectOutput out, Object toWrite, FSTClazzInfo clzInfo, FSTFieldInfo arg3, int arg4)
					throws IOException {
		
		IValue v = ((FSTSerializableIValue2) toWrite).getValue();
		out.writeObject(VALUE.SERIALIZED);
		writeValue(out, v);
		out.writeObject(VALUE.END_SERIALIZED);
		//out.writeObject(new FSTSerializableIValue2(v));
	}
	
	private void writeValue(FSTObjectOutput out, IValue v) throws IOException{
		
		v.accept(new IValueVisitor<Void,IOException>() {

			@Override
			public Void visitBoolean(IBool val) throws IOException {
				out.writeObject(VALUE.BOOL);
				out.writeObject(val.getValue());
				return null;
			}

			@Override
			public Void visitConstructor(IConstructor cons) throws IOException {
				if(cons.mayHaveKeywordParameters()){
					if(cons.asWithKeywordParameters().hasParameters()){
						out.writeObject(VALUE.CONSTRUCTOR_KEYWORDS);
						writeKeywordParams(out, cons.asWithKeywordParameters().getParameters());
					} else {
						out.writeObject(VALUE.CONSTRUCTOR);
					}
				} else {
					if(cons.asAnnotatable().hasAnnotations()){
						out.writeObject(VALUE.CONSTRUCTOR_ANNOTATIONS);
						writeAnnos(out, cons.asAnnotatable().getAnnotations());
					} else {
						out.writeObject(VALUE.CONSTRUCTOR);
					}
				}
				Type consType = cons.getUninstantiatedConstructorType();
				writeType(out, consType);
				int arity = consType.getArity();
				for(int i = 0; i < arity; i++){
					writeValue(out, cons.get(i));
				}
				out.writeObject(VALUE.CONSTRUCTOR_END);
				//if(consType.getName().equals("type"))
				System.out.println("w cons: " + consType + ": " + cons);
				return null;
			}

			@Override
			public Void visitDateTime(IDateTime dateTime) throws IOException {
				if(dateTime.isDateTime()){
					out.writeObject(VALUE.DATE_TIME);
					
					out.writeInt(dateTime.getYear());
					out.writeInt(dateTime.getMonthOfYear());
					out.writeInt(dateTime.getDayOfMonth());
					
					out.writeInt(dateTime.getHourOfDay());
					out.writeInt(dateTime.getMinuteOfHour());
					out.writeInt(dateTime.getSecondOfMinute());
					out.writeInt(dateTime.getMillisecondsOfSecond());
					
					out.writeInt(dateTime.getTimezoneOffsetHours());
					out.writeInt(dateTime.getTimezoneOffsetMinutes());
				} else if(dateTime.isDate()){
					out.writeObject(VALUE.DATE);
					
					out.writeInt(dateTime.getYear());
					out.writeInt(dateTime.getMonthOfYear());
					out.writeInt(dateTime.getDayOfMonth());
				} else {
					out.writeObject(VALUE.TIME);
					
					out.writeInt(dateTime.getHourOfDay());
					out.writeInt(dateTime.getMinuteOfHour());
					out.writeInt(dateTime.getSecondOfMinute());
					out.writeInt(dateTime.getMillisecondsOfSecond());
					
					out.writeInt(dateTime.getTimezoneOffsetHours());
					out.writeInt(dateTime.getTimezoneOffsetMinutes());
				}
				return null;
			}

			@Override
			public Void visitExternal(IExternalValue val) throws IOException {
					throw new RuntimeException("External type not supported: " + val);
			}

			@Override
			public Void visitInteger(IInteger val) throws IOException {
				int n = val.intValue();
				switch(n){
				case -10:	out.writeObject(VALUE.INT_10);break;
				case -9:	out.writeObject(VALUE.INT_9);break;
				case -8:	out.writeObject(VALUE.INT_8);break;
				case -7:	out.writeObject(VALUE.INT_7);break;
				case -6:	out.writeObject(VALUE.INT_6);break;
				case -5:	out.writeObject(VALUE.INT_5);break;
				case -4:	out.writeObject(VALUE.INT_4);break;
				case -3:	out.writeObject(VALUE.INT_3);break;
				case -2:	out.writeObject(VALUE.INT_2);break;
				case -1:	out.writeObject(VALUE.INT_1);break;
				case 0:		out.writeObject(VALUE.INT0);break;
				case 1:		out.writeObject(VALUE.INT1);break;
				case 2:		out.writeObject(VALUE.INT2);break;
				case 3:		out.writeObject(VALUE.INT3);break;
				case 4:		out.writeObject(VALUE.INT4);break;
				case 5:		out.writeObject(VALUE.INT5);break;
				case 6:		out.writeObject(VALUE.INT6);break;
				case 7:		out.writeObject(VALUE.INT7);break;
				case 8:		out.writeObject(VALUE.INT8);break;
				case 9:		out.writeObject(VALUE.INT9);break;
				case 10:	out.writeObject(VALUE.INT10);break;
				default:
				out.writeObject(VALUE.INT);
				out.writeInt(val.intValue());
				}
				return null;
			}

			@Override
			public Void visitList(IList val) throws IOException {
				out.writeObject(VALUE.LIST);
				writeType(out, val.getElementType());
				writeLength(out, val.length());
				for(IValue v : val){
					writeValue(out, v);
				}
				out.writeObject(VALUE.LIST_END);
				return null;
			}

			@Override
			public Void visitListRelation(IList val) throws IOException {
				visitList(val);
				return null;
			}

			@Override
			public Void visitMap(IMap val) throws IOException {
				out.writeObject(VALUE.MAP);
				writeType(out, val.getType());
				writeLength(out, val.size());
				for(IValue key : val){
					writeValue(out, key);
					writeValue(out, val.get(key));
				}
				out.writeObject(VALUE.MAP_END);
				return null;
			}

			@Override
			public Void visitNode(INode val) throws IOException {
				if(val.mayHaveKeywordParameters()){
					if(val.asWithKeywordParameters().hasParameters()){
						out.writeObject(VALUE.NODE_KEYWORDS);
						writeKeywordParams(out, val.asWithKeywordParameters().getParameters());
					} else {
						out.writeObject(VALUE.NODE);
					}
				} else {
					if(val.asAnnotatable().hasAnnotations()){
						out.writeObject(VALUE.NODE_ANNOTATIONS);
						writeAnnos(out, val.asAnnotatable().getAnnotations());
					} else {
						out.writeObject(VALUE.NODE);
					}
				}
				writeName(out, val.getName());
				int arity = val.arity();
				writeArity(out, arity);
				for(int i = 0; i < arity; i++){
					writeValue(out, val.get(i));
				}
				out.writeObject(VALUE.NODE_END);
				return null;
			}

			@Override
			public Void visitRational(IRational val) throws IOException {
				out.writeObject(VALUE.RAT);
				out.writeInt(val.numerator().intValue());
				out.writeInt(val.denominator().intValue());
				return null;
			}

			@Override
			public Void visitReal(IReal val) throws IOException {
				out.writeObject(VALUE.REAL);
				out.writeObject(val.doubleValue());
				return null;
			}

			@Override
			public Void visitRelation(ISet val) throws IOException {
				visitSet(val);
				return null;
			}

			@Override
			public Void visitSet(ISet val) throws IOException {
				out.writeObject(VALUE.SET);
				writeType(out, val.getElementType());
				writeLength(out, val.size());
				for(IValue v : val){
					writeValue(out, v);
				}
				out.writeObject(VALUE.SET_END);
				return null;
			}

			@Override
			public Void visitSourceLocation(ISourceLocation val) throws IOException {
				out.writeObject(VALUE.LOC);
				writeURI(out, val.getURI());
				
				//uri, offset, length, beginLine, endLine, beginCol, endCol
				if(val.hasOffsetLength()){
					out.writeInt(val.getOffset());
					out.writeInt(val.getLength());
				} else {
					out.writeInt(-1);
					return null;
				}
				if(val.hasLineColumn()){
					out.writeShort(val.getBeginLine());
					out.writeShort(val.getEndLine());
					out.writeShort(val.getBeginColumn());
					out.writeShort(val.getEndColumn());
				}
				return null;
			}

			@Override
			public Void visitString(IString val) throws IOException {
				writeString(out, val);
				return null;
			}

			@Override
			public Void visitTuple(ITuple val) throws IOException {
				out.writeObject(VALUE.TUPLE);
				writeType(out, val.getType());
				writeArity(out, val.arity());
				for(IValue v : val){
					writeValue(out, v);
				}
				out.writeObject(VALUE.TUPLE_END);
				return null;
			} });
	}

	@Override
	public void readObject(FSTObjectInput in, Object toRead, FSTClazzInfo clzInfo, FSTClazzInfo.FSTFieldInfo referencedBy)
	{
			System.out.println("FSTSerializableIValue.readObject");
	}

	@Override
	public Object instantiate(@SuppressWarnings("rawtypes") Class objectClass, FSTObjectInput in, FSTClazzInfo serializationInfo, FSTClazzInfo.FSTFieldInfo referencee, int streamPosition) throws ClassNotFoundException, IOException 
	{	
		Object o = in.readObject();
		if(!o.equals(VALUE.SERIALIZED)){
			throw new IllegalArgumentException("Serialized value does not start with SERIALIZED");
		}
		IValue v = readValue(in);
		o = in.readObject();
		if(!o.equals(VALUE.END_SERIALIZED)){
			throw new IllegalArgumentException("Serialized value does not end with END_SERIALIZED");
		}
		return v;
		
	}
	
	private IValue readValue(final FSTObjectInput in) throws ClassNotFoundException, IOException{
		Object o = in.readObject();
		if(o instanceof IValue){
			return (IValue) o;
		}
		
		VALUE start = (VALUE) o;
		
		switch(start){
		
		case BOOL:
			return vf.bool((boolean) in.readObject());
			
		case CONSTRUCTOR:
		case CONSTRUCTOR_ANNOTATIONS:
		case CONSTRUCTOR_KEYWORDS:
			TransientMap<String, IValue> kwParams  = null;
			TransientMap<String, IValue> annos = null;
			if(start == VALUE.CONSTRUCTOR_KEYWORDS){
				int arity = readArity(in);
				kwParams = TrieMap_5Bits.transientOf();
				for(int i = 0; i < arity; i++){
					String key = readName(in);
					IValue val = readValue(in);
					kwParams.__put(key,  val);
				}
			} else if(start == VALUE.CONSTRUCTOR_ANNOTATIONS){
				int arity = readArity(in);
				annos = TrieMap_5Bits.transientOf();
				for(int i = 0; i < arity; i++){
					String key = readName(in);
					IValue val = readValue(in);
					annos.__put(key,  val);
				}
			}
			Type consType = readType(in);
			int arity = consType.getArity();
			IValue[] args = new IValue[arity];
			for(int i = 0; i < arity; i++){
				args[i] = readValue(in);
			}
			
			IValue res = null;
			if(start == VALUE.CONSTRUCTOR_KEYWORDS){
				res = vf.constructor(consType, args).asWithKeywordParameters().setParameters(kwParams);
			} else {
				if(start == VALUE.CONSTRUCTOR_ANNOTATIONS){
					res = vf.constructor(consType, args).asAnnotatable().setAnnotations(annos);
				}
				res = vf.constructor(consType, args);
			}
			System.out.println("r cons: "+ consType + ": " + res);
			o = in.readObject();
			if(!o.equals(VALUE.CONSTRUCTOR_END)){
				System.out.println("consType: " + consType);
				throw new IllegalArgumentException("found = " + o + ", instead of CONSTRUCTOR_END");
			}
			return res;
			
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
			Type elmType = readType(in);
			int len = readLength(in);
			IListWriter wl = vf.listWriter(elmType);
			for(int i = 0; i < len; i++){
				wl.append(readValue(in));
			}
			o = in.readObject();
			if(!o.equals(VALUE.LIST_END)){
				throw new IllegalArgumentException("found = " + o + ", instead of LIST_END");
			}
			return wl.done();
			
		case LOC:
			URI uri = readURI(in);
			int offset = in.readInt();
			int length = -1;
			if(offset >= 0){
				length = in.readInt();
			} else {
				return vf.sourceLocation(uri);
			}
			int beginLine = in.readShort();
			if(beginLine >= 0){
				int endLine = in.readShort();
				int beginColumn = in.readShort();
				int endColumn = in.readShort();
				return vf.sourceLocation(uri, offset, length, beginLine, endLine, beginColumn, endColumn);
			}
			return vf.sourceLocation(uri, offset, length);
			
		case MAP:
			Type type = readType(in);
			len = readLength(in);
			IMapWriter wm = vf.mapWriter(type);
			for(int i = 0; i < len; i++){
				wm.put(readValue(in), readValue(in));
			}
			//System.out.println("map: " + wm.done());
			o = in.readObject();
			if(!o.equals(VALUE.MAP_END)){
				throw new IllegalArgumentException("found = " + o + ", instead of MAP_END");
			}
			return wm.done();
		
		case NODE:
		case NODE_ANNOTATIONS:
		case NODE_KEYWORDS:
			kwParams  = null;
			annos = null;
			if(start == VALUE.NODE_KEYWORDS){
				kwParams = TrieMap_5Bits.transientOf();
				arity = readArity(in);
				for(int i = 0; i < arity; i++){
					String key = readName(in);
					IValue val = readValue(in);
					kwParams.__put(key,  val);
				}
			} else if(start == VALUE.NODE_ANNOTATIONS){
				annos = TrieMap_5Bits.transientOf();
				arity = readArity(in);
				for(int i = 0; i < arity; i++){
					String key = readName(in);
					IValue val = readValue(in);
					annos.__put(key,  val);
				}
			}

			String name = readName(in);
			arity = readArity(in);;
			args = new IValue[arity];
			for(int i = 0; i < arity; i++){
				args[i] = readValue(in);
			}
			o = in.readObject();
			if(!o.equals(VALUE.NODE_END)){
				throw new IllegalArgumentException("found = " + o + ", instead of NODE_END");
			}
			if(start == VALUE.NODE_KEYWORDS){
				return vf.node(name, args, kwParams);
			} else {
				if(start == VALUE.NODE_ANNOTATIONS){
					return vf.node(name, args).asAnnotatable().setAnnotations(annos);
				} else {
					return vf.node(name, args);
				}
			}
			
		case RAT:
			int numerator = in.readInt();
			int denominator = in.readInt();
			return vf.rational(numerator, denominator);
			
		case REAL:
			double d = (double) in.readObject();
			return vf.real(d);
			
		case SET:
			elmType = readType(in);
			len = readLength(in);
			ISetWriter ws = vf.setWriter(elmType);
			for(int i = 0; i < len; i++){
				ws.insert(readValue(in));
			}
			o = in.readObject();
			if(!o.equals(VALUE.SET_END)){
				throw new IllegalArgumentException("found = " + o + ", instead of SET_END");
			}
			return ws.done();
				
		case STR:
			return readString(in);
			
		case TUPLE:
			type = readType(in);
			arity = readArity(in);
			IValue[] elems = new IValue[arity];
			for(int i = 0; i < arity; i++){
				elems[i] = readValue(in);
			}
			o = in.readObject();
			if(!o.equals(VALUE.TUPLE_END)){
				throw new IllegalArgumentException("found = " + o + ", instead of TUPLE_END");
			}
			return vf.tuple(type, elems);
			
		case SHARED_STR:
			return (IString) sharedValuesList.get(in.readShort());
			
		default:
			break;
		
		}
		throw new IllegalArgumentException("readValue, start = " + start);
	}
	
	static private final FSTSerializableIValue2 serializableValue;
	
	static private final FSTSerializableType serializableType;
	
	static {
		// set up FST serialization in gredients that will be reused across read/write calls

		// PDB Types
		serializableType = new FSTSerializableType();
		serializableValue = new FSTSerializableIValue2();
	} 
	
	/**
	 * Create an FSTConfiguration depending on the used extension: ".json" triggers the JSON reader/writer.
	 * Note: the JSON version is somewhat larger and slower but is usefull for recovery during bootstrapping incidents.
	 * @param source or desination of executable
	 * @return an initialized FSTConfiguration
	 */
	private static FSTConfiguration makeFSTConfig(ISourceLocation path){
		FSTConfiguration config = path.getURI().getPath().contains(".json") ?
				FSTConfiguration.createJsonConfiguration() : FSTConfiguration.createDefaultConfiguration(); 
		config.registerSerializer(FSTSerializableType.class, serializableType, false);
		config.registerSerializer(FSTSerializableIValue2.class, serializableValue, false);
		return config;
	}
	
	public static void main(String[] args) throws IOException {

		int N = 1;

		OutputStream fileOut;

		TypeStore typeStore = RascalValueFactory.getStore();
		TypeFactory tf = TypeFactory.getInstance();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();

		FSTSerializableIValue2.initSerialization(vf, typeStore);

		ISourceLocation fileLoc = null;
		ISourceLocation locVal1 = null;
		ISourceLocation locVal2 = null;
		
		IDateTime dt = vf.datetime(1234567);
		System.out.println(dt);
		
		
		try {
			fileLoc = vf.sourceLocation("home", "", "file.fst");
			locVal1 = vf.sourceLocation(fileLoc, 100, 12);
			locVal2 = vf.sourceLocation(fileLoc, 100, 12, 1, 2, 3, 4);
		} catch (URISyntaxException e) {
			System.err.println("Cannot create default location: " + e.getMessage());
		}

		fileOut = URIResolverRegistry.getInstance().getOutputStream(fileLoc, false);
		FSTObjectOutput out = new FSTObjectOutput(fileOut, makeFSTConfig(fileLoc));

		ISetWriter w = vf.setWriter();
		w.insert(vf.integer(10));
		w.insert(vf.integer(42));
		
		Type adt = tf.abstractDataType(typeStore, "D");
		Type fcons = tf.constructor(typeStore, adt, "f", tf.setType(tf.integerType()), "n");
		
		HashMap<String,IValue> kwParams = new HashMap<>();
		kwParams.put("zzz", vf.string("pqr"));
		
		IValue start =
				
				vf.constructor(fcons, w.done()).asWithKeywordParameters().setParameters(kwParams);
		
		
		long startTime = Timing.getCpuTime();

		FSTSerializableIValue2 startFST = new FSTSerializableIValue2(start);

		
		
		for(int i = 0; i < N; i++){
			out.writeObject(startFST);
		}
		out.close();

		long endWrite = Timing.getCpuTime();

		System.out.println("Writing " + N + " values " +  (endWrite - startTime)/1000000 + " msec");


		FSTSerializableIValue2.initSerialization(vf, typeStore);

		FSTObjectInput in = null;
		IValue outVal = start;
		try {
			ISourceLocation compIn = fileLoc;
			InputStream fileIn = URIResolverRegistry.getInstance().getInputStream(compIn);
			in = new FSTObjectInput(fileIn, makeFSTConfig(fileLoc));
			for(int i = 0; i < N; i++){
				outVal = (IValue) in.readObject(IValue.class);
				System.out.println(outVal);
				System.out.println(outVal);
			}
			in.close();
			in = null;

		} catch (ClassNotFoundException c) {
			throw new IOException("Class not found: " + c.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		} 
		finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
  		}
		long endRead = Timing.getCpuTime();
		//System.out.println(outVal);
		System.out.println("Reading " + N + " value " +  (endRead - endWrite)/1000000 + " msec");


	}
}
