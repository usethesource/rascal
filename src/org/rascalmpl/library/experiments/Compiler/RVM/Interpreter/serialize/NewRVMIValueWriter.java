package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Set;

import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.OverloadedFunctionType;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.MapLastWritten;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.util.TrackLastWritten;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
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
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
        	
/**
 * RVMIValueWriter is a binary serializer for IValues and Types. The main public functions are:
 * - writeType
 * - writeValue
 */
	        
public class NewRVMIValueWriter {

	private final TrackLastWritten<Type> typeCache;
	private final TrackLastWritten<IValue> valueCache;
	private final TrackLastWritten<ISourceLocation> uriCache;
	
	protected static final byte[] header = { 'R', 'V', 1,0,0 };

	private final IInteger minInt;
	private final IInteger maxInt;

	private OutputStream basicOut;
	private NewStyleWriter writer;

	public NewRVMIValueWriter(OutputStream out, int typeWindowSize, int valueWindowSize, int uriWindowSize) throws IOException {
		this.basicOut = out;
		
		assert typeWindowSize > 0 && typeWindowSize < 255;
        assert valueWindowSize > 0 && valueWindowSize < 255;
        assert uriWindowSize > 0 && uriWindowSize < 255;
       
        out.write(header);
    	out.write(typeWindowSize);
    	out.write(valueWindowSize);
    	out.write(uriWindowSize);
    	
    	this.writer = new NewStyleWriter(out);

//		store = ts;
//		store.extendStore(RascalValueFactory.getStore());
		
		typeCache = new MapLastWritten<>(typeWindowSize * 1024);
		valueCache = new MapLastWritten<>(valueWindowSize * 1024);
		uriCache = new MapLastWritten<>(uriWindowSize * 1024);
		 
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		minInt = vf.integer(Integer.MIN_VALUE);
		maxInt = vf.integer(Integer.MAX_VALUE);
	}
	
	public void close() throws IOException {
		writer.flush();
		basicOut.close();
	}
	
	NewStyleWriter getWriter() {
		return writer;
	}
	
	private void writeAtomicType(int valId) throws IOException{
		writer.startValue(valId);
		writer.endValue();
	}
	
	void writeName(int fieldId, String name) throws IOException{
			writer.writeField(fieldId, name);
	}
	
	void writeNames(int fieldId, String[] names) throws IOException{
		int n = names.length;
		writer.writeField(fieldId, n);
		for(int i = 0; i < n; i++){
			writeName(fieldId, names[i]);
		}
	}
	
	/**
	 * Write type t to the output stream. Types are shared when possible.
	 * @param t
	 * @throws IOException
	 */
	public void writeType(Type value) throws IOException {
		writer.assertNotClosed();
		int alreadyWritten = typeCache.howLongAgo(value);
		if (alreadyWritten != -1) {
			writer.startValue(SType.PREVIOUS);
			writer.writeField(SType.PREVIOUS_TYPE, alreadyWritten);
			writer.endValue();
		}
		else {
			writeType1(value);
			typeCache.write(value);
		}
	}
	
	public void writeField(int fieldId, Type value) throws IOException {
		writer.assertNotClosed();
		int alreadyWritten = typeCache.howLongAgo(value);
		if (alreadyWritten != -1) {
			writer.writeField(SType.PREVIOUS_TYPE, alreadyWritten);
		}
		else {
			writer.writeField(fieldId, 0);
			writeType1(value);
			typeCache.write(value);
		}
	}
	
