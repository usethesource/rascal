package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public enum MuPrimitive {
	addition_mint_mint,
	and_mbool_mbool,
	assign_pair,
	assign_subscript_array_mint,
	check_arg_type,
	division_mint_mint,
	equal_mint_mint,
	equal,
	equal_set_mset,
	equivalent_mbool_mbool,
	get_children,
	get_children_and_keyword_params_as_values,
	get_name,
	get_name_and_children_and_keyword_params_as_map,
	get_tuple_elements,
	greater_equal_mint_mint,
	greater_mint_mint,
	implies_mbool_mbool,
	is_defined,
	is_element_mset,
	is_bool,
	is_constructor,
	is_datetime,
	is_int,
	is_list,
	is_lrel,
	is_loc,
	is_map,
	is_node,
	is_num,
	is_real,
	is_rat,
	is_rel,
	is_set,
	is_str,
	
	is_tuple,
	keys_map,
	values_map,
	less_equal_mint_mint,
	less_mint_mint,
	make_iarray,
	make_iarray_of_size,
	make_array,
	make_array_of_size,
	make_mset,
	make_map_str_entry,     // kwp
	make_map_str_ivalue,    // kwp
	make_entry_type_ivalue, // kwp
	map_contains_key,
	mint,
	modulo_mint_mint,
	mset,
	mset_empty(),
	mset2list,
	mset_destructive_add_elm,
	mset_destructive_add_mset,
	map_str_entry_add_entry_type_ivalue, // kwp
	map_str_ivalue_add_ivalue,           // kwp
	mset_destructive_subtract_mset,
	mset_destructive_subtract_set,
	mset_destructive_subtract_elm,
	not_equal_mint_mint,
	not_mbool,
	occurs_list_list_mint,
	or_mbool_mbool,
	power_mint_mint,
	rbool,
	rint,
	regexp_compile,
	regexp_find,
	regexp_group,
	set,
	set2list,
	set_is_subset_of_mset,
	size_array,
	size_list,
	size_set,
	size_mset,
	size_map,
	size_tuple,
	size,
	starts_with,
	sublist_list_mint_mint,
	subscript_array_mint,
	subscript_list_mint,
	subscript_tuple_mint,
	subtraction_mint_mint,
	subtype,
	typeOf,
	undefine,
	product_mint_mint,
	
	typeOf_constructor
	;
	
	private static IValueFactory vf;
	static Method [] methods;
	
	private static MuPrimitive[] values = MuPrimitive.values();
	private static HashSet<IValue> emptyMset = new HashSet<IValue>(0);
	
	private static boolean profiling = false;
	private static long timeSpent[] = new long[values.length];
	
	private static PrintWriter stdout;

	public static MuPrimitive fromInteger(int muprim){
		return values[muprim];
	}
	
	/**
	 * Initialize the primitive methods.
	 * @param fact value factory to be used
	 * @param stdout TODO
	 * @param doProfile TODO
	 * @param stdout 
	 */
	public static void init(IValueFactory fact, PrintWriter stdoutWriter, boolean doProfile) {
		vf = fact;
		stdout = stdoutWriter;
		profiling = doProfile;
		Method [] methods1 = MuPrimitive.class.getDeclaredMethods();
		HashSet<String> implemented = new HashSet<String>();
		methods = new Method[methods1.length];
		for(int i = 0; i < methods1.length; i++){
			Method m = methods1[i];
			String name = m.getName();
			if(!name.startsWith("$")){ // ignore all auxiliary functions that start with $.
				switch(name){
				case "init":
				case "exit":
				case "invoke":
				case "fromInteger":
				case "values":
				case "valueOf":
				case "main":
				case "printProfile":
					/* ignore all utility functions that do not implement some primitive */
					break;
				default:
					implemented.add(name);
					methods[valueOf(name).ordinal()] = m;
				}
			}
		}
		for(int i = 0; i < values.length; i++){
			if(!implemented.contains(values[i].toString())){
				throw new RuntimeException("PANIC: unimplemented primitive " + values[i] + " [add implementation to MuPrimitive.java]");
			}
		}
	}
	
	public static void exit(){
		if(profiling)
			printProfile();
	}
	private static void printProfile(){
		stdout.println("\nMuPrimitive execution times (ms)");
		long total = 0;
		TreeMap<Long,String> data = new TreeMap<Long,String>();
		for(int i = 0; i < values.length; i++){
			if(timeSpent[i] > 0 ){
				data.put(timeSpent[i], values[i].name());
				total += timeSpent[i];
			}
		}
		for(long t : data.descendingKeySet()){
			stdout.printf("%30s: %3d%% (%d ms)\n", data.get(t), t * 100 / total, t);
		}
	}
	
	/**
	 * Invoke the implementation of a muRascal primitive from the RVM main interpreter loop.
	 * @param stack	stack in the current execution frame
	 * @param sp	stack pointer
	 * @param arity number of arguments on the stack
	 * @return		new stack pointer and (implicitly) modified stack contents
	 */
	int invoke(Object[] stack, int sp, int arity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(!profiling){
			return (int) methods[ordinal()].invoke(null, stack,  sp, arity);
		} else {
			long start = System.currentTimeMillis();
			int res = (int) methods[ordinal()].invoke(null, stack,  sp, arity);
			timeSpent[ordinal()] += System.currentTimeMillis() - start;
			return res;
		}
	}
	
	/***************************************************************
	 * 				IMPLEMENTATION OF muRascal PRIMITIVES          *
	 ***************************************************************/
	
	public static int addition_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) + ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int subtraction_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) - ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int and_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 =  (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 =  (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = b1 && b2;
		return sp - 1;
	}
		
	public static int greater_equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) >= ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int greater_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) > ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int less_equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) <= ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int less_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) < ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int assign_pair(Object[] stack, int sp, int arity) {
		assert arity == 3;
		int v1 = ((Integer) stack[sp - 3]);
		int v2 = ((Integer) stack[sp - 2]);
		Object[] pair = (Object[]) stack[sp - 1];
		stack[v1] = pair[0];
		stack[v2] = pair[1];
		stack[sp - 3] = pair;
		return sp - 2;  // TODO:???
	}
		
	public static int assign_subscript_array_mint(Object[] stack, int sp, int arity) {
		assert arity == 3;
		Object[] ar = (Object[]) stack[sp - 3];
		Integer index = ((Integer) stack[sp - 2]);
		ar[index] = stack[sp - 1];
		stack[sp - 3] = stack[sp - 1];
		return sp - 2;
	}
		
	public static int check_arg_type(Object[] stack, int sp, int arity) {
		assert arity == 2;
		Type argType =  ((IValue) stack[sp - 2]).getType();
		Type paramType = ((Type) stack[sp - 1]);
		stack[sp - 2] = argType.isSubtypeOf(paramType);
		return sp - 1;
	}
		
	public static int division_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) / ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) == ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int equal(Object[] stack, int sp, int arity) {
		assert arity == 2;
		if(stack[sp - 2] instanceof IValue && (stack[sp - 2] instanceof IValue)){
			stack[sp - 2] = ((IValue) stack[sp - 2]).isEqual(((IValue) stack[sp - 1]));
		} else if(stack[sp - 2] instanceof Type && (stack[sp - 2] instanceof Type)){
			stack[sp - 2] = ((Type) stack[sp - 2]) == ((Type) stack[sp - 1]);
		} else 
			throw new RuntimeException("equal -- not defined on " + stack[sp - 2].getClass() + " and " + stack[sp - 2].getClass());
		return sp - 1;
	}
	
	public static int equivalent_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 = (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = (b1 == b2);
		return sp - 1;
	}
	
	public static int get_name(Object[] stack, int sp, int arity) {
		assert arity == 1;
		INode nd = (INode) stack[sp - 1];
		stack[sp - 1] = vf.string(nd.getName());
		return sp;
	}
	
	public static int get_children(Object[] stack, int sp, int arity) {
		assert arity == 1;
		INode nd = (INode) stack[sp - 1];
		Object[] elems = new Object[nd.arity()];
		for(int i = 0; i < nd.arity(); i++){
			elems[i] = nd.get(i);
		}
		stack[sp - 1] =  elems;
		return sp;
	}
	
	public static int get_children_and_keyword_params_as_values(Object[] stack, int sp, int arity) {
		assert arity == 1;
		INode nd = (INode) stack[sp - 1];
		int nd_arity = nd.arity();
		IValue last = nd.get(nd_arity - 1);
		Object[] elems;
		
		if(last.getType().isMap()){
			IMap kwmap = (IMap) last;
			int kw_arity = kwmap.size();
			elems = new Object[nd_arity - 1 + kw_arity];
			int j = nd_arity - 1;
			for(int i = 0; i < nd_arity - 1; i++){
				elems[i] = nd.get(i);
			}
			for(IValue v : kwmap){
				elems[j++] = v;
			}
		} else {
			elems = new Object[nd_arity];
			for(int i = 0; i < nd_arity; i++){
				elems[i] = nd.get(i);
			}
		}
		stack[sp - 1] =  elems;
		return sp;
	}
	
	/*
	 * Given a constructor or node get:
	 * - its name
	 * - positional arguments
	 * - keyword parameters collected in a map
	 */
		
	public static int get_name_and_children_and_keyword_params_as_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		IValue v = (IValue) stack[sp - 1];
		if(v.getType().isAbstractData()){
			IConstructor cons = (IConstructor) v;
			Type tp = cons.getConstructorType();
			
			int cons_arity = tp.getArity();
			int pos_arity = tp.getPositionalArity();
			IMapWriter writer = vf.mapWriter();
			for(int i = pos_arity; i < cons_arity; i++){
				String key = tp.getFieldName(i);
				IValue val = cons.get(key);
				writer.put(vf.string(key), val);
			}
			Object[] elems = new Object[pos_arity + 2];
			elems[0] = vf.string(cons.getName());
			for(int i = 0; i < pos_arity; i++){
				elems[i + 1] = cons.get(i);
			}
			elems[pos_arity + 1] = writer.done();
			stack[sp - 1] =  elems;
			return sp;
		}
		INode nd = (INode) v;
		String name = nd.getName();
		int nd_arity = nd.arity();
		IValue last = nd.get(nd_arity - 1);
		IMap map;
		Object[] elems;
		if(last.getType().isMap()){
			elems = new Object[nd_arity + 1];				// account for function name
			elems[0] = vf.string(name);
			for(int i = 0; i < nd_arity; i++){
				elems[i + 1] = nd.get(i);
			}
		} else {
			TypeFactory tf = TypeFactory.getInstance();
			map = vf.map(tf.voidType(), tf.voidType());
			elems = new Object[nd_arity + 2];				// account for function name and keyword map
		 
			elems[0] = vf.string(name);
			for(int i = 0; i < nd_arity; i++){
				elems[i + 1] = nd.get(i);
			}
			elems[1 + nd_arity] = map;
		}
		stack[sp - 1] = elems;
		return sp;
	}
		
	public static int get_tuple_elements(Object[] stack, int sp, int arity) {
		assert arity == 1;
		ITuple tup = (ITuple) stack[sp - 1];
		int nelem = tup.arity();
		Object[] elems = new Object[nelem];
		for(int i = 0; i < nelem; i++){
			elems[i] = tup.get(i);
		}
		stack[sp - 1] =  elems;
		return sp;
	}
		
	public static int implies_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 = (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = b1 ? b2 : true;
		return sp - 1;
	}
		
	public static int is_defined(Object[] stack, int sp, int arity) {
		assert arity == 1;
		Reference ref = (Reference) stack[sp - 1];
		stack[sp - 1] = ref.isDefined();
		return sp;
	}
	
	public static int undefine(Object[] stack, int sp, int arity) {
		assert arity == 1;
		Reference ref = (Reference) stack[sp - 1];
		stack[sp - 1] = ref.getValue();
		ref.undefine();
		return sp;
	}
		
