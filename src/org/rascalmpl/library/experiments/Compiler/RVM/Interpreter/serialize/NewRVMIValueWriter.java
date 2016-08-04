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

	private final TrackLastWritten<Type> typesWindow;
	private final TrackLastWritten<IValue> valuesWindow;
	
	protected static final byte[] header = { 'R', 'V', 1,0,0 };

	private final IInteger minInt;
	private final IInteger maxInt;

	private OutputStream basicOut;
	private NewStyleWriter out;

	public NewRVMIValueWriter(OutputStream out, int typeWindowSize, int valueWindowSize) throws IOException {
		this.basicOut = out;
		
		assert typeWindowSize > 0 && typeWindowSize < 255;
        assert valueWindowSize > 0 && valueWindowSize < 255;
       
        out.write(header);
    	out.write(typeWindowSize);
    	out.write(valueWindowSize);
    	
    	this.out = new NewStyleWriter(out);

//		store = ts;
//		store.extendStore(RascalValueFactory.getStore());
		
		typesWindow = new MapLastWritten<>(typeWindowSize * 1024);
		valuesWindow = new MapLastWritten<>(valueWindowSize * 1024);
		 
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		minInt = vf.integer(Integer.MIN_VALUE);
		maxInt = vf.integer(Integer.MAX_VALUE);
	}
	
	public void close() throws IOException {
		out.flush();
		basicOut.close();
	}
	
	NewStyleWriter getOut() {
		return out;
	}
	
	private void writeAtomicType(int valId) throws IOException{
		out.startValue(valId);
		out.endValue();
	}
	
	/**
	 * Write a "name": for the sake of (de) serialization, a name is a Java string 
	 * that can be used in various positions, e.g. function name, alias, adt name,
	 * field name, etc.
	 * @param name
	 * @throws IOException
	 */
	void writeName(int fieldId, String name) throws IOException{
			out.writeField(fieldId, name);
	}
	
	void writeNames(int fieldId, String[] names) throws IOException{
		int n = names.length;
		out.writeField(fieldId, n);
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
		out.assertNotClosed();
		int alreadyWritten = typesWindow.howLongAgo(value);
		if (alreadyWritten != -1) {
			out.startValue(SType.PREVIOUS);
			out.writeField(SType.PREVIOUS_TYPE, alreadyWritten);
			out.endValue();
		}
		else {
			writeType1(value);
			typesWindow.write(value);
		}
	}
	
	public void writeField(int fieldId, Type value) throws IOException {
		out.assertNotClosed();
		int alreadyWritten = typesWindow.howLongAgo(value);
		if (alreadyWritten != -1) {
			out.writeField(SType.PREVIOUS_TYPE, alreadyWritten);
		}
		else {
			out.writeField(fieldId, 0);
			writeType1(value);
			typesWindow.write(value);
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
				out.startValue(SType.ADT);
				
				out.writeField(SType.ADT_NAME,  type.getName());
				
				writeField(SType.ADT_TYPE_PARAMETERS, type.getTypeParameters());
				
				out.endValue();
				return null;
			}
			
			@Override
			public Void visitAlias(Type type) throws IOException {
				out.startValue(SType.ALIAS);
				out.writeField(SType.ALIAS_NAME,  type.getName());
				
				writeField(SType.ALIAS_ALIASED, type.getAliased());
				writeField(SType.ALIAS_TYPE_PARAMETERS, type.getTypeParameters());
				
				out.endValue();
				return null;
			}
			
			@Override
			public Void visitConstructor(Type type) throws IOException {
				
				out.startValue(SType.CONSTRUCTOR);
				out.writeField(SType.CONSTRUCTOR_NAME,  type.getName());
				
				writeField(SType.CONSTRUCTOR_ABSTRACT_DATA_TYPE, type.getAbstractDataType());
				
				writeField(SType.CONSTRUCTOR_TYPE, type.getFieldTypes());
				
				out.endValue();
				return null;
			}
			
			@Override
			public Void visitExternal(Type type) throws IOException {
				if(type instanceof FunctionType){
					out.startValue(SType.FUNCTION);
					FunctionType ft = (FunctionType) type;
		
					writeField(SType.FUNCTION_RETURN_TYPE, ft.getReturnType());
					writeField(SType.FUNCTION_ARGUMENT_TYPES, ft.getArgumentTypes());
					writeField(SType.FUNCTION_KEYWORD_PARAMETER_TYPES, ft.getKeywordParameterTypes());
					out.endValue();
					
				} else if(type instanceof ReifiedType){
					out.startValue(SType.REIFIED);
					ReifiedType rt = (ReifiedType) type;
					writeField(SType.REIFIED_ELEMENT_TYPE, rt.getTypeParameters());
					out.endValue();
					
				} else if(type instanceof OverloadedFunctionType){
					out.startValue(SType.OVERLOADED);
					
					Set<FunctionType> alternatives = ((OverloadedFunctionType) type).getAlternatives();
					int arity = alternatives.size();
					out.writeField(SType.OVERLOADED_TYPES, arity);
					for(FunctionType ft : alternatives){
						writeType(ft);
					}
					out.endValue();
				} 
				else if(type instanceof NonTerminalType){
					NonTerminalType nt = (NonTerminalType) type;
					out.startValue(SType.NONTERMINAL);
					out.writeField(SType.NONTERMINAL_CONSTRUCTOR, 0);
					IConstructor cons = nt.getSymbol();
					writeValue(cons);
					out.endValue();
				} 
				else {
					throw new RuntimeException("External type not supported: " + type);
				}
				return null;
			}

			@Override
			public Void visitList(Type type) throws IOException {
				out.startValue(SType.LIST);
				writeField(SType.LIST_ELEMENT_TYPE, type.getElementType());
				out.endValue();
				return null;
			}

			@Override
			public Void visitMap(Type type) throws IOException {
				out.startValue(SType.MAP);
				
				String keyLabel = type.getKeyLabel();
				String valLabel = type.getValueLabel();
			
				if(keyLabel != null && valLabel != null){
					out.writeField(SType.MAP_KEY_LABEL, keyLabel);
					out.writeField(SType.MAP_VAL_LABEL, valLabel);
				}
				writeField(SType.MAP_KEY_TYPE, type.getKeyType());
				writeField(SType.MAP_VAL_TYPE, type.getValueType());
				out.endValue();
				return null;
			}
			
			@Override
			public Void visitParameter(Type type) throws IOException {
				out.startValue(SType.PARAMETER);
				
				out.writeField(SType.PARAMETER_NAME, type.getName());
				writeField(SType.PARAMETER_BOUND, type.getBound());
				
				out.endValue();
				return null;
			}

			@Override
			public Void visitSet(Type type) throws IOException {
				out.startValue(SType.SET);
				
				writeField(SType.SET_ELEMENT_TYPE, type.getElementType());
				
				out.endValue();
				return null;
			}

			@Override
			public Void visitTuple(Type type) throws IOException {
				out.startValue(SType.TUPLE);
				
				String[] fieldNames = type.getFieldNames();
				if(fieldNames != null){
					writeNames(SType.TUPLE_NAMES, fieldNames);
				}
				
				int arity = type.getArity();
				out.writeField(SType.TUPLE_TYPES, arity);
				for(int i = 0; i < arity; i++){
					writeType(type.getFieldType(i));
				}
				
				out.endValue();
				return null;
			}
		});
	}
	
	boolean inWindow(IValue v) throws IOException{
		int id = valuesWindow.howLongAgo(v);
		if(id > -1){
			out.startValue(SValue.PREVIOUS);
			out.writeField(SValue.PREVIOUS_VALUE, id);
			out.endValue();
			return true;
		}
		return false;
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
				if(!inWindow(b)){
					out.startValue(SValue.BOOL);
					out.writeField(SValue.BOOL_VALUE, b.getValue() ? 1 : 0);
					out.endValue();
					valuesWindow.write(b);
				}
				break;
			}

			case CONSTRUCTOR: {
				IConstructor cons = (IConstructor) it.getIValue();
				if(it.atBeginning()){
					if(inWindow(cons)){
						it.skipChildren();
					}
				} else {
					out.startValue(SValue.CONSTRUCTOR);
					out.writeField(SValue.CONSTRUCTOR_ARITY, cons.arity());
					if(cons.mayHaveKeywordParameters()){
						if(cons.asWithKeywordParameters().hasParameters()){
							out.writeField(SValue.CONSTRUCTOR_KWPARAMS, cons.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(cons.asAnnotatable().hasAnnotations()){
							out.writeField(SValue.CONSTRUCTOR_ANNOS, cons.asAnnotatable().getAnnotations().size());
						}
					}
					writeField(SValue.CONSTRUCTOR_TYPE, cons.getUninstantiatedConstructorType());
					out.endValue();
					valuesWindow.write(cons);
				}
				break;
			}
			
			case DATETIME: {
				assert it.atBeginning();
				
				IDateTime dateTime = (IDateTime) it.getIValue();
				if(!inWindow(dateTime)){
						if(dateTime.isDateTime()){
						out.startValue(SValue.DATETIME);
						
						out.writeField(SValue.DATETIME_YEAR, dateTime.getYear());
						out.writeField(SValue.DATETIME_MONTH, dateTime.getMonthOfYear());
						out.writeField(SValue.DATETIME_DAY, dateTime.getDayOfMonth());
						
						out.writeField(SValue.DATETIME_HOUR, dateTime.getHourOfDay());
						out.writeField(SValue.DATETIME_MINUTE, dateTime.getMinuteOfHour());
						out.writeField(SValue.DATETIME_SECOND, dateTime.getSecondOfMinute());
						out.writeField(SValue.DATETIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						out.writeField(SValue.DATETIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						out.writeField(SValue.DATETIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
					} else if(dateTime.isDate()){
						out.startValue(SValue.DATE);
						
						out.writeField(SValue.DATE_YEAR, dateTime.getYear());
						out.writeField(SValue.DATE_MONTH, dateTime.getMonthOfYear());
						out.writeField(SValue.DATE_DAY, dateTime.getDayOfMonth());
					} else {
						out.startValue(SValue.TIME);
						
						out.writeField(SValue.TIME_HOUR, dateTime.getHourOfDay());
						out.writeField(SValue.TIME_MINUTE, dateTime.getMinuteOfHour());
						out.writeField(SValue.TIME_SECOND, dateTime.getSecondOfMinute());
						out.writeField(SValue.TIME_MILLISECOND, dateTime.getMillisecondsOfSecond());
						
						out.writeField(SValue.TIME_TZ_HOUR, dateTime.getTimezoneOffsetHours());
						out.writeField(SValue.TIME_TZ_MINUTE, dateTime.getTimezoneOffsetMinutes());
					}
					out.endValue();
					valuesWindow.write(dateTime);
				}
				break;
			}

			case INT: {
				assert it.atBeginning();
				IInteger ii = (IInteger) it.getIValue();
				if(!inWindow(ii)){
					if(ii.greaterEqual(minInt).getValue() && ii.lessEqual(maxInt).getValue()){
						int n = ii.intValue();
						out.startValue(SValue.INT);
						out.writeField(SValue.INT_VALUE, n);
						out.endValue();
						valuesWindow.write(ii);
					} else {
						out.startValue(SValue.BIGINT);
						byte[] valueData = ii.getTwosComplementRepresentation();
						out.writeField(SValue.BIGINT_VALUE, valueData);
						out.endValue();
						valuesWindow.write(ii);
					}
				}
				break;
			}
			
			case LIST: {
				IList lst = (IList) it.getIValue();
				if(it.atBeginning()){
					if(inWindow(lst)){
						it.skipChildren();
					}
				} else {
					out.startValue(SValue.LIST);
					out.writeField(SValue.LIST_SIZE, lst.length());
					out.endValue();
					valuesWindow.write(lst);
				}
				break;
			}
			
			case MAP: {
				IMap  map = (IMap) it.getIValue();
				if(it.atBeginning()){
					if(inWindow(map)){
						it.skipChildren();
					}
				} else {
					out.startValue(SValue.MAP);
					out.writeField(SValue.MAP_SIZE, map.size());
					out.endValue();
					valuesWindow.write(map);
				}
				break;
			}

			case NODE: {
				INode node = (INode) it.getIValue();
				if(it.atBeginning()){
					if(inWindow(node)){
						it.skipChildren();
					}
				} else {
					out.startValue(SValue.NODE);
					out.writeField(SValue.NODE_NAME,  node.getName());
					out.writeField(SValue.NODE_ARITY, node.arity());
					if(node.mayHaveKeywordParameters()){
						if(node.asWithKeywordParameters().hasParameters()){
							out.writeField(SValue.NODE_KWPARAMS, node.asWithKeywordParameters().getParameters().size());
						}
					} else {
						if(node.asAnnotatable().hasAnnotations()){
							out.writeField(SValue.NODE_ANNOS, node.asAnnotatable().getAnnotations().size());
						}
					}
					out.endValue();
					valuesWindow.write(node);
				}
				break;
			}
					
			case RATIONAL: {
				assert it.atBeginning();
				IRational rat = (IRational) it.getIValue();
				if(!inWindow(rat)){
					out.startValue(SValue.RAT);
					out.writeField(SValue.RAT_NUMERATOR, 0);
					writeValue(rat.numerator());
					out.writeField(SValue.RAT_DENOMINATOR, 0);
					writeValue(rat.denominator());
					out.endValue();
					valuesWindow.write(rat);;
				}
				break;
			}
				
			case REAL: {
				assert it.atBeginning();

				IReal real = (IReal) it.getIValue();
				if(!inWindow(real)){
					out.startValue(SValue.REAL);
					byte[] valueData = real.unscaled().getTwosComplementRepresentation();
					out.writeField(SValue.REAL_VALUE, valueData);
					out.writeField(SValue.REAL_SCALE, real.scale());
					out.endValue();
					valuesWindow.write(real);
				}
				break;
			}
			
			case SET: {
				ISet set = (ISet) it.getIValue();
				if(it.atBeginning()){
					if(inWindow(set)){
						it.skipChildren();
					}
				} else {
					out.startValue(SValue.SET);
					out.writeField(SValue.SET_SIZE, set.size());
					out.endValue();
					valuesWindow.write(set);
				}
				break;
			}

			case LOC: {
				assert it.atBeginning();
				
				ISourceLocation loc = (ISourceLocation) it.getIValue();
				if(!inWindow(loc)){
					out.startValue(SValue.LOC);
					URI uri = loc.getURI();
					String scheme = uri.getScheme();
					String authority = uri.getAuthority();
					String path = uri.getPath();
					String query = uri.getQuery();
					String fragment = uri.getFragment();
					if(scheme != null){
						out.writeField(SValue.LOC_SCHEME, scheme);
					}
					if(authority != null){
						out.writeField(SValue.LOC_AUTHORITY, authority);
					}
					if(path != null){
						out.writeField(SValue.LOC_PATH, path);
					}
					if(query != null){
						out.writeField(SValue.LOC_QUERY,  query);
					}
					if(fragment != null){
						out.writeField(SValue.LOC_FRAGMENT,  fragment);
					}
					
					if(loc.hasOffsetLength()){
						out.writeField(SValue.LOC_OFFSET, loc.getOffset());
						out.writeField(SValue.LOC_LENGTH, loc.getLength());
					} 
					if(loc.hasLineColumn()){
						out.writeField(SValue.LOC_BEGINLINE, loc.getBeginLine());
						out.writeField(SValue.LOC_ENDLINE, loc.getEndLine());
						out.writeField(SValue.LOC_BEGINCOLUMN, loc.getBeginColumn());
						out.writeField(SValue.LOC_ENDCOLUMN, loc.getEndColumn());
					}
					out.endValue();
					valuesWindow.write(loc);
				}
				break;
			}
				
			case STR: {
				assert it.atBeginning();
				
				IString str = (IString) it.getIValue();
				out.startValue(SValue.STR);
				out.writeField(SValue.STR_VALUE, str.getValue());
				out.endValue();
				// Already cached at wire level
				break;
			}

			case TUPLE: {
				ITuple tuple = (ITuple) it.getIValue();
				if(it.atBeginning()){
					if(inWindow(tuple)){
						it.skipChildren();
					}
				} else {
					out.startValue(SValue.TUPLE);
					out.writeField(SValue.TUPLE_SIZE, tuple.arity());
					out.endValue();
					valuesWindow.write(tuple);
				}
				break;
			}
			default:
				 throw new RuntimeException("writeValue: unexpected kind of value " + kind);
			}
		}
		out.startValue(SValue.END_OF_VALUE);
		out.endValue();
	}
	
  // Test code
    
    public static void main(String[] args) throws Exception {
    	TypeFactory tf = TypeFactory.getInstance();
    	IValueFactory vf = ValueFactoryFactory.getValueFactory();
    	TypeStore ts = RascalValueFactory.getStore();
    	 try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
    		 NewRVMIValueWriter ser = new NewRVMIValueWriter(out, 10, 10);
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
