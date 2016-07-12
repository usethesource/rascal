package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.ReifiedType;
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
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.IndexedSet;
import org.rascalmpl.value.visitors.IValueVisitor;
import org.rascalmpl.values.ValueFactoryFactory;


/**
 * The TYPEs distinguished during (de)serialization.
 */
enum TYPE {REAL, INT, RAT, LIST, MAP, MAP_NAMED_FIELDS, NUMBER, ALIAS, SET, LOC, STR,
	       NODE, CONSTRUCTOR, CONSTRUCTOR_NAMED_FIELDS, 
	       ADT, TUPLE, TUPLE_NAMED_FIELDS, VALUE, VOID, BOOL, PARAMETER, 
	       FUNCTION, REIFIED, OVERLOADED, NONTERMINAL, DATETIME, SHARED_TYPE};

/**
 * The VALUEs distinguished during (de)serialization.
 */
enum VALUE {REAL, INT, BIG_INT,
	        INT_10, INT_9, INT_8, INT_7, INT_6, INT_5, INT_4, INT_3, INT_2, INT_1,
	        INT0, INT1, INT2, INT3, INT4, INT5, INT6, INT7, INT8, INT9, INT10,
	        RAT, LIST, MAP, SET, LOC, STR, SHARED_STR, NAME, SHARED_NAME,
	        NODE, NODE_ANNOTATIONS, NODE_KEYWORDS,
	        CONSTRUCTOR, CONSTRUCTOR_ANNOTATIONS, CONSTRUCTOR_KEYWORDS,
	        TUPLE, BOOL, FUNCTION, DATE, TIME, DATE_TIME, 
	        URI, SHARED_URI};

/**
 * RVMIValueWriter is a binary serializer for IValues and Types. The main public functions are:
 * - writeType
 * - writeValue
 */
	        
public class RVMIValueWriter   {	
	private final static int MAX_BYTE = 127;

	transient private static IndexedSet<IValue> sharedValues;
	transient private static IndexedSet<Type> sharedTypes;
	private final IndexedSet<String> sharedPaths;
	transient private static IndexedSet<String> sharedNames;
	
	private final IInteger minInt;
	private final IInteger maxInt;

	private RVMOutputStream out;

	public RVMIValueWriter(OutputStream out) {
		this.out = new RVMOutputStream(out);
//		store = ts;
//		store.extendStore(RascalValueFactory.getStore());
		
		sharedValues = new IndexedSet<>();
		sharedTypes = new IndexedSet<>();
		sharedPaths = new IndexedSet<>();
		sharedNames = new IndexedSet<>();
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		minInt = vf.integer(Integer.MIN_VALUE);
		maxInt = vf.integer(Integer.MAX_VALUE);
	}
	
	public void close() throws IOException {
		out.close();
	}
	
	RVMOutputStream getOut() {
		return out;
	}
	
	private void writeTypeEnum(TYPE t) throws IOException{
		out.write(t.ordinal());
	}
	
	private void writeValueEnum(VALUE v) throws IOException{
		out.write(v.ordinal());
	}
	
	private void writeArity(int arity) throws IOException{
		if(arity >= MAX_BYTE){
			throw new IllegalArgumentException("Arity " + arity + " not supported");
		}
		out.writeByte(arity);
	}
	
	private void writeLength(int length) throws IOException{
		out.writeInt(length);
	}
	
	/**
	 * Write a "name": for the sake of (de) serialization, a name is a Java string 
	 * that can be used in various positions, e.g. function name, alias, adt name,
	 * field name, etc.
	 * @param name
	 * @throws IOException
	 */
	void writeName(String name) throws IOException{
		int nameId = sharedNames.store(name);
		if(nameId >= 0){
			writeValueEnum(VALUE.SHARED_NAME);
		   out.writeInt(nameId);
		   
		} else {
			writeValueEnum(VALUE.NAME);
			out.writeString(name);
		}
	}
	
	void writeNames(String[] names) throws IOException{
		int n = names.length;
		writeLength(n);
		for(int i = 0; i < n; i++){
			writeName(names[i]);
		}
	}
	
	private void writeIString(IString s) throws IOException{
		int stringId = sharedValues.store(s);
		if(stringId >= 0){
			writeValueEnum(VALUE.SHARED_STR);
			out.writeInt(stringId);
		} else {
			writeValueEnum(VALUE.STR);
			out.writeString(s.getValue());
		}
	}
	
	private void writeURI(URI uri) throws IOException{
		String path = uri.toString();
		
		int uriId = sharedPaths.store(path);
		if(uriId >= 0){
			writeValueEnum(VALUE.SHARED_URI);
			out.writeInt(uriId);
		} else {
			writeValueEnum(VALUE.URI);
			out.writeString(path);
		}
	}
	