	private void writeType1(Type t) throws IOException {
		t.accept(new ITypeVisitor<Void,IOException>() {

			// Atomic types
			
			@Override
			public Void visitBool(Type type) throws IOException {
				writeAtomicType(SType.BOOL);
				return null;
			}
			
			@Override
			public Void visitDateTime(Type type) throws IOException {
				writeAtomicType(SType.DATETIME);
				return null;
			}
			
			@Override
			public Void visitInteger(Type type) throws IOException {
				writeAtomicType(SType.INT);
				return null;
			}
			
			@Override
			public Void visitNode(Type type) throws IOException {
				writeAtomicType(SType.NODE);
				return null;
			}
			
			@Override
			public Void visitNumber(Type type) throws IOException {
				writeAtomicType(SType.NUMBER);
				return null;
			}
			
			@Override
			public Void visitRational(Type type) throws IOException {
				writeAtomicType(SType.RAT);
				return null;
			}
			
			
			@Override
			public Void visitReal(Type type) throws IOException {
				writeAtomicType(SType.REAL);
				return null;
			}
			
			@Override
			public Void visitSourceLocation(Type type) throws IOException {
				writeAtomicType(SType.LOC);
				return null;
			}
			
			@Override
			public Void visitString(Type type) throws IOException {
				writeAtomicType(SType.STR);
				return null;
			}
			
			@Override
			public Void visitValue(Type type) throws IOException {
				writeAtomicType(SType.VALUE);
				return null;
			}

			@Override
			public Void visitVoid(Type type) throws IOException {
				writeAtomicType(SType.VOID);
				return null;
			}
			
			// Composite types
			
			@Override
			public Void visitAbstractData(Type type) throws IOException {
				writer.startValue(SType.ADT);
				
				writer.writeField(SType.ADT_NAME,  type.getName());
				
				writeField(SType.ADT_TYPE_PARAMETERS, type.getTypeParameters());
				
				writer.endValue();
				return null;
			}
			
			@Override
			public Void visitAlias(Type type) throws IOException {
				writer.startValue(SType.ALIAS);
				writer.writeField(SType.ALIAS_NAME,  type.getName());
				
				writeField(SType.ALIAS_ALIASED, type.getAliased());
				writeField(SType.ALIAS_TYPE_PARAMETERS, type.getTypeParameters());
				
				writer.endValue();
				return null;
			}
			
			@Override
			public Void visitConstructor(Type type) throws IOException {
				
				writer.startValue(SType.CONSTRUCTOR);
				writer.writeField(SType.CONSTRUCTOR_NAME,  type.getName());
				
				writeField(SType.CONSTRUCTOR_ABSTRACT_DATA_TYPE, type.getAbstractDataType());
				
				writeField(SType.CONSTRUCTOR_TYPE, type.getFieldTypes());
				
				writer.endValue();
				return null;
			}
			
			@Override
			public Void visitExternal(Type type) throws IOException {
				if(type instanceof FunctionType){
					writer.startValue(SType.FUNCTION);
					FunctionType ft = (FunctionType) type;
		
					writeField(SType.FUNCTION_RETURN_TYPE, ft.getReturnType());
					writeField(SType.FUNCTION_ARGUMENT_TYPES, ft.getArgumentTypes());
					writeField(SType.FUNCTION_KEYWORD_PARAMETER_TYPES, ft.getKeywordParameterTypes());
					writer.endValue();
					
				} else if(type instanceof ReifiedType){
					writer.startValue(SType.REIFIED);
					ReifiedType rt = (ReifiedType) type;
					writeField(SType.REIFIED_ELEMENT_TYPE, rt.getTypeParameters());
					writer.endValue();
					
				} else if(type instanceof OverloadedFunctionType){
					writer.startValue(SType.OVERLOADED);
					
					Set<FunctionType> alternatives = ((OverloadedFunctionType) type).getAlternatives();
					int arity = alternatives.size();
					writer.writeField(SType.OVERLOADED_TYPES, arity);
					for(FunctionType ft : alternatives){
						writeType(ft);
					}
					writer.endValue();
				} 
				else if(type instanceof NonTerminalType){
					NonTerminalType nt = (NonTerminalType) type;
					writer.startValue(SType.NONTERMINAL);
					writer.writeField(SType.NONTERMINAL_CONSTRUCTOR, 0);
					IConstructor cons = nt.getSymbol();
					writeValue(cons);
					writer.endValue();
				} 
				else {
					throw new RuntimeException("External type not supported: " + type);
				}
				return null;
			}

			@Override
			public Void visitList(Type type) throws IOException {
				writer.startValue(SType.LIST);
				writeField(SType.LIST_ELEMENT_TYPE, type.getElementType());
				writer.endValue();
				return null;
			}

			@Override
			public Void visitMap(Type type) throws IOException {
				writer.startValue(SType.MAP);
				
				String keyLabel = type.getKeyLabel();
				String valLabel = type.getValueLabel();
			
				if(keyLabel != null && valLabel != null){
					writer.writeField(SType.MAP_KEY_LABEL, keyLabel);
					writer.writeField(SType.MAP_VAL_LABEL, valLabel);
				}
				writeField(SType.MAP_KEY_TYPE, type.getKeyType());
				writeField(SType.MAP_VAL_TYPE, type.getValueType());
				writer.endValue();
				return null;
			}
			
			@Override
			public Void visitParameter(Type type) throws IOException {
				writer.startValue(SType.PARAMETER);
				
				writer.writeField(SType.PARAMETER_NAME, type.getName());
				writeField(SType.PARAMETER_BOUND, type.getBound());
				
				writer.endValue();
				return null;
			}

			@Override
			public Void visitSet(Type type) throws IOException {
				writer.startValue(SType.SET);
				
				writeField(SType.SET_ELEMENT_TYPE, type.getElementType());
				
				writer.endValue();
				return null;
			}

			@Override
			public Void visitTuple(Type type) throws IOException {
				writer.startValue(SType.TUPLE);
				
				String[] fieldNames = type.getFieldNames();
				if(fieldNames != null){
					writeNames(SType.TUPLE_NAMES, fieldNames);
				}
				
				int arity = type.getArity();
				writer.writeField(SType.TUPLE_TYPES, arity);
				for(int i = 0; i < arity; i++){
					writeType(type.getFieldType(i));
				}
				
				writer.endValue();
				return null;
			}
		});
	}
	
