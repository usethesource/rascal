package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelationalAlgebra;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.cobra.TypeParameterVisitor;
import org.rascalmpl.library.experiments.Compiler.Rascal2muRascal.RandomValueTypeVisitor;
import org.rascalmpl.values.ValueFactoryFactory;

/*
 * The primitives that can be called via the CALLPRIM instruction.
 * Each primitive with name P (e.g. addition_int_int) is defined by:
 * - a constant P below
 * - a static method int P(Object[] stack, int sp)
 * 
 * Each primitive implementation gets the current stack and stack pointer as argument
 * and returns a new stack pointer. It may make modifications to the stack.
 */

public enum RascalPrimitive {
	appendAfter,

	elm_add_list,
	list_add_elm,
	list_add_list,
	map_add_map,
	tuple_add_tuple,

	int_add_int,
	int_add_num,
	int_add_rat,
	int_add_real,
	
	int_mod_int,

	num_add_int,
	num_add_num,
	num_add_real,
	num_add_rat,

	rat_add_int,
	rat_add_num,
	rat_add_rat,
	rat_add_real,

	real_add_num,
	real_add_int,
	real_add_real,
	real_add_rat,

	elm_add_set,
	set_add_elm,
	set_add_set,
	str_add_str,
	
	str_addindented_str,
	str_remove_margins,
	
	adt_field_access,
	adt_field_update,
	adt_subscript,
	adt_update,
	
	annotation_get,
	annotation_set,

	lrel_compose_lrel,
	rel_compose_rel,
	map_compose_map,
	
	datetime_field_access,
	
	int_divide_int,
	int_divide_num,
	int_divide_rat,
	int_divide_real,

	num_divide_int,
	num_divide_num,
	num_divide_real,
	num_divide_rat,

	rat_divide_int,
	rat_divide_num,
	rat_divide_rat,
	rat_divide_real,

	real_divide_num,
	real_divide_int,
	real_divide_real,
	real_divide_rat,
	
	equal,
	
	equivalent_bool_bool,

	int_greater_int,
	int_greater_num,
	int_greater_rat,
	int_greater_real,

	num_greater_int,
	num_greater_num,
	num_greater_real,
	num_greater_rat,

	rat_greater_int,
	rat_greater_num,
	rat_greater_rat,
	rat_greater_real,

	real_greater_num,
	real_greater_int,
	real_greater_real,
	real_greater_rat,

	str_greater_str,

	int_greaterequal_int,
	int_greaterequal_num,
	int_greaterequal_rat,
	int_greaterequal_real,

	num_greaterequal_int,
	num_greaterequal_num,
	num_greaterequal_real,
	num_greaterequal_rat,

	rat_greaterequal_int,
	rat_greaterequal_num,
	rat_greaterequal_rat,
	rat_greaterequal_real,

	real_greaterequal_num,
	real_greaterequal_int,
	real_greaterequal_real,
	real_greaterequal_rat,

	str_greaterequal_str,

	has,
	implies_bool_bool,
	set_intersect_set,
	list_intersect_list,
	map_intersect_map,
	elm_in_list,
	elm_in_set,
	elm_in_map,
	
	is,
	
	is_bool,
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

	int_less_int,
	int_less_num,
	int_less_rat,
	int_less_real,

	num_less_int,
	num_less_num,
	num_less_real,
	num_less_rat,

	rat_less_int,
	rat_less_num,
	rat_less_rat,
	rat_less_real,

	real_less_num,
	real_less_int,
	real_less_real,
	real_less_rat,

	str_less_str,

	int_lessequal_int,
	int_lessequal_num,
	int_lessequal_rat,
	int_lessequal_real,

	num_lessequal_int,
	num_lessequal_num,
	num_lessequal_real,
	num_lessequal_rat,

	rat_lessequal_int,
	rat_lessequal_num,
	rat_lessequal_rat,
	rat_lessequal_real,

	real_lessequal_num,
	real_lessequal_int,
	real_lessequal_real,
	real_lessequal_rat,

	str_lessequal_str,
	
	list_size,
	list_create,
	list_replace,
	list_slice,
	list_subscript, 
	list_update,
	
	listwriter_add,
	listwriter_close,
	listwriter_open,
	listwriter_splice,
	
	loc_field_access,
	
	map_create,
	map_subscript,
	map_update,
	
	mapwriter_add,
	mapwriter_close,
	mapwriter_open,
	
	negative_int,
	negative_real,
	negative_rat,
	negative_num,
	
	node_create,
	node_replace,
	node_slice,
	
	not_bool,
	notequal,
	elm_notin_list,
	elm_notin_set,
	elm_notin_map,
	or_bool_bool,
	println,

	int_product_int,
	int_product_num,
	int_product_rat,
	int_product_real,

	num_product_int,
	num_product_num,
	num_product_real,
	num_product_rat,

	rat_product_int,
	rat_product_num,
	rat_product_rat,
	rat_product_real,

	real_product_num,
	real_product_int,
	real_product_real,
	real_product_rat,

	int_remainder_int,
	
	set_create,
	
	setwriter_add,
	setwriter_close,
	setwriter_open,
	setwriter_splice,
	
	str_replace,
	str_slice,
	
	stringwriter_open,
	stringwriter_add,
	stringwriter_close,

	sublist,

	list_subtract_list,
	map_subtract_map,
	set_subtract_set,

	int_subtract_int,
	int_subtract_num,
	int_subtract_rat,
	int_subtract_real,

	num_subtract_int,
	num_subtract_num,
	num_subtract_real,
	num_subtract_rat,

	rat_subtract_int,
	rat_subtract_num,
	rat_subtract_rat,
	rat_subtract_real,

	real_subtract_num,
	real_subtract_int,
	real_subtract_real,
	real_subtract_rat,
	
	assertreport,
	testreport_add,
	testreport_close,
	testreport_open,
	
	lrel_transitive_closure,
	rel_transitive_closure,
	lrel_transitive_reflexive_closure,
	rel_transitive_reflexive_closure,
	
	template_open,
	template_add,
	template_close,
	
	tuple_field_access,
	tuple_field_project,
	tuple_create,
	tuple_subscript,
	tuple_update,