	private void writeIReal(IReal real) throws IOException{
		writeValueEnum(VALUE.REAL);
		
		byte[] valueData = real.unscaled().getTwosComplementRepresentation();
		int length = valueData.length;
		out.writeInt(length);
		out.write(valueData, 0, length);
		out.write(real.scale());
	}
	
	private void writeIInteger(IInteger val) throws IOException{
		if(val.greaterEqual(minInt).getValue() && val.lessEqual(maxInt).getValue()){
			int n = val.intValue();
			switch(n){
			case -10:	writeValueEnum(VALUE.INT_10);break;
			case -9:	writeValueEnum(VALUE.INT_9);break;
			case -8:	writeValueEnum(VALUE.INT_8);break;
			case -7:	writeValueEnum(VALUE.INT_7);break;
			case -6:	writeValueEnum(VALUE.INT_6);break;
			case -5:	writeValueEnum(VALUE.INT_5);break;
			case -4:	writeValueEnum(VALUE.INT_4);break;
			case -3:	writeValueEnum(VALUE.INT_3);break;
			case -2:	writeValueEnum(VALUE.INT_2);break;
			case -1:	writeValueEnum(VALUE.INT_1);break;
			case 0:		writeValueEnum(VALUE.INT0);break;
			case 1:		writeValueEnum(VALUE.INT1);break;
			case 2:		writeValueEnum(VALUE.INT2);break;
			case 3:		writeValueEnum(VALUE.INT3);break;
			case 4:		writeValueEnum(VALUE.INT4);break;
			case 5:		writeValueEnum(VALUE.INT5);break;
			case 6:		writeValueEnum(VALUE.INT6);break;
			case 7:		writeValueEnum(VALUE.INT7);break;
			case 8:		writeValueEnum(VALUE.INT8);break;
			case 9:		writeValueEnum(VALUE.INT9);break;
			case 10:	writeValueEnum(VALUE.INT10);break;
			default:
				writeValueEnum(VALUE.INT);
				out.writeInt(n);
			}
		} else {
			writeValueEnum(VALUE.BIG_INT);
			byte[] valueData = val.getTwosComplementRepresentation();
			int length = valueData.length;
			out.writeInt(length);
			out.write(valueData, 0, length);
		}
	}
	
	private void writeKeywordParamsOrAnnos(Map<String, IValue> kwParamsOrAnnos) throws IOException{
		writeArity(kwParamsOrAnnos.size());
		for (Map.Entry<String, IValue> param : kwParamsOrAnnos.entrySet()) {
			writeName(param.getKey());
			writeValue(param.getValue());
		}
	}
	
	/**
	 * Write type t to the output stream. Types are shared when possible.
	 * @param t
	 * @throws IOException
	 */
	public void writeType(Type t) throws IOException {
		int typeId = sharedTypes.get(t);
		if(typeId >= 0){
			//System.out.println("writeType: " + t + " shared as " + typeId);
			writeTypeEnum(TYPE.SHARED_TYPE);
			out.writeInt(typeId);
			return;
		}
		writeType1(t);
		sharedTypes.store(t);
	}
	