//	public static int is_element(Object[] stack, int sp, int arity) {
//		assert arity == 2;
//		stack[sp - 2] = ((ISet) stack[sp - 1]).contains((ISet) stack[sp - 2]);
//		return sp - 1;
//	}
	
	@SuppressWarnings("unchecked")
	public static int is_element_mset(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((HashSet<IValue>) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}
		
	public static int is_bool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isBool();
		return sp;
	}
	
	public static int is_constructor(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isAbstractData();
		return sp;
	}
		
	public static int is_datetime(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isDateTime();
		return sp;
	}
		
	public static int is_int(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isInteger();
		return sp;
	}
		
	public static int is_list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isList();
		return sp;
	}
		
	public static int is_loc(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isSourceLocation();
		return sp;
	}
		
	public static int is_lrel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isListRelation();
		return sp;
	}
		
	public static int is_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isMap();
		return sp;
	}
		
	public static int is_node(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isNode();
		return sp;
	}
		
	public static int is_num(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isNumber();
		return sp;
	}
		
	public static int is_rat(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isRational();
		return sp;
	}
		
	public static int is_real(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isReal();
		return sp;
	}
		
	public static int is_rel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = ((IValue) stack[sp - 1]).getType().isRelation();
		return sp;
	}
		
	public static int is_set(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isSet();
		return sp;
	}
		
	public static int is_str(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isString();
		return sp;
	}
		
	public static int is_tuple(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType().isTuple();
		return sp;
	}
		
	public static int keys_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		IMap map = ((IMap) stack[sp - 1]);
		IListWriter writer = vf.listWriter();
		for(IValue key : map){
			writer.append(key);
		}
		stack[sp - 1] = writer.done();
		return sp;
	}
	
	public static int make_iarray(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		
		IValue[] ar = new IValue[arity];
		for(int i = arity - 1; i >= 0; i--) {
			ar[i] = (IValue) stack[sp - arity + i];
		}
		sp = sp - arity + 1;
		stack[sp - 1] = ar;
		return sp;
	}
	
	public static int make_iarray_of_size(Object[] stack, int sp, int arity) {
		assert arity == 1;
		int len = ((Integer) stack[sp - 1]);
		stack[sp - 1] = new IValue[len];
		return sp;
	}
		
	public static int make_array(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		
		Object[] ar = new Object[arity];

		for (int i = arity - 1; i >= 0; i--) {
			ar[i] = stack[sp - arity + i];
		}
		sp = sp - arity + 1;
		stack[sp - 1] = ar;
		return sp;
	}
			
	public static int make_array_of_size(Object[] stack, int sp, int arity) {
		assert arity == 1;
		int len = ((Integer)stack[sp - 1]);
		stack[sp - 1] = new Object[len];
		return sp;
	}
	
	public static int map_contains_key(Object[] stack, int sp, int arity) {
		assert arity == 2;
		IMap m = ((IMap)stack[sp - 2]);
		IString key = ((IString) stack[sp -1]);
		stack[sp - 2] = m.containsKey(key);
		return sp - 1;
	}
		
	public static int mint(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IInteger) stack[sp - 1]).intValue();
		return sp;
	}
		
	public static int modulo_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) % ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int not_equal_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) != ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int not_mbool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		boolean b1 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 1] = !b1;
		return sp;
	}
		
	public static int or_mbool_mbool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		boolean b1 = (stack[sp - 2] instanceof Boolean) ? ((Boolean) stack[sp - 2]) : ((IBool) stack[sp - 2]).getValue();
		boolean b2 = (stack[sp - 1] instanceof Boolean) ? ((Boolean) stack[sp - 1]) : ((IBool) stack[sp - 1]).getValue();
		stack[sp - 2] = b1 || b2;
		return sp - 1;
	}
		
	public static int power_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int n1 = ((Integer) stack[sp - 2]);
		int n2 = ((Integer) stack[sp - 1]);
		int pow = 1;
		for(int i = 0; i < n2; i++){
			pow *= n1;
		}
		stack[sp - 2] = pow;
		return sp - 1;
	}
		
	public static int rbool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp -1] = (stack[sp -1] instanceof Boolean) ? vf.bool((Boolean) stack[sp - 2]) : (IBool) stack[sp - 1];
		return sp;
	}
	
	/*
	 * Regular expressions
	 */
	
	public static int regexp_compile(Object[] stack, int sp, int arity) {
		assert arity == 2;
		String RegExpAsString = ((IString)stack[sp - 2]).getValue();
		String subject = ((IString)stack[sp - 1]).getValue();
		try {
			Pattern pat = Pattern.compile(RegExpAsString);
			stack[sp - 2] = pat.matcher(subject);
			return sp - 1;
		} catch (PatternSyntaxException e){
			throw new RuntimeException("Syntax error in Regexp: " + RegExpAsString);
		}
	}
	
	public static int regexp_find(Object[] stack, int sp, int arity) {
		assert arity == 1;
		Matcher matcher = (Matcher) stack[sp - 1];
		stack[sp - 1] = matcher.find();
		return sp;
	}
	
	public static int regexp_group(Object[] stack, int sp, int arity) {
		assert arity == 2;
		Matcher matcher = (Matcher) stack[sp - 2];
		int idx = (Integer) stack[sp - 1];
		stack[sp - 2] = vf.string(matcher.group(idx));
		return sp - 1;
	}
		
	/*
	 * rint -- convert muRascal int (mint) to Rascal int (rint)
	 * 
	 * 	
	 */
	public static int rint(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = vf.integer((Integer) stack[sp -1]);
		return sp;
	}
	
	public static int set2list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		ISet set = (ISet) stack[sp - 1];
		IListWriter writer = vf.listWriter();
		for(IValue elem : set){
			writer.append(elem);
		}
		stack[sp - 1] = writer.done();
		return sp;
	}
	
	@SuppressWarnings("unchecked")
	public static int mset2list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		HashSet<IValue> mset =(HashSet<IValue>) stack[sp - 1];
		IListWriter writer = vf.listWriter();
		for(IValue elem : mset){
			writer.append(elem);
		}
		stack[sp - 1] = writer.done();
		return sp;
	}
		
	public static int size_array(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((Object[]) stack[sp - 1]).length;
		return sp;
	}
	
	public static int size_list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IList) stack[sp - 1]).length();
		return sp;
	}
	
	public static int size_set(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((ISet) stack[sp - 1]).size();
		return sp;
	}
	
	public static int size_mset(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((HashSet<?>) stack[sp - 1]).size();
		return sp;
	}
	
	public static int size_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IMap) stack[sp - 1]).size();
		return sp;
	}
	
	public static int size_tuple(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((ITuple) stack[sp - 1]).arity();
		return sp;
	}
	
	public static int size(Object[] stack, int sp, int arity) {
		assert arity == 1;
		if(stack[sp - 1] instanceof IConstructor) {
			stack[sp - 1] = ((IConstructor) stack[sp - 1]).arity();
		} else if(stack[sp - 1] instanceof INode) {
			stack[sp - 1] = ((INode) stack[sp - 1]).arity();
		} else if(stack[sp - 1] instanceof IList) {
			stack[sp - 1] = ((IList) stack[sp - 1]).length();
		} else if(stack[sp - 1] instanceof ISet) {
			stack[sp - 1] = ((ISet) stack[sp - 1]).size();
		} else if(stack[sp - 1] instanceof IMap) {
			stack[sp - 1] = ((IMap) stack[sp - 1]).size();
		} else if(stack[sp - 1] instanceof ITuple) {
			stack[sp - 1] = ((ITuple) stack[sp - 1]).arity();
		}
		return sp;
	}
		
	public static int starts_with(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList sublist = (IList) stack[sp - 3];
		IList list = (IList) stack[sp - 2];
		int start = (Integer) stack[sp - 1];
		boolean eq = true;
		
		if(start + sublist.length() <= list.length()){
			for(int i = 0; i < sublist.length() && eq; i++){
				if(!sublist.get(i).equals(list.get(start + i))){
					eq = false;
				}
			}
		}
		stack[sp - 3] = eq;
		return sp - 2;
	}
		
	public static int sublist_list_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList lst = (IList) stack[sp - 3];
		int offset = ((Integer) stack[sp - 2]);
		int length = ((Integer) stack[sp - 1]);
		stack[sp - 3] = lst.sublist(offset, length);
		return sp - 2;
	}
		
	public static int subscript_array_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Object[]) stack[sp - 2])[((Integer) stack[sp - 1])];
		return sp - 1;
	}
	
	public static int subscript_list_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).get((Integer) stack[sp - 1]);
		return sp - 1;
	}
	
	public static int subscript_tuple_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ITuple) stack[sp - 2]).get((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int subtype(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int typeOf(Object[] stack, int sp, int arity) {
		assert arity == 1;
		if(stack[sp - 1] instanceof Integer) {
			stack[sp - 1] = TypeFactory.getInstance().integerType();
		} else {
			stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
		}
		return sp;
	}
	
	public static int typeOf_constructor(Object[] stack, int sp, int arity) {
		assert arity == 1;
		if(stack[sp - 1] instanceof Integer) {
			stack[sp - 1] = TypeFactory.getInstance().integerType();
		} else if(stack[sp - 1] instanceof IConstructor) {
			stack[sp - 1] = ((IConstructor) stack[sp - 1]).getConstructorType();
		} else {
			stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
		}
		return sp;
	}
	
	public static int product_mint_mint(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((Integer) stack[sp - 2]) * ((Integer) stack[sp - 1]);
		return sp - 1;
	}
		
	public static int values_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		IMap map = ((IMap) stack[sp - 1]);
		IListWriter writer = vf.listWriter();
		for(IValue key : map){
			writer.append(map.get(key));
		}
		stack[sp - 1] = writer.done();
		return sp;
	}
	
	public static int occurs_list_list_mint(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList sublist =  ((IList) stack[sp - 3]);
		int nsub = sublist.length();
		IList list =  ((IList) stack[sp - 2]);
		Integer start = (Integer) stack[sp - 1];
		int nlist = list.length();
		stack[sp - 3] = false;
		int newsp = sp - 2;
		if(start + nsub <= nlist){
			for(int i = 0; i < nsub; i++){
				if(!sublist.get(i).isEqual(list.get(start + i)))
					return newsp;
			}
		} else {
			return newsp;
		}
		
		stack[sp - 3] = true;
		return newsp;
	}
	
	public static int mset(Object[] stack, int sp, int arity) {
		assert arity == 1;
		ISet set =  ((ISet) stack[sp - 1]);
		int n = set.size();
		HashSet<IValue> mset = n > 0 ? new HashSet<IValue>(set.size()) : emptyMset;
		for(IValue v : set){
			mset.add(v);
		}
		stack[sp - 1] = mset;
		return sp;
	}
	
	public static int mset_empty(Object[] stack, int sp, int arity) {
		assert arity == 0;
		stack[sp] = emptyMset;
		return sp + 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int mset_destructive_subtract_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		mset = (HashSet<IValue>) mset.clone();
		ISet set =  ((ISet) stack[sp - 1]);
		for(IValue v : set){
			mset.remove(v);
		}
		stack[sp - 2] = mset;
		return sp - 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int set_is_subset_of_mset(Object[] stack, int sp, int arity) {
		assert arity == 2;
		ISet subset = ((ISet) stack[sp - 2]);
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
		for(IValue v : subset){
			if(!mset.contains(v)){
				stack[sp - 2] = false;
				return sp - 1;
			}
		}
		stack[sp - 2] = true;
		return sp - 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int mset_destructive_subtract_mset(Object[] stack, int sp, int arity) {
		assert arity == 2;
		HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
		//lhs =  (HashSet<IValue>) lhs.clone();
		HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];
	
		lhs.removeAll(rhs);
		stack[sp - 2] = lhs;
		return sp - 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int mset_destructive_add_mset(Object[] stack, int sp, int arity) {
		assert arity == 2;
		HashSet<IValue> lhs = (HashSet<IValue>) stack[sp - 2];
		//lhs =  (HashSet<IValue>) lhs.clone();
		HashSet<IValue> rhs = (HashSet<IValue>) stack[sp - 1];
		lhs.addAll(rhs);
		stack[sp - 2] = lhs;
		return sp - 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int mset_destructive_subtract_elm(Object[] stack, int sp, int arity) {
		assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		//mset =  (HashSet<IValue>)  mset.clone();
		IValue elm =  ((IValue) stack[sp - 1]);
		mset.remove(elm);
		stack[sp - 2] = mset;
		return sp - 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int mset_destructive_add_elm(Object[] stack, int sp, int arity) {
		assert arity == 2;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 2];
		IValue elm =  ((IValue) stack[sp - 1]);
		mset.add(elm);
		stack[sp - 2] = mset;
		return sp - 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int set(Object[] stack, int sp, int arity) {
		assert arity == 1;
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
		ISetWriter w = vf.setWriter();
		for(IValue v : mset){
			w.insert(v);
		}
		stack[sp - 1] = w.done();
		return sp;
	}
	
	public static int make_mset(Object[] stack, int sp, int arity) {
		assert arity == 0;
		HashSet<IValue> mset = new HashSet<IValue>();
		stack[sp] = mset;
		return sp + 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int equal_set_mset(Object[] stack, int sp, int arity) {
		assert arity == 2;
		ISet set = (ISet) stack[sp - 2];
		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
		stack[sp - 2] = false;
		if(set.size() != mset.size()){
			return sp - 1;
		}
		for(IValue v : set){
			if(!mset.contains(v)){
				return sp - 1;
			}
		}
		stack[sp - 2] = true;
		return sp - 1;
	}
	
//	@SuppressWarnings("unchecked")
//	public static int mset_copy(Object[] stack, int sp, int arity) {
//		assert arity == 1;
//		HashSet<IValue> mset = (HashSet<IValue>) stack[sp - 1];
//		stack[sp - 1] = mset.clone();
//		return sp;
//	}
	
	public static int make_map_str_entry(Object[] stack, int sp, int arity) {
		assert arity == 0;
		stack[sp] = new HashMap<String, Map.Entry<Type,IValue>>();
		return sp + 1;
	}
	
	public static int make_map_str_ivalue(Object[] stack, int sp, int arity) {
		assert arity == 0;
		stack[sp] = new HashMap<String, IValue>();
		return sp + 1;
	}
	
	public static int make_entry_type_ivalue(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = new AbstractMap.SimpleEntry<Type,IValue>((Type) stack[sp - 2], (IValue) stack[sp - 1]);
		return sp - 1;
	}
	
	@SuppressWarnings("unchecked")
	public static int map_str_entry_add_entry_type_ivalue(Object[] stack, int sp, int arity) {
		assert arity == 3;
		stack[sp - 3] = ((Map<String, Map.Entry<Type,IValue>>) stack[sp - 3]).put(((IString) stack[sp - 2]).getValue(), (Map.Entry<Type, IValue>) stack[sp - 1]);
		return sp - 2;
	}
	
	@SuppressWarnings("unchecked")
	public static int map_str_ivalue_add_ivalue(Object[] stack, int sp, int arity) {
		assert arity == 3;
		stack[sp - 3] = ((Map<String, IValue>) stack[sp - 3]).put(((IString) stack[sp - 2]).getValue(), (IValue) stack[sp - 1]);
		return sp - 2;
	}
			
	/*
	 * Run this class as a Java program to compare the list of enumeration constants with the implemented methods in this class.
	 */

	public static void main(String[] args) {
		init(ValueFactoryFactory.getValueFactory(), null, false);
		System.err.println("MuPrimitives have been validated!");
	}
}