	type_equal_type,
	subtype,
	typeOf,
	value_to_string
	;

	private static RascalPrimitive[] values = RascalPrimitive.values();

	public static RascalPrimitive fromInteger(int prim){
		return values[prim];
	}

	private static IValueFactory vf;
	private static TypeFactory tf;
	private static IBool TRUE;
	private static IBool FALSE;
	static Method [] methods;
	private static Type lineColumnType;
	
	private static PrintWriter stdout;
	private static RVM rvm;

	/**
	 * Initialize the primitive methods.
	 * @param fact value factory to be used
	 * @param stdout 
	 */
	public static void init(IValueFactory fact, PrintWriter stdoutPrinter, RVM usedRVM) {
		vf = fact;
		stdout = stdoutPrinter;
		rvm = usedRVM;
		tf = TypeFactory.getInstance();
		lineColumnType = tf.tupleType(new Type[] {tf.integerType(), tf.integerType()},
									new String[] {"line", "column"});
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		Method [] methods1 = RascalPrimitive.class.getDeclaredMethods();
		HashSet<String> implemented = new HashSet<String>();
		methods = new Method[methods1.length];
		for(int i = 0; i < methods1.length; i++){
			Method m = methods1[i];
			String name = m.getName();
			if(!name.startsWith("$")){ // ignore all auxiliary functions that start with $.
				switch(name){
				case "init":
				case "invoke":
				case "fromInteger":
				case "values":
				case "valueOf":
				case "main":
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
				throw new RuntimeException("PANIC: unimplemented primitive " + values[i] + " [add implementation to Primitives]");
			}
		}
	}

	/**
	 * Invoke the implementation of a primitive from the RVM main interpreter loop.
	 * @param stack	stack in the current execution frame
	 * @param sp	stack pointer
	 * @param arity TODO
	 * @return		new stack pointer and modified stack contents
	 */
	int invoke(Object[] stack, int sp, int arity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return (int) methods[ordinal()].invoke(null, stack,  sp, arity);
	}

	/***************************************************************
	 * 				IMPLEMENTATION OF PRIMITIVES                   *
	 ***************************************************************/
	
	/*
	 * assertreport
	 */
	public static int assertreport(Object[] stack, int sp, int arity) {
		assert arity == 3;
		boolean succeeded = (stack[sp - 3] instanceof Boolean) ? (Boolean) stack[sp - 3] : ((IBool) stack[sp - 3]).getValue();
		String message = ((IString) stack[sp - 2]).getValue();
		message = message.isEmpty() ? "" : ": " + message;
		ISourceLocation src = ((ISourceLocation) stack[sp - 1]);
		if(!succeeded){
			stdout.println("Assertion failed" + message + " at " + src);
		}
		return sp - 2;
	}
	
	/*
	 * add_to_...writer
	 */
	public static int listwriter_add(Object[] stack, int sp, int arity) {
		assert arity > 0;
		IListWriter writer = (IListWriter) stack[sp - arity];
		for(int i = arity - 1; i > 0; i--){
			writer.append((IValue) stack[sp - i]);
		}
		return sp - arity + 1;
	}

	public static int setwriter_add(Object[] stack, int sp, int arity) {
		assert arity > 0;
		ISetWriter writer = (ISetWriter) stack[sp - arity];
		for(int i = arity - 1; i > 0; i--){
			writer.insert((IValue) stack[sp - i]);
		}
		return sp - arity + 1;
	}
	
	public static int mapwriter_add(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IMapWriter writer = (IMapWriter) stack[sp - 3];
		writer.insert(vf.tuple((IValue) stack[sp - 2], (IValue) stack[sp - 1]));
		return sp - 2;
	}

	/*
	 * addition
	 *
	 * infix Addition "+"
	 * {  
	 *		&L <: num x &R <: num               -> LUB(&L, &R),

	 *		list[&L] x list[&R]                 -> list[LUB(&L,&R)],
	 *		list[&L] x &R              		  -> list[LUB(&L,&R)] when &R is not a list,	  
	 *		&L x list[&R <: &L]                 -> list[LUB(&L,&R)] when &L is not a list,

	 *		set[&L] x set[&R]                   -> set[LUB(&L,&R)],
	 *		set[&L] x &R                        -> set[LUB(&L,&R)] when &R is not a list,
	 *		&L x set[&R]                        -> set[LUB(&L,&R)] when &L is not a list,

	 *		map[&K1,&V1] x map[&K2,&V2]         -> map[LUB(&K1,&K2), LUB(&V1,&V2)],

	 *		str x str                           -> str,
	 *		loc x str                           -> loc,
	 *		tuple[&L1,&L2] x tuple[&R1,&R2,&R3] -> tuple[&L1,&L2,&R1,&R2,&R3]
	 * }
	 */

	// int
	public static int int_add_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_add_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_add_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_add_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int num_add_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_add_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_add_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_add_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int rat_add_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_add_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_add_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_add_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int real_add_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_add_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_add_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_add_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}