	private void writeType1(Type t) throws IOException {
		t.accept(new ITypeVisitor<Void,IOException>() {

			// Atomic types
			
			@Override
			public Void visitBool(Type type) throws IOException {
				writeTypeEnum(TYPE.BOOL);
				return null;
			}
			
			@Override
			public Void visitDateTime(Type type) throws IOException {
				writeTypeEnum(TYPE.DATETIME);
				return null;
			}
			
			@Override
			public Void visitInteger(Type type) throws IOException {
				writeTypeEnum(TYPE.INT);
				return null;
			}
			
			@Override
			public Void visitNode(Type type) throws IOException {
				writeTypeEnum(TYPE.NODE);
				return null;
			}
			
			@Override
			public Void visitNumber(Type type) throws IOException {
				writeTypeEnum(TYPE.NUMBER);
				return null;
			}
			
			@Override
			public Void visitRational(Type type) throws IOException {
				writeTypeEnum(TYPE.RAT);
				return null;
			}
			
			
			@Override
			public Void visitReal(Type type) throws IOException {
				writeTypeEnum(TYPE.REAL);
				return null;
			}
			
			@Override
			public Void visitSourceLocation(Type type) throws IOException {
				writeTypeEnum(TYPE.LOC);
				return null;
			}
			
			@Override
			public Void visitString(Type type) throws IOException {
				writeTypeEnum(TYPE.STR);
				return null;
			}
			
			@Override
			public Void visitValue(Type type) throws IOException {
				writeTypeEnum(TYPE.VALUE);
				return null;
			}

			@Override
			public Void visitVoid(Type type) throws IOException {
				writeTypeEnum(TYPE.VOID);
				return null;
			}
			
			// Composite types
			
			@Override
			public Void visitAbstractData(Type type) throws IOException {
				writeTypeEnum(TYPE.ADT);
				writeName(type.getName());
				Type typeParameters = type.getTypeParameters();
				writeType(typeParameters);
				return null;
			}
			
			@Override
			public Void visitAlias(Type type) throws IOException {
				writeTypeEnum(TYPE.ALIAS);
				writeName(type.getName());
				writeType(type.getAliased());
				writeType(type.getTypeParameters());
				return null;
			}
			
			@Override
			public Void visitConstructor(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				if(fieldNames == null){
					writeTypeEnum(TYPE.CONSTRUCTOR);
				} else {
					writeTypeEnum(TYPE.CONSTRUCTOR_NAMED_FIELDS);
					writeNames(fieldNames);
				}
				writeName(type.getName());
				int arity = type.getArity();
				writeArity(arity);
				
				writeType(type.getAbstractDataType());
				Type elemType = type.getFieldTypes();
				for(int i = 0; i < arity; i++){
					writeType(elemType.getFieldType(i));
				}
				return null;
			}
			
			@Override
			public Void visitExternal(Type type) throws IOException {
				if(type instanceof FunctionType){
					FunctionType ft = (FunctionType) type;
					writeTypeEnum(TYPE.FUNCTION);
					writeType(ft.getReturnType());
					writeType(ft.getArgumentTypes());
					writeType(ft.getKeywordParameterTypes());
				} else if(type instanceof ReifiedType){
					//System.out.println("writeType: " + type);
					ReifiedType rt = (ReifiedType) type;
					writeTypeEnum(TYPE.REIFIED);
					Type elemType = rt.getTypeParameters();  // TODO ok?
					writeType(elemType);
				} else if(type instanceof OverloadedFunctionType){
					writeTypeEnum(TYPE.OVERLOADED);
					Set<FunctionType> alternatives = ((OverloadedFunctionType) type).getAlternatives();
					out.writeInt(alternatives.size());
					for(FunctionType ft : alternatives){
						writeType(ft);
					}
				} else if(type instanceof NonTerminalType){
					writeTypeEnum(TYPE.NONTERMINAL);
					NonTerminalType nt = (NonTerminalType) type;
					IConstructor cons = nt.getSymbol();
					writeValue(cons);
				} else {
					throw new RuntimeException("External type not supported: " + type);
				}
				return null;
			}

			@Override
			public Void visitList(Type type) throws IOException {
				writeTypeEnum(TYPE.LIST);
				writeType(type.getElementType());
				return null;
			}

			@Override
			public Void visitMap(Type type) throws IOException {
				String keyLabel = type.getKeyLabel();
				String valLabel = type.getValueLabel();
				
				if(keyLabel == null && valLabel == null){
					writeTypeEnum(TYPE.MAP);
				} else {
					writeTypeEnum(TYPE.MAP_NAMED_FIELDS);
					writeName(keyLabel);
					writeName(valLabel);
				}
				writeType(type.getKeyType());
				writeType(type.getValueType());
				return null;
			}
			
			@Override
			public Void visitParameter(Type type) throws IOException {
				writeTypeEnum(TYPE.PARAMETER);
				writeName(type.getName());
				writeType(type.getBound());
				return null;
			}

			@Override
			public Void visitSet(Type type) throws IOException {
				writeTypeEnum(TYPE.SET);
				writeType(type.getElementType());
				return null;
			}

			@Override
			public Void visitTuple(Type type) throws IOException {
				String[] fieldNames = type.getFieldNames();
				
				if(fieldNames == null){
					writeTypeEnum(TYPE.TUPLE);
				} else {
					writeTypeEnum(TYPE.TUPLE_NAMED_FIELDS);
					writeNames(fieldNames);
				}
				int arity = type.getArity();
				writeArity(arity);
				for(int i = 0; i < arity; i++){
					writeType(type.getFieldType(i));
				}
				return null;
			}
		});
	}
	