	boolean inCache(IValue v) throws IOException{
		int id = valueCache.howLongAgo(v);
		if(id > -1){
			writer.startValue(SValue.PREVIOUS);
			writer.writeField(SValue.PREVIOUS_VALUE, id);
			writer.endValue();
			return true;
		}
		return false;
	}
	
	void endAndCacheValue(IValue v) throws IOException{
		writer.endValue();
		valueCache.write(v);
	}
	
	/**
	 * Write value v to the output stream.
	 * @param v
	 * @throws IOException
	 */
	public void writeValue(IValue v) throws IOException{
		LazyPrePostIValueIterator it = new LazyPrePostIValueIterator(v);
		
		while(it.hasNext()){
			Kind kind = it.next();
			switch(kind){
			case BOOL: {
				assert it.atBeginning();
				IBool b = (IBool) it.getIValue();
				if(!inCache(b)){
					writer.startValue(SValue.BOOL);
					writer.writeField(SValue.BOOL_VALUE, b.getValue() ? 1 : 0);
					endAndCacheValue(b);
				}
				break;
			}

			case CONSTRUCTOR: {
				IConstructor cons = (IConstructor) it.getIValue();
				if(it.atBeginning()){
					if(inCache(cons)){
						it.skipIValue();
					}
				} else {
					writer.startValue(SValue.CONSTRUCTOR);
					writer.writeField(SValue.CONSTRUCTOR_ARITY, cons.arity());
					if(cons.mayHaveKeywordParameters()){
						if(cons.asWithKeywordParameters().hasParameters()){
							writer.writeField(SValue.CONSTRUCTOR_KWPARAMS, cons.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(cons.asAnnotatable().hasAnnotations()){
							writer.writeField(SValue.CONSTRUCTOR_ANNOS, cons.asAnnotatable().getAnnotations().size());
						}
					}
					writeField(SValue.CONSTRUCTOR_TYPE, cons.getUninstantiatedConstructorType());
					endAndCacheValue(cons);
				}
				break;
			}
			
			case DATETIME: {
				assert it.atBeginning();
				
				IDateTime dateTime = (IDateTime) it.getIValue();
				if(!inCache(dateTime)){
						if(dateTime.isDateTime()){
						writer.startValue(SValue.DATETIME);
						
						writer.writeField(SValue.DATETIME_YEAR, dateTime.getYear());
						writer.writeField(SValue.DATETIME_MONTH, dateTime.getMonthOfYear());
						writer.writeField(SValue.DATETIME_DAY, dateTime.getDayOfMonth());
						
						writer.writeField(SValue.DATETIME_HOUR, dateTime.getHourOfDay());
						writer.writeField(SValue.DATETIME_MINUTE, dateTime.getMinuteOfHour());
						writer.writeField(SValue.DATETIME_SECOND, dateTime.getSecondOfMinute());
						writer.writeField(SValue.DATETIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						writer.writeField(SValue.DATETIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						writer.writeField(SValue.DATETIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
					} else if(dateTime.isDate()){
						writer.startValue(SValue.DATE);
						
						writer.writeField(SValue.DATE_YEAR, dateTime.getYear());
						writer.writeField(SValue.DATE_MONTH, dateTime.getMonthOfYear());
						writer.writeField(SValue.DATE_DAY, dateTime.getDayOfMonth());
					} else {
						writer.startValue(SValue.TIME);
						
						writer.writeField(SValue.TIME_HOUR, dateTime.getHourOfDay());
						writer.writeField(SValue.TIME_MINUTE, dateTime.getMinuteOfHour());
						writer.writeField(SValue.TIME_SECOND, dateTime.getSecondOfMinute());
						writer.writeField(SValue.TIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						writer.writeField(SValue.TIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						writer.writeField(SValue.TIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
					}
					endAndCacheValue(dateTime);
				}
				break;
			}

			case INT: {
				assert it.atBeginning();
				IInteger ii = (IInteger) it.getIValue();
				if(!inCache(ii)){
					if(ii.greaterEqual(minInt).getValue() && ii.lessEqual(maxInt).getValue()){
						int n = ii.intValue();
						writer.startValue(SValue.INT);
						writer.writeField(SValue.INT_VALUE, n);
					} else {
						writer.startValue(SValue.BIGINT);
						byte[] valueData = ii.getTwosComplementRepresentation();
						writer.writeField(SValue.BIGINT_VALUE, valueData);
					}
					endAndCacheValue(ii);
				}
				break;
			}
			
			case LIST: {
				IList lst = (IList) it.getIValue();
				if(it.atBeginning()){
					if(inCache(lst)){
						it.skipIValue();
					}
				} else {
					writer.startValue(SValue.LIST);
					writer.writeField(SValue.LIST_SIZE, lst.length());
					endAndCacheValue(lst);
				}
				break;
			}
			
			case MAP: {
				IMap  map = (IMap) it.getIValue();
				if(it.atBeginning()){
					if(inCache(map)){
						it.skipIValue();
					}
				} else {
					writer.startValue(SValue.MAP);
					writer.writeField(SValue.MAP_SIZE, map.size());
					endAndCacheValue(map);
				}
				break;
			}

			case NODE: {
				INode node = (INode) it.getIValue();
				if(it.atBeginning()){
					if(inCache(node)){
						it.skipIValue();
					}
				} else {
					writer.startValue(SValue.NODE);
					writer.writeField(SValue.NODE_NAME,  node.getName());
					writer.writeField(SValue.NODE_ARITY, node.arity());
					if(node.mayHaveKeywordParameters()){
						if(node.asWithKeywordParameters().hasParameters()){
							writer.writeField(SValue.NODE_KWPARAMS, node.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(node.asAnnotatable().hasAnnotations()){
							writer.writeField(SValue.NODE_ANNOS, node.asAnnotatable().getAnnotations().size());
						}
					}
					endAndCacheValue(node);
				}
				break;
			}
					
			case RATIONAL: {
				assert it.atBeginning();
				IRational rat = (IRational) it.getIValue();
				if(!inCache(rat)){
					writer.startValue(SValue.RAT);
					writer.writeField(SValue.RAT_NUMERATOR, 0);
					writeValue(rat.numerator());
					writer.writeField(SValue.RAT_DENOMINATOR, 0);
					writeValue(rat.denominator());
					endAndCacheValue(rat);
				}
				break;
			}
				
			case REAL: {
				assert it.atBeginning();

				IReal real = (IReal) it.getIValue();
				if(!inCache(real)){
					writer.startValue(SValue.REAL);
					byte[] valueData = real.unscaled().getTwosComplementRepresentation();
					writer.writeField(SValue.REAL_VALUE, valueData);
					writer.writeField(SValue.REAL_SCALE, real.scale());
					endAndCacheValue(real);
				}
				break;
			}
			
			case SET: {
				ISet set = (ISet) it.getIValue();
				if(it.atBeginning()){
					if(inCache(set)){
						it.skipIValue();
					}
				} else {
					writer.startValue(SValue.SET);
					writer.writeField(SValue.SET_SIZE, set.size());
					endAndCacheValue(set);
				}
				break;
			}

			case LOC: {
				assert it.atBeginning();
				
				ISourceLocation loc = (ISourceLocation) it.getIValue();
				if(!inCache(loc)){
					writer.startValue(SValue.LOC);
					ISourceLocation uriPart = loc.top();
					int alreadyWritten = uriCache.howLongAgo(uriPart);
					if (alreadyWritten == -1) {
					    writer.writeField(SValue.LOC_SCHEME, uriPart.getScheme());
					    if (uriPart.hasAuthority()) {
					        writer.writeField(SValue.LOC_AUTHORITY, uriPart.getAuthority());
					    }
					    if (uriPart.hasPath()) {
					        writer.writeField(SValue.LOC_PATH, uriPart.getPath());
					    }
					    if (uriPart.hasQuery()) {
					        writer.writeField(SValue.LOC_QUERY,  uriPart.getQuery());
					    }
					    if (uriPart.hasFragment()) {
					        writer.writeField(SValue.LOC_FRAGMENT,  uriPart.getFragment());
					    }
					    uriCache.write(uriPart);
					}
					else {
					    writer.writeField(SValue.LOC_PREVIOUS_URI, alreadyWritten);
					}
					
					if(loc.hasOffsetLength()){
						writer.writeField(SValue.LOC_OFFSET, loc.getOffset());
						writer.writeField(SValue.LOC_LENGTH, loc.getLength());
					} 
					if(loc.hasLineColumn()){
						writer.writeField(SValue.LOC_BEGINLINE, loc.getBeginLine());
						writer.writeField(SValue.LOC_ENDLINE, loc.getEndLine());
						writer.writeField(SValue.LOC_BEGINCOLUMN, loc.getBeginColumn());
						writer.writeField(SValue.LOC_ENDCOLUMN, loc.getEndColumn());
					}
					endAndCacheValue(loc);
				}
				break;
			}
				
			case STR: {
				assert it.atBeginning();
				
				IString str = (IString) it.getIValue();
				writer.startValue(SValue.STR);
				writer.writeField(SValue.STR_VALUE, str.getValue());
				writer.endValue();
				// Already cached at wire level
				break;
			}

			case TUPLE: {
				ITuple tuple = (ITuple) it.getIValue();
				if(it.atBeginning()){
					if(inCache(tuple)){
						it.skipIValue();
					}
				} else {
					writer.startValue(SValue.TUPLE);
					writer.writeField(SValue.TUPLE_SIZE, tuple.arity());
					endAndCacheValue(tuple);
				}
				break;
			}
			default:
				 throw new RuntimeException("writeValue: unexpected kind of value " + kind);
			}
		}
		writer.startValue(SValue.END_OF_VALUE);
		writer.endValue();
	}
	
  // Test code
    
    public static void main(String[] args) throws Exception {
    	TypeFactory tf = TypeFactory.getInstance();
    	IValueFactory vf = ValueFactoryFactory.getValueFactory();
    	TypeStore ts = RascalValueFactory.getStore();
    	 try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
    		 NewRVMIValueWriter ser = new NewRVMIValueWriter(out, 10, 10, 10);
//    		 Type ct = tf.constructor(ts, tf.abstractDataType(ts, "D"), "f", tf.integerType());
//    		 IConstructor nd = vf.constructor(ct, vf.integer(42));
//    		 nd = nd.asWithKeywordParameters().setParameter("a", vf.integer(1));
//    		 nd = nd.asWithKeywordParameters().setParameter("b", vf.string("xyz"));
    		 
    		 
    		 Type maybe = tf.abstractDataType(ts, "Maybe", tf.parameterType("T"));
    		 
    		 Type none = tf.constructor(ts, maybe, "none");
    		 
    		 IValue v = vf.constructor(none);
    		 
 
    		 System.out.println(v);
    		 ser.writeValue(v);
    		 ser.close();
    		 try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                 NewRVMIValueReader reader = new NewRVMIValueReader(in, vf, ts);
                 System.out.println(reader.readValue());
             }
    		 
    	 }
    }
}