	public static int list_add_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).concat((IList) stack[sp - 1]);
		return sp - 1;
	}

	public static int map_add_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).join((IMap) stack[sp - 1]);
		return sp - 1;
	}

	public static int list_add_elm(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).append((IValue) stack[sp - 1]);
		return sp - 1;
	}

	public static int elm_add_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 1]).insert((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int set_add_elm(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).insert((IValue) stack[sp - 1]);
		return sp - 1;
	}

	public static int elm_add_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 1]).insert((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int set_add_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).union((ISet) stack[sp - 1]);
		return sp - 1;
	}

	public static int str_add_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		
		stack[sp - 2] = ((IString) stack[sp - 2]).concat((IString) stack[sp - 1]);
		return sp - 1;
	}
	
	/*
	 * String templates
	 */
	
	private static final Pattern MARGIN = Pattern.compile("^[ \t]*'", Pattern.MULTILINE);
	private static final Pattern INDENT = Pattern.compile("(?<![\\\\])'([ \t]*)([^']*)$");
	private static final Pattern NONSPACE = Pattern.compile("[^ \t]");
	
	
	private static Stack<String> indentStack = new Stack<String>();
	
	private static void $indent(String s){
		indentStack.push(s);
	}
	
	public static String $getCurrentIndent() {
		return indentStack.peek();
	}
	
	private static void $unindent(){
		indentStack.pop();
	}
	
	public static int template_open(Object[] stack, int sp, int arity) {
		assert arity == 0;
		
		$indent(indentStack.size() == 0 ? "" : $computeIndent($getCurrentIndent()));
		stack[sp] = vf.string("");
		return sp + 1;
	}
	
	public static int template_close(Object[] stack, int sp, int arity) {
		assert arity == 1;
		$unindent();
		return sp;
	}
	
	private static String $computeIndent(String arg) {
		Matcher m = INDENT.matcher(arg);
		if (m.find()) {
			return m.group(1) + $replaceEverythingBySpace(m.group(2));
		}
		return "";
	}
	
	private static String $replaceEverythingBySpace(String input) {
		return NONSPACE.matcher(input).replaceAll(" ");
	}
	
	public static int str_remove_margins(Object[] stack, int sp, int arity) {
		assert arity == 1;
		String arg = ((IString) stack[sp - 1]).getValue();
		arg = MARGIN.matcher(arg).replaceAll("");
		stack[sp - 1] = vf.string(org.rascalmpl.interpreter.utils.StringUtils.unescapeSingleQuoteAndBackslash(arg));
		return sp;
	}
	
	private static String $removeMargins(String arg) {
		arg = MARGIN.matcher(arg).replaceAll("");
		return org.rascalmpl.interpreter.utils.StringUtils.unescapeSingleQuoteAndBackslash(arg);
	}
	
	private static IString $processString(IString s){
		return vf.string($removeMargins(s.getValue()));
	}
	
	public static int template_add(Object[] stack, int sp, int arity) {
		assert arity >= 2;
		IString template = (IString) stack[sp - arity];
		for(int i = 1; i < arity; i++){
			template = template.concat($processString((IString) stack[sp - arity + i]));
		}
		stack[sp - arity] = template;
		return sp - arity + 1;
	}
	
	public static int str_addindented_str(Object[] stack, int sp, int arity) {
		assert arity >= 2;
		IString template = (IString) stack[sp - arity];
		for(int i = 1; i < arity; i++){
			template = template.concat($processString((IString) stack[sp - arity + i]));
		}
		stack[sp - arity] = template;
		return sp - arity + 1;
	}

	//	public static int addition_loc_str(Object[] stack, int sp) { 	}

	public static int tuple_add_tuple(Object[] stack, int sp, int arity) {
		assert arity == 2;
		ITuple t1 = (ITuple) stack[sp - 2];
		ITuple t2 = (ITuple) stack[sp - 1];
		int len1 = t1.arity();
		int len2 = t2.arity();
		IValue elems[] = new IValue[len1 + len2];
		for(int i = 0; i < len1; i++)
			elems[i] = t1.get(i);
		for(int i = 0; i < len2; i++)
			elems[len1 + i] = t2.get(i);
		stack[sp - 2] = vf.tuple(elems);
		return sp - 1;
	}

	/*
	 * appendAfter
	 */
	public static int appendAfter(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).append((IValue) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * asType
	 */

	/*
	 * composition
	 * infix Composition "o" {
     	lrel[&A,&B] x lrel[&B,&C] -> lrel[&A,&C],
     	rel[&A,&B] x rel[&B,&C] -> rel[&A,&C],
     	map[&A,&B] x map[&B,&C] -> map[&A,&C]
		}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int lrel_compose_lrel(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IListRelation) stack[sp - 2]).compose((IListRelation) stack[sp - 1]);
		return sp - 1;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int rel_compose_rel(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRelationalAlgebra) stack[sp - 2]).compose((IRelationalAlgebra) stack[sp - 1]);
		return sp - 1;
	}

	public static int map_compose_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).compose((IMap) stack[sp - 1]);
		return sp - 1;
	}
	
	public static int int_mod_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).mod((IInteger) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * division
	 * 
	 * infix Division "/" { &L <: num x &R <: num        -> LUB(&L, &R) }
	 */

	// int
	public static int int_divide_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_divide_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int int_divide_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_divide_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	// num
	public static int num_divide_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IInteger) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int num_divide_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int num_divide_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IRational) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int num_divide_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	// rat
	public static int rat_divide_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_divide_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int rat_divide_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_divide_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	// real
	public static int real_divide_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int real_divide_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IInteger) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int real_divide_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int real_divide_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IRational) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}



	/*
	 * ...writer_close
	 */

	public static int listwriter_close(Object[] stack, int sp, int arity) {
		assert arity == 0;
		IListWriter writer = (IListWriter) stack[sp - 1];
		stack[sp - 1] = writer.done();
		return sp;
	}

	public static int setwriter_close(Object[] stack, int sp, int arity) {
		assert arity == 0;
		ISetWriter writer = (ISetWriter) stack[sp - 1];
		stack[sp - 1] = writer.done();
		return sp;
	}
	
	public static int mapwriter_close(Object[] stack, int sp, int arity) {
		assert arity == 0;
		IMapWriter writer = (IMapWriter) stack[sp - 1];
		stack[sp - 1] = writer.done();
		return sp;
	}

	/*
	 * equal
	 */

	public static int equal(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IValue) stack[sp - 2]).isEqual((IValue) stack[sp - 1]);
		return sp - 1;
	}
	
	public static int type_equal_type(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = vf.bool(((Type) stack[sp - 2]) == ((Type) stack[sp - 1]));
		return sp - 1;
	}

	/*
	 * equivalent
	 */

	public static int equivalent_bool_bool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).equivalent((IBool) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * field_access_...
	 */
	public static int adt_field_access(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IConstructor) stack[sp - 2]).get(((IString) stack[sp - 1]).getValue());
		return sp - 1;
	}
	
	public static int datetime_field_access(Object[] stack, int sp, int arity) {
		assert arity == 2;
		IDateTime dt = ((IDateTime) stack[sp - 2]);
		String field = ((IString) stack[sp - 1]).getValue();
		IValue v;
		switch (field) {
		case "isDate":
			v = vf.bool(dt.isDate());
			break;
		case "isTime":
			v = vf.bool(dt.isTime());
			break;
		case "isDateTime":
			v = vf.bool(dt.isDateTime());
			break;
		case "century":
			v = vf.integer(dt.getCentury());
			break;
		case "year":
			v = vf.integer(dt.getYear());
			break;
		case "month":
			v = vf.integer(dt.getMonthOfYear());
			break;
		case "day":
			v = vf.integer(dt.getDayOfMonth());
			break;
		case "hour":
			v = vf.integer(dt.getHourOfDay());
			break;
		case "minute":
			v = vf.integer(dt.getMinuteOfHour());
			break;
		case "second":
			v = vf.integer(dt.getSecondOfMinute());
			break;
		case "millisecond":
			v = vf.integer(dt.getMillisecondsOfSecond());
			break;
		case "timezoneOffsetHours":
			v = vf.integer(dt.getTimezoneOffsetHours());
			break;
		case "timezoneOffsetMinutes":
			v = vf.integer(dt.getTimezoneOffsetMinutes());
			break;
		default:
			throw new RuntimeException("Access to non-existing field " + field + " in datetime");
		}
		stack[sp - 2] = v;
		return sp - 1;
	}
	
	public static int loc_field_access(Object[] stack, int sp, int arity) {
		assert arity == 2;
		ISourceLocation sloc = ((ISourceLocation) stack[sp - 2]);
		String field = ((IString) stack[sp - 1]).getValue();
		IValue v;
		switch (field) {
		case "uri":
			v = vf.string(sloc.getURI().toString());
			break;
		case "scheme":
			String s = sloc.getURI().getScheme();
			v = vf.string(s == null ? "" : s);
			break;
		case "authority":
			s = sloc.getURI().getAuthority();
			v = vf.string(s == null ? "" : s);
			break;
		case "host":
			s = sloc.getURI().getHost();
			v = vf.string(s == null ? "" : s);
			break;
		case "port":
			int n = sloc.getURI().getPort();
			v = vf.string(Integer.toString(n));
			break;
		case "path":
			s = sloc.getURI().getPath();
			v = vf.string(s == null ? "" : s);
			break;
		case "extension":
			String path = sloc.getURI().getPath();
			int i = path.lastIndexOf('.');
			if (i != -1) {
				v = vf.string(path.substring(i + 1));
			} else {
				v = vf.string("");
			}
			break;
		case "query":
			s = sloc.getURI().getQuery();
			v = vf.string(s == null ? "" : s);
			break;
		case "fragment":
			s= sloc.getURI().getFragment();
			v = vf.string(s == null ? "" : s);
			break;
		case "user":
			s = sloc.getURI().getUserInfo();
			v = vf.string(s == null ? "" : s);
			break;
		case "parent":
			path = sloc.getURI().getPath();
			if (path.equals("")) {
				throw RuntimeExceptionFactory.noParent(sloc, null, null);
			}
			i = path.lastIndexOf("/");
			
			if (i != -1) {
				path = path.substring(0, i);
				v = vf.string(path);
			} else {
				throw RuntimeExceptionFactory.noParent(sloc, null, null);
			}
			break;	
		case "file": 
			path = sloc.getURI().getPath();
			
			if (path.equals("")) {
				throw RuntimeExceptionFactory.noParent(sloc,null,null);
			}
			i = path.lastIndexOf("/");
			
			if (i != -1) {
				path = path.substring(i+1);
			}
			v = vf.string(path);			
			
		case "ls":
//			try {
//				IListWriter w = vf.listWriter();
//				Type stringType = tf.stringType();
//				URI uri = sloc.getURI();
//				for (String elem : ctx.getResolverRegistry().listEntries(uri)) {
//					w.append(vf.string(elem));
//				}
//				
//				v = w.done();
//				
//			} catch (IOException e) {
//				throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null,null);
//			}
			v = null;
			break;
		case "offset":
			v = vf.string(sloc.getOffset());
			break;
		case "length":
			v = vf.string(sloc.getLength());
			break;
		case "begin":
			v = vf.tuple(lineColumnType, vf.integer(sloc.getBeginLine()), vf.integer(sloc.getBeginColumn()));
			break;
		case "end":
			v = vf.tuple(lineColumnType, vf.integer(sloc.getEndLine()), vf.integer(sloc.getEndColumn()));
			break;

		default:
			throw new RuntimeException("Access to non-existing field " + field + " in location");
		}
		stack[sp - 2] = v;
		return sp - 1;
	}
	
	public static int annotation_get(Object[] stack, int sp, int arity) {
		assert arity == 2;
		IValue val = (IValue) stack[sp - 2];
		String label = ((IString) stack[sp - 1]).getValue();
		stack[sp - 2] = val.asAnnotatable().getAnnotation(label);
		return sp - 1;
	}
	
	public static int annotation_set(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IValue val = (IValue) stack[sp - 3];
		String label = ((IString) stack[sp - 2]).getValue();
		IValue repl = (IValue) stack[sp - 1];
		stack[sp - 2] = val.asAnnotatable().setAnnotation(label, repl);
		return sp - 1;
	}
	
	
	public static int tuple_field_access(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ITuple) stack[sp - 2]).get(((IString) stack[sp - 1]).getValue());
		return sp - 1;
	}
	
	public static int tuple_field_project(Object[] stack, int sp, int arity) {
		assert arity >= 2;
		ITuple tup = (ITuple) stack[sp - arity];
		IValue [] newFields = new IValue[arity - 1];
		for(int i = 0; i < arity - 1; i++){
			IValue field = (IValue) stack[sp - arity + 1 + i];
			newFields[i] = field.getType().isInteger() ? tup.get(((IInteger) field).intValue())
												       : tup.get(((IString) field).getValue());
		}
		stack[sp - arity] = vf.tuple(newFields);
		return sp - arity + 1;
	}
	
	/*
	 * fieldUpdate
	 */
	public static int adt_field_update(Object[] stack, int sp, int arity) {
		assert arity == 3;
		stack[sp - 3] = ((IConstructor) stack[sp - 3]).set(((IString) stack[sp - 2]).getValue(),
							(IValue) stack[sp -1]);
		return sp - 2;
	}
	
	/*
	 * fieldProject
	 */
	/*
	 * getAnnotation
	 */

	/*
	 * greaterthan
	 */


	// int
	public static int int_greater_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_greater_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_greater_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_greater_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int num_greater_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_greater_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_greater_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_greater_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int rat_greater_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_greater_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_greater_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_greater_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int real_greater_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_greater_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_greater_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_greater_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}

	// greater on other types
	public static int str_greater_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]) == 1;
		return sp - 1;
	}

	/*
	 * greaterThanOrEq
	 */

	// int
	public static int int_greaterequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_greaterequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_greaterequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_greaterequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int num_greaterequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_greaterequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_greaterequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_greaterequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int rat_greaterequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_greaterequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_greaterequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_greaterequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int real_greaterequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_greaterequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_greaterequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_greaterequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}

	// greaterequal on other types
	public static int str_greaterequal_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
		stack[sp - 2] = c == 0 || c == 1;
		return sp - 1;
	}
	
	/*
	 * has
	 */
	
	public static int has(Object[] stack, int sp, int arity) {
		assert arity == 2;
		IValue val = ((IValue) stack[sp - 2]);
		String fieldName = ((IString) stack[sp - 1]).getValue();
		stdout.println("type = " + val.getType());
		if(val.getType().isAbstractData()){
			stack[sp - 2] = ((IConstructor)val).getConstructorType().hasField(fieldName);
		} else {
			stack[sp - 2] = val.getType().hasField(fieldName);
		}
		return sp - 1;
	}

	/*
	 * implies
	 */

	public static int implies_bool_bool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).implies((IBool) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * intersect
	 */

	public static int list_intersect_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).intersect((IList) stack[sp - 1]);
		return sp - 1;
	}

	public static int set_intersect_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).intersect((ISet) stack[sp - 1]);
		return sp - 1;
	}

	public static int map_intersect_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).common((IMap) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * in
	 */

	public static int elm_in_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int elm_in_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int elm_in_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 1]).containsKey((IValue) stack[sp - 2]);
		return sp - 1;
	}
	/*
	 * is
	 * 
	 */
	
	public static int is(Object[] stack, int sp, int arity) {
		assert arity == 2;
		IValue val  = (IValue) stack[sp - 2];
		Type tp = val.getType();
		String name = ((IString) stack[sp - 1]).getValue();
		if(tp.isAbstractData()){
			stack[sp - 2] = ((IConstructor)val).getConstructorType().getName().equals(name);
		} else if(tp.isNode()){
			stack[sp - 2] = ((INode) val).getName().equals(name);
		} else {
			stack[sp - 2] = false;
		}
		return sp - 1;
	}
	
	/*
	 * is_*: check the type of an IValue
	 */

	public static int  is_bool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isBool());
		return sp;
	}

	public static int  is_datetime(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isDateTime());
		return sp;
	}
	public static int  is_int(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isInteger());
		return sp;
	}

	public static int  is_list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isList());
		return sp;
	}

	public static int  is_loc(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isSourceLocation());
		return sp;
	}

	public static int  is_lrel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isListRelation());
		return sp;
	}

	public static int  is_map(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isMap());
		return sp;
	}

	public static int  is_node(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isNode());
		return sp;
	}

	public static int  is_num(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isNumber());
		return sp;
	}

	public static int  is_rat(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isRational());
		return sp;
	}

	public static int  is_real(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isReal());
		return sp;
	}

	public static int  is_rel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isRelation());
		return sp;
	}

	public static int  is_set(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isSet());
		return sp;
	}

	public static int  is_str(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isString());
		return sp;
	}

	public static int  is_tuple(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 2] = vf.bool(((IValue) stack[sp - 1]).getType().isTuple());
		return sp;
	}

	/*
	 * insertBefore
	 */
	/*
	 * intersect
	 * 
	 * infix intersect "&" {
	 *		list[&L] x list[&R]                  -> list[LUB(&L,&R)],
	 *		set[&L] x set[&R]                    -> set[LUB(&L,&R)],
	 * 		map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
} 
	 */
	/*
	 * in
	 */
	/*
	 * is
	 */
	/*
	 * isDefined
	 */
	/*
	 * join
	 */
	/*
	 * lessThan
	 */

	// int
	public static int int_less_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_less_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_less_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_less_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int num_less_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_less_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_less_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_less_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int rat_less_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_less_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_less_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_less_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int real_less_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_less_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_less_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_less_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	// less on other types
	public static int str_less_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
		stack[sp - 2] = c == -1;
		return sp - 1;
	}



	/*
	 * lessequal
	 */

	// int
	public static int int_lessequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_lessequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_lessequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_lessequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int num_lessequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_lessequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_lessequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_lessequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int rat_lessequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_lessequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_lessequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_lessequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int real_lessequal_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_lessequal_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_lessequal_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_lessequal_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}

	// lessequal on other types
	public static int str_lessequal_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
		stack[sp - 2] = c == -1 || c == 0;
		return sp - 1;
	}


	/*
	 * list_create
	 */
	public static int list_create(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		IListWriter writer = vf.listWriter();

		for (int i = arity - 1; i >= 0; i--) {
			writer.append((IValue) stack[sp - 1 - i]);
		}
		sp = sp - arity + 1;
		stack[sp - 1] = writer.done();

		return sp;
	}

	/*
	 * ...writer_open
	 */

	public static int listwriter_open(Object[] stack, int sp, int arity) {
		assert arity == 0;	// For now, later type can be added
		IListWriter writer = vf.listWriter();
		stack[sp] = writer;
		return sp + 1;
	}

	public static int setwriter_open(Object[] stack, int sp, int arity) {
		assert arity == 0;	// For now, later type can be added
		ISetWriter writer = vf.setWriter();
		stack[sp] = writer;
		return sp + 1;
	}
	
	public static int mapwriter_open(Object[] stack, int sp, int arity) {
		assert arity == 0;	// For now, later type can be added
		IMapWriter writer = vf.mapWriter();
		stack[sp] = writer;
		return sp + 1;
	}

	/*
	 * map_create
	 */
	public static int map_create(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		IMapWriter writer = vf.mapWriter();

		for (int i = arity; i > 0; i -= 2) {
			writer.put((IValue) stack[sp - i], (IValue) stack[sp - i + 1]);
		}
		sp = sp - arity + 1;
		stack[sp - 1] = writer.done();

		return sp;
	}

	/*
	 * set_create
	 */
	public static int set_create(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		ISetWriter writer = vf.setWriter();

		for (int i = arity - 1; i >= 0; i--) {
			writer.insert((IValue) stack[sp - 1 - i]);
		}
		sp = sp - arity + 1;
		stack[sp - 1] = writer.done();

		return sp;
	}

	public static int tuple_create(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		IValue[] elems = new IValue[arity];

		for (int i = arity - 1; i >= 0; i--) {
			elems[i] = (IValue) stack[sp - arity + i];
		}
		sp = sp - arity + 1;
		stack[sp - 1] = vf.tuple(elems);
		return sp;
	}

	/*
	 * notin
	 *
	 */
	public static int elm_notin_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = !((IList) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int elm_notin_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = !((ISet) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int elm_notin_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = !((IMap) stack[sp - 1]).containsKey((IValue) stack[sp - 2]);
		return sp - 1;
	}

	/*
	 * list_size
	 */
	public static int list_size(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = vf.integer(((IList) stack[sp - 1]).length());
		return sp;
	}
	
	public static int list_replace(Object[] stack, int sp, int arity) {
		assert arity == 5;
		IList lst = (IList) stack[sp - 5];
		IList repl = (IList) stack[sp - 1];
		SliceDescriptor sd = $makeSliceDescriptor(stack, sp, arity, lst.length());
		stack[sp - 5] = lst.replace(sd.first, sd.second, sd.end, repl);
		return sp - 4;
	}
	
	public static int str_replace(Object[] stack, int sp, int arity) {
		assert arity == 5;
		IString str = (IString) stack[sp - 5];
		IString repl = (IString) stack[sp - 1];
		SliceDescriptor sd = $makeSliceDescriptor(stack, sp, arity, str.length());
		stack[sp - 5] = str.replace(sd.first, sd.second, sd.end, repl);
		return sp - 4;
	}
	
	public static int node_replace(Object[] stack, int sp, int arity) {
		assert arity == 5;
		INode node = (INode) stack[sp - 5];
		IList repl = (IList) stack[sp - 1];
		SliceDescriptor sd = $makeSliceDescriptor(stack, sp, arity, node.arity());
		stack[sp - 5] = node.replace(sd.first, sd.second, sd.end, repl);
		return sp - 4;
	}
	
	private static Integer $getInt(IValue v){
		return v instanceof IInteger ? ((IInteger) v).intValue() : null;
	}
	
	public static int list_slice(Object[] stack, int sp, int arity) {
		assert arity == 4;
		
		IList lst = (IList) stack[sp - 4];
		stack[sp - 4] = $makeSlice(lst, $makeSliceDescriptor(stack, sp, arity, lst.length()));
		return sp - 3;
	}
	
	public static int str_slice(Object[] stack, int sp, int arity) {
		assert arity == 4;
		
		IString str = (IString) stack[sp - 4];
		stack[sp - 4] = $makeSlice(str, $makeSliceDescriptor(stack, sp, arity, str.length()));
		return sp - 3;
	}
	
	
	
	public static int node_create(Object[] stack, int sp, int arity) {
		assert arity >= 1;
		
		String name = ((IString) stack[sp - arity]).getValue();
		IValue[] args = new IValue[arity - 1];
		for(int i = 0; i < arity - 1; i ++){
			args[i] = (IValue) stack[sp - arity + 1 + i];
		}
		stack[sp - arity] = vf.node(name, args);
		return sp - arity + 1;
	}
	

	
	public static int node_slice(Object[] stack, int sp, int arity) {
		assert arity == 4;
		
		INode node = (INode) stack[sp - 4];
		stack[sp - 4] = $makeSlice(node, $makeSliceDescriptor(stack, sp, arity, node.arity()));
		return sp - 3;
	}
	
	public static SliceDescriptor $makeSliceDescriptor(Object[] stack, int sp, int arity, int len) {
		assert arity == 4;
		
		Integer first = $getInt((IValue) stack[sp - 3]);
	
		Integer second = $getInt((IValue) stack[sp - 2]);
		Integer end = $getInt((IValue) stack[sp - 1]);
		
		int firstIndex = 0;
		int secondIndex = 1;
		int endIndex = len;
		
		if(first != null){
			firstIndex = first;
			if(firstIndex < 0)
				firstIndex += len;
		}
		if(end != null){
			endIndex = end;
			if(endIndex < 0){
				endIndex += len;
			}
		}
		
		if(second == null){
			secondIndex = firstIndex + ((firstIndex <= endIndex) ? 1 : -1);
		} else {
			secondIndex = second;
			if(secondIndex < 0)
				secondIndex += len;
			if(!(first == null && end == null)){
				if(first == null && secondIndex > endIndex)
					firstIndex = len - 1;
				if(end == null && secondIndex < firstIndex)
					endIndex = -1;
			}
		}
		
		if (len == 0) {
			throw RuntimeExceptionFactory.emptyList(null, null);
		}
		if (firstIndex >= len) {
			throw RuntimeExceptionFactory.indexOutOfBounds(vf.integer(firstIndex), null, null);
		}
		if (endIndex > len ) {
			throw RuntimeExceptionFactory.indexOutOfBounds(vf.integer(endIndex), null, null);
		}
		
		return new SliceDescriptor(firstIndex, secondIndex, endIndex);
	}
	
	public static IList $makeSlice(IList lst, SliceDescriptor sd){
		IListWriter w = vf.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
		if(sd.first <= sd.end){
			for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
				w.append(lst.get(i));
			}
		} else {
			for(int j = sd.first; j >= 0 && j > sd.end && j < lst.length(); j += increment){
				w.append(lst.get(j));
			}
		}
		return w.done();
	}
	
	public static IString $makeSlice(IString str, SliceDescriptor sd){
		StringBuilder buffer = new StringBuilder();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
		if(sd.first <= sd.end){
			for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
				buffer.appendCodePoint(str.charAt(i));
			}
		} else {
			for(int j = sd.first; j >= 0 && j > sd.end && j < str.length(); j += increment){
				buffer.appendCodePoint(str.charAt(j));
			}
		}
		return vf.string(buffer.toString());
	}
	
	public static IList $makeSlice(INode node, SliceDescriptor sd){
		IListWriter w = vf.listWriter();
		int increment = sd.second - sd.first;
		if(sd.first == sd.end || increment == 0){
			// nothing to be done
		} else
		if(sd.first <= sd.end){
			for(int i = sd.first; i >= 0 && i < sd.end; i += increment){
				w.append(node.get(i));
			}
		} else {
			for(int j = sd.first; j >= 0 && j > sd.end && j < node.arity(); j += increment){
				w.append(node.get(j));
			}
		}
		
		return w.done();
	}
	
	/*
	 * stringwriter_*
	 */
	
	public static int stringwriter_open(Object[] stack, int sp, int arity) {
		assert arity == 0;
		stack[sp] = vf.string("");
		return sp + 1;
	}
	
	public static int stringwriter_add(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IString) stack[sp - 2]).concat(((IString) stack[sp - 1]));
		return sp - 1;
	}
	
	public static int stringwriter_close(Object[] stack, int sp, int arity) {
		assert arity == 1;
		return sp;
	}
	
	/*
	 * splice_to_...writer
	 */
	
	public static int listwriter_splice(Object[] stack, int sp, int arity) {
		assert arity == 2;
		IListWriter writer = (IListWriter)stack[sp - 2];
		if(stack[sp - 1] instanceof IList){
			IList lst = (IList) stack[sp - 1];
			for(IValue v : lst){
				writer.append(v);
			}
		} else if(stack[sp - 1] instanceof ISet){
			ISet set = (ISet) stack[sp - 1];
			for(IValue v : set){
				writer.append(v);
			}
		} else
			throw new RuntimeException("splice_to_listwriter illegal argument: " + stack[sp - 1].getClass());
		stack[sp - 2] = writer;
		return sp - 1;
	}
	
	public static int setwriter_splice(Object[] stack, int sp, int arity) {
		assert arity == 2;
		ISetWriter writer = (ISetWriter)stack[sp - 2];
		if(stack[sp - 1] instanceof IList){
			IList lst = (IList) stack[sp - 1];
			for(IValue v : lst){
				writer.insert(v);
			}
		} else if(stack[sp - 1] instanceof ISet){
			ISet set = (ISet) stack[sp - 1];
			for(IValue v : set){
				writer.insert(v);
			}
		} else
			throw new RuntimeException("splice_to_listwriter illegal argument: " + stack[sp - 1].getClass());
		stack[sp - 2] = writer;
		return sp - 1;
	}

	/*
	 * sublist
	 */
	public static int sublist(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList lst = (IList) stack[sp - 3];
		int offset = ((IInteger) stack[sp - 2]).intValue();
		int length = ((IInteger) stack[sp - 1]).intValue();
		stack[sp - 3] = lst.sublist(offset, length);
		return sp - 2;
	}

	
	/*
	 * product
	 * 
	 * infix Product "*" {
	 *		&L <: num x &R <: num                -> LUB(&L, &R),
	 * 		list[&L] x list[&R]                  -> lrel[&L,&R],
	 *		set[&L] x set[&R]                    -> rel[&L,&R]
	 * }
	 */

	// int
	public static int int_product_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_product_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_product_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_product_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int num_product_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_product_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_product_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_product_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int rat_product_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_product_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_product_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_product_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int real_product_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_product_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_product_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_product_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}
	
	
	/*
	 * remainder
	 */

	public static int int_remainder_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).remainder((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	
	/* 
	 * testreport_...
	 */
	static int number_of_tests = 0;
	static int number_of_failures = 0;
	static TypeReifier typeReifier;
	static final int MAXDEPTH = 5;
	static final int TRIES = 3;
	
	public static int testreport_open(Object[] stack, int sp, int arity) {
		assert arity == 0;
		number_of_tests = 0;
		number_of_failures = 0;
		typeReifier = new TypeReifier(vf);
		stdout.println("\nTEST REPORT\n");
		stack[sp] = null;
		return sp + 1;
	}
	
	public static int testreport_close(Object[] stack, int sp, int arity) {
		assert arity == 0;
		stdout.println("\nExecuted " + number_of_tests + " tests: "  
				+ (number_of_tests  - number_of_failures) + " succeeded; "
				+ number_of_failures + " failed.\n");
		stack[sp] = null;
		return sp + 1;
	}
	
	public static int testreport_add(Object[] stack, int sp, int arity) {
		assert arity == 3; 
		
		String fun = ((IString) stack[sp - 3]).getValue();
		ISourceLocation src = ((ISourceLocation) stack[sp - 2]);
		IConstructor type_cons = ((IConstructor) stack[sp - 1]);
		Type argType = typeReifier.valueToType(type_cons);
		IMap definitions = (IMap) type_cons.get("definitions");
		
		TypeStore store = new TypeStore();
		typeReifier.declareAbstractDataTypes(definitions, store);
		
		int nargs = argType.getArity();
		IValue[] args = new IValue[nargs];
		
		TypeParameterVisitor tpvisit = new TypeParameterVisitor();
		Type requestedType = tf.tupleType(argType);
		HashMap<Type, Type> tpbindings = tpvisit.bindTypeParameters(requestedType);
		RandomValueTypeVisitor randomValue = new RandomValueTypeVisitor(vf, MAXDEPTH, tpbindings, store);
		
		int tries = nargs == 0 ? 1 : TRIES;
		boolean passed = true;
		for(int i = 0; i < tries; i++){
			if(nargs > 0){
				ITuple tup = (ITuple) randomValue.generate(argType);
				for(int j = 0; j < args.length; j++){
					args[j] = tup.get(j);
					//stdout.println("args[" + j + "] = " + args[j]);
				}
			}
			IValue res = rvm.executeFunction(fun, args);  // TODO: catch exceptions
			passed = ((IBool) res).getValue();
			if(!passed){
				number_of_failures++;
				break;
			}
		}
		
		number_of_tests++;
		stdout.println("Test " + fun + (passed ? ": succeeded" : ": FAILED") + " at " + src);
		return sp - 2;
	}

	/*
	 * negation
	 */

	public static int not_bool(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IBool) stack[sp - 1]).not();
		return sp;
	}

	/*
	 * notequal
	 */

	public static int notequal(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = vf.bool(!((IValue) stack[sp - 2]).isEqual((IValue) stack[sp - 1]));
		return sp - 1;
	}

	/*
	 * negative
	 * 
	 * prefix UnaryMinus "-" { &L <: num -> &L }
	 */

	public static int negative_int(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IInteger) stack[sp - 1]).negate();
		return sp;
	}
	public static int negative_real(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IReal) stack[sp - 1]).negate();
		return sp;
	}
	
	public static int negative_rat(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IRational) stack[sp - 1]).negate();
		return sp;
	}
	
	public static int negative_num(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((INumber) stack[sp - 1]).negate();
		return sp;
	}
	

	/*
	 * notEquals
	 */

	/*
	 * or
	 */

	public static int or_bool_bool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).or((IBool) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * println
	 */

	public static int println(Object[] stack, int sp, int arity) {
		stdout.println(">>>>> " + stack[sp - 1]);
		return sp;
	}

	


	/*
	 * slice
	 */

	/*
	 * setAnnotation
	 */

	/*
	 * subscript
	 */
	
	public static int adt_subscript(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IConstructor) stack[sp - 2]).get(((IInteger) stack[sp - 1]).intValue());
		return sp - 1;
	}
	
	public static int list_subscript(Object[] stack, int sp, int arity) {
		assert arity == 2;
		IList lst = ((IList) stack[sp - 2]);
		int idx = ((IInteger) stack[sp - 1]).intValue();
		stack[sp - 2] = lst.get(idx >= 0 ? idx : lst.length() - idx);
		return sp - 1;
	}

	public static Object map_subscript(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).get((IValue) stack[sp - 1]);
		return sp - 1;
	}
	
	
	public static int tuple_subscript(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ITuple) stack[sp - 2]).get(((IInteger) stack[sp - 1]).intValue());
		return sp - 1;
	}

	/*
	 * subtraction
	 * 
	 * infix Difference "-" {
	 *		&L <: num x &R <: num                -> LUB(&L, &R),
	 * 		list[&L] x list[&R]                  -> list[LUB(&L,&R)],
	 *		set[&L] x set[&R]                    -> set[LUB(&L,&R)],
	 * 		map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
	 * }
	 */

	// Numbers
	// int
	public static int int_subtract_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_subtract_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_subtract_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int int_subtract_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int num_subtract_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_subtract_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_subtract_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int num_subtract_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int rat_subtract_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_subtract_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_subtract_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int rat_subtract_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int real_subtract_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_subtract_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_subtract_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int real_subtract_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}


	public static int list_subtract_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).subtract((IList) stack[sp - 1]);
		return sp - 1;
	}

	public static int set_subtract_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).subtract((ISet) stack[sp - 1]);
		return sp - 1;
	}

	public static int map_subtract_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).remove((IMap) stack[sp - 1]);
		return sp - 1;
	}
	
	/*
	 * subtype
	 */
	
	public static int subtype(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = vf.bool(((Type) stack[sp - 2]).isSubtypeOf((Type) stack[sp - 1]));
		return sp - 1;
	}
	

	/*
	 * transitiveClosure
	 * 
	 * postfix Closure "+", "*" { 
	 *  	lrel[&L,&L]			-> lrel[&L,&L],
	 * 		rel[&L,&L]  		-> rel[&L,&L]
	 * }
	 */

	@SuppressWarnings("rawtypes")
	public static int lrel_transitive_closure(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IListRelation) stack[sp - 1]).closure();
		return sp;
	}

	@SuppressWarnings("rawtypes")
	public static int rel_transitive_closure(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IRelationalAlgebra) stack[sp - 1]).closure();
		return sp;
	}

	/*
	 * transitiveReflexiveClosure
	 */
	@SuppressWarnings("rawtypes")
	public static int lrel_transitive_reflexive_closure(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IListRelation) stack[sp - 1]).closureStar();
		return sp;
	}

	@SuppressWarnings("rawtypes")
	public static int rel_transitive_reflexive_closure(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] =((IRelationalAlgebra) stack[sp - 1]).closureStar();
		return sp;
	}
	
	/*
	 * typeOf
	 */
	
	public static int typeOf(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IValue) stack[sp - 1]).getType();
		return sp;
	}
	
	/*
	 * update_...
	 */
	
	public static int adt_update(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IConstructor cons = (IConstructor) stack[sp - 3];
		String field = ((IString) stack[sp - 2]).getValue();
		stack[sp - 3] = cons.set(field, (IValue) stack[sp - 1]);
		return sp - 2;
	}
	
	public static int list_update(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList lst = (IList) stack[sp - 3];
		int n = ((IInteger) stack[sp - 2]).intValue();
		stack[sp - 3] = lst.put(n, (IValue) stack[sp - 1]);
		return sp - 2;
	}
	
	public static int map_update(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IMap map = (IMap) stack[sp - 3];
		IValue key = (IValue) stack[sp - 2];
		stack[sp - 3] = map.put(key, (IValue) stack[sp - 1]);
		return sp - 2;
	}
	
	public static int tuple_update(Object[] stack, int sp, int arity) {
		assert arity == 3;
		ITuple tup = (ITuple) stack[sp - 3];
		int n = ((IInteger) stack[sp -2]).intValue();
		stack[sp - 3] = tup.set(n, (IValue) stack[sp - 1]);
		return sp - 2;
	}
	
	
	
	public static int value_to_string(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = vf.string(((IValue) stack[sp -1]).toString());
		return sp;
	}

	/*
	 * Run this class as a Java program to compare the list of enumeration constants with the implemented methods in this class.
	 */

	public static void main(String[] args) {
		init(ValueFactoryFactory.getValueFactory(), null, null);
		System.err.println("RascalPrimitives have been validated!");
	}

}

class SliceDescriptor{
	
	final int first;
	final int second;
	final int end;
	
	SliceDescriptor(int first, int second, int end){
		this.first = first;
		this.second = second;
		this.end = end;
	}
}