	/**
	 * Write value v to the output stream.
	 * @param v
	 * @throws IOException
	 */
	public void writeValue(IValue v) throws IOException{
		
		v.accept(new IValueVisitor<Void,IOException>() {

			@Override
			public Void visitBoolean(IBool val) throws IOException {
				writeValueEnum(VALUE.BOOL);
				out.writeBool(val.getValue());
				return null;
			}

			@Override
			public Void visitConstructor(IConstructor cons) throws IOException {
				if(cons.mayHaveKeywordParameters()){
					if(cons.asWithKeywordParameters().hasParameters()){
						writeValueEnum(VALUE.CONSTRUCTOR_KEYWORDS);
						writeKeywordParamsOrAnnos(cons.asWithKeywordParameters().getParameters());
					} else {
						writeValueEnum(VALUE.CONSTRUCTOR);
					}
				} else {
					if(cons.isAnnotatable() && cons.asAnnotatable().hasAnnotations()){
						writeValueEnum(VALUE.CONSTRUCTOR_ANNOTATIONS);
						writeKeywordParamsOrAnnos(cons.asAnnotatable().getAnnotations());
					} else {
						writeValueEnum(VALUE.CONSTRUCTOR);
					}
				}
				Type consType = cons.getUninstantiatedConstructorType();
				//Type consType = cons.getConstructorType();
				writeType(consType);
				int arity = cons.arity();
				writeArity(arity);
				for(int i = 0; i < arity; i++){
					writeValue(cons.get(i));
				}
				return null;
			}

			@Override
			public Void visitDateTime(IDateTime dateTime) throws IOException {
				if(dateTime.isDateTime()){
					writeValueEnum(VALUE.DATE_TIME);
					
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
					writeValueEnum(VALUE.DATE);
					
					out.writeInt(dateTime.getYear());
					out.writeInt(dateTime.getMonthOfYear());
					out.writeInt(dateTime.getDayOfMonth());
				} else {
					writeValueEnum(VALUE.TIME);
					
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
				writeIInteger(val);
				return null;
			}

			@Override
			public Void visitList(IList val) throws IOException {
				writeValueEnum(VALUE.LIST);
				writeType(val.getElementType());
				writeLength(val.length());
				for(IValue v : val){
					writeValue(v);
				}
				return null;
			}

			@Override
			public Void visitListRelation(IList val) throws IOException {
				visitList(val);
				return null;
			}

			@Override
			public Void visitMap(IMap val) throws IOException {
				writeValueEnum(VALUE.MAP);
				writeType(val.getType());
				writeLength(val.size());
				for(IValue key : val){
					writeValue(key);
					writeValue(val.get(key));
				}
				return null;
			}

			@Override
			public Void visitNode(INode val) throws IOException {
				if(val.mayHaveKeywordParameters()){
					if(val.asWithKeywordParameters().hasParameters()){
						writeValueEnum(VALUE.NODE_KEYWORDS);
						writeKeywordParamsOrAnnos(val.asWithKeywordParameters().getParameters());
					} else {
						writeValueEnum(VALUE.NODE);
					}
				} else {
					if(val.asAnnotatable().hasAnnotations()){
						writeValueEnum(VALUE.NODE_ANNOTATIONS);
						writeKeywordParamsOrAnnos(val.asAnnotatable().getAnnotations());
					} else {
						writeValueEnum(VALUE.NODE);
					}
				}
				writeName(val.getName());
				int arity = val.arity();
				writeArity(arity);
				for(int i = 0; i < arity; i++){
					writeValue(val.get(i));
				}
				return null;
			}

			@Override
			public Void visitRational(IRational val) throws IOException {
				writeValueEnum(VALUE.RAT);
				writeIInteger(val.numerator());
				writeIInteger(val.denominator());
				return null;
			}

			@Override
			public Void visitReal(IReal val) throws IOException {
				writeIReal(val);
				return null;
			}

			@Override
			public Void visitRelation(ISet val) throws IOException {
				visitSet(val);
				return null;
			}

			@Override
			public Void visitSet(ISet val) throws IOException {
				writeValueEnum(VALUE.SET);
				writeType(val.getElementType());
				writeLength(val.size());
				for(IValue v : val){
					writeValue(v);
				}
				return null;
			}

			@Override
			public Void visitSourceLocation(ISourceLocation val) throws IOException {
				writeValueEnum(VALUE.LOC);
				writeURI(val.getURI());
				
				//uri, offset, length, beginLine, endLine, beginCol, endCol
				if(val.hasOffsetLength()){
					out.writeInt(val.getOffset());
					out.writeInt(val.getLength());
				} else {
					out.writeInt(-1);
					return null;
				}
				if(val.hasLineColumn()){
					out.writeShort((short)val.getBeginLine());
					out.writeShort((short)val.getEndLine());
					out.writeShort((short)val.getBeginColumn());
					out.writeShort((short)val.getEndColumn());
				}
				return null;
			}

			@Override
			public Void visitString(IString val) throws IOException {
				writeIString(val);
				return null;
			}

			@Override
			public Void visitTuple(ITuple val) throws IOException {
				writeValueEnum(VALUE.TUPLE);
				writeType(val.getType());
				writeArity(val.arity());
				for(IValue v : val){
					writeValue(v);
				}
				return null;
			} });
	}
}
