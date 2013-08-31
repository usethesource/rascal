package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListRelation;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
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
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
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

	add_to_listwriter,
	add_to_setwriter,
	add_to_mapwriter,

	addition_elm_list,
	addition_list_elm,
	addition_list_list,
	addition_map_map,
	addition_tuple_tuple,

	addition_int_int,
	addition_int_num,
	addition_int_rat,
	addition_int_real,

	addition_num_int,
	addition_num_num,
	addition_num_real,
	addition_num_rat,

	addition_rat_int,
	addition_rat_num,
	addition_rat_rat,
	addition_rat_real,

	addition_real_num,
	addition_real_int,
	addition_real_real,
	addition_real_rat,

	addition_elm_set,
	addition_set_elm,
	addition_set_set,
	addition_str_str,

	composition_lrel_lrel,
	composition_rel_rel,
	composition_map_map,

	division_int_int,
	division_int_num,
	division_int_rat,
	division_int_real,

	division_num_int,
	division_num_num,
	division_num_real,
	division_num_rat,

	division_rat_int,
	division_rat_num,
	division_rat_rat,
	division_rat_real,

	division_real_num,
	division_real_int,
	division_real_real,
	division_real_rat,

	done_listwriter,
	done_setwriter,
	done_mapwriter,
	equal,
	
	field_access_adt,
	field_access_datetime,
	field_access_loc,
	field_access_tuple,
	
	field_project_tuple,
	
	field_update_adt,
	
	
	equivalent_bool_bool,

	greater_int_int,
	greater_int_num,
	greater_int_rat,
	greater_int_real,

	greater_num_int,
	greater_num_num,
	greater_num_real,
	greater_num_rat,

	greater_rat_int,
	greater_rat_num,
	greater_rat_rat,
	greater_rat_real,

	greater_real_num,
	greater_real_int,
	greater_real_real,
	greater_real_rat,

	greater_str_str,

	greaterequal_int_int,
	greaterequal_int_num,
	greaterequal_int_rat,
	greaterequal_int_real,

	greaterequal_num_int,
	greaterequal_num_num,
	greaterequal_num_real,
	greaterequal_num_rat,

	greaterequal_rat_int,
	greaterequal_rat_num,
	greaterequal_rat_rat,
	greaterequal_rat_real,

	greaterequal_real_num,
	greaterequal_real_int,
	greaterequal_real_real,
	greaterequal_real_rat,

	greaterequal_str_str,

	implies_bool_bool,
	intersection_set_set,
	intersection_list_list,
	intersection_map_map,
	in_elm_list,
	in_elm_set,
	in_elm_map,
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

	less_int_int,
	less_int_num,
	less_int_rat,
	less_int_real,

	less_num_int,
	less_num_num,
	less_num_real,
	less_num_rat,

	less_rat_int,
	less_rat_num,
	less_rat_rat,
	less_rat_real,

	less_real_num,
	less_real_int,
	less_real_real,
	less_real_rat,

	less_str_str,

	lessequal_int_int,
	lessequal_int_num,
	lessequal_int_rat,
	lessequal_int_real,

	lessequal_num_int,
	lessequal_num_num,
	lessequal_num_real,
	lessequal_num_rat,

	lessequal_rat_int,
	lessequal_rat_num,
	lessequal_rat_rat,
	lessequal_rat_real,

	lessequal_real_num,
	lessequal_real_int,
	lessequal_real_real,
	lessequal_real_rat,

	lessequal_str_str,

	make_list,
	make_listwriter,
	make_set,
	make_setwriter,
	make_map,
	make_mapwriter,
	make_tuple,
	negative,
	not_bool,
	notequal,
	notin_elm_list,
	notin_elm_set,
	notin_elm_map,
	or_bool_bool,
	println,

	product_int_int,
	product_int_num,
	product_int_rat,
	product_int_real,

	product_num_int,
	product_num_num,
	product_num_real,
	product_num_rat,

	product_rat_int,
	product_rat_num,
	product_rat_rat,
	product_rat_real,

	product_real_num,
	product_real_int,
	product_real_real,
	product_real_rat,

	remainder_int_int,
	
	report_test_result,

	size_list,
	splice_to_listwriter,
	splice_to_setwriter,

	sublist,
	
	subscript_adt,
	subscript_list,  
	subscript_map,
	subscript_tuple,

	subtraction_list_list,
	subtraction_map_map,
	subtraction_set_set,

	subtraction_int_int,
	subtraction_int_num,
	subtraction_int_rat,
	subtraction_int_real,

	subtraction_num_int,
	subtraction_num_num,
	subtraction_num_real,
	subtraction_num_rat,

	subtraction_rat_int,
	subtraction_rat_num,
	subtraction_rat_rat,
	subtraction_rat_real,

	subtraction_real_num,
	subtraction_real_int,
	subtraction_real_real,
	subtraction_real_rat,

	
	
	transitive_closure_lrel,
	transitive_closure_rel,
	transitive_reflexive_closure_lrel,
	transitive_reflexive_closure_rel,
	
	update_adt,
	update_list,
	update_map,
	update_tuple,

	equal_type_type,
	subtype,
	typeOf
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

	/**
	 * Initialize the primitive methods.
	 * @param fact value factory to be used
	 */
	public static void init(IValueFactory fact) {
		vf = fact;
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
	 * add_to_...writer
	 */
	public static int add_to_listwriter(Object[] stack, int sp, int arity) {
		assert arity > 0;
		IListWriter writer = (IListWriter) stack[sp - arity];
		for(int i = arity - 1; i > 0; i--){
			writer.append((IValue) stack[sp - i]);
		}
		return sp - arity + 1;
	}

	public static int add_to_setwriter(Object[] stack, int sp, int arity) {
		assert arity > 0;
		ISetWriter writer = (ISetWriter) stack[sp - arity];
		for(int i = arity - 1; i > 0; i--){
			writer.insert((IValue) stack[sp - i]);
		}
		return sp - arity + 1;
	}
	
	public static int add_to_mapwriter(Object[] stack, int sp, int arity) {
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
	public static int addition_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int addition_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int addition_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int addition_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int addition_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).add((IRational) stack[sp - 1]);
		return sp - 1;
	}


	public static int addition_list_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).concat((IList) stack[sp - 1]);
		return sp - 1;
	}

	public static int addition_map_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).join((IMap) stack[sp - 1]);
		return sp - 1;
	}

	public static int addition_list_elm(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).append((IValue) stack[sp - 1]);
		return sp - 1;
	}

	public static int addition_elm_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 1]).insert((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int addition_set_elm(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).insert((IValue) stack[sp - 1]);
		return sp - 1;
	}

	public static int addition_elm_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 1]).insert((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int addition_set_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).union((ISet) stack[sp - 1]);
		return sp - 1;
	}

	public static int addition_str_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IString) stack[sp - 2]).concat((IString) stack[sp - 1]);
		return sp - 1;
	}

	//	public static int addition_loc_str(Object[] stack, int sp) { 	}

	public static int addition_tuple_tuple(Object[] stack, int sp, int arity) {
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
	public static int composition_lrel_lrel(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IListRelation) stack[sp - 2]).compose((IListRelation) stack[sp - 1]);
		return sp - 1;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int composition_rel_rel(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRelationalAlgebra) stack[sp - 2]).compose((IRelationalAlgebra) stack[sp - 1]);
		return sp - 1;
	}

	public static int composition_map_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).compose((IMap) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * division
	 * 
	 * infix Division "/" { &L <: num x &R <: num        -> LUB(&L, &R) }
	 */

	// int
	public static int division_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int division_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int division_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	// num
	public static int division_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IInteger) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IRational) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	// rat
	public static int division_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int division_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int division_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	// real
	public static int division_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((INumber) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IInteger) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IReal) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}
	public static int division_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).divide((IRational) stack[sp - 1], vf.getPrecision());
		return sp - 1;
	}



	/*
	 * done_...writer
	 */

	public static int done_listwriter(Object[] stack, int sp, int arity) {
		assert arity == 0;
		IListWriter writer = (IListWriter) stack[sp - 1];
		stack[sp - 1] = writer.done();
		return sp;
	}

	public static int done_setwriter(Object[] stack, int sp, int arity) {
		assert arity == 0;
		ISetWriter writer = (ISetWriter) stack[sp - 1];
		stack[sp - 1] = writer.done();
		return sp;
	}
	
	public static int done_mapwriter(Object[] stack, int sp, int arity) {
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
	
	public static int equal_type_type(Object[] stack, int sp, int arity) {
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
	public static int field_access_adt(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IConstructor) stack[sp - 2]).get(((IString) stack[sp - 1]).getValue());
		return sp - 1;
	}
	
	public static int field_access_datetime(Object[] stack, int sp, int arity) {
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
	
	public static int field_access_loc(Object[] stack, int sp, int arity) {
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
			v = vf.tuple(lineColumnType, vf.integer(sloc.getBeginLine()), 
															vf.integer(sloc.getBeginColumn()));
			break;
		case "end":
			v = vf.tuple(lineColumnType, vf.integer(sloc.getEndLine()), 
															vf.integer(sloc.getEndColumn()));
			break;

		default:
			throw new RuntimeException("Access to non-existing field " + field
					+ " in location");

		}
		stack[sp - 2] = v;
		return sp - 1;
	}
	
	public static int field_access_tuple(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ITuple) stack[sp - 2]).get(((IString) stack[sp - 1]).getValue());
		return sp - 1;
	}
	
	public static int field_project_tuple(Object[] stack, int sp, int arity) {
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
	public static int field_update_adt(Object[] stack, int sp, int arity) {
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
	public static int greater_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int greater_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int greater_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int greater_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int greater_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greater((IRational) stack[sp - 1]);
		return sp - 1;
	}

	// greater on other types
	public static int greater_str_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]) == 1;
		return sp - 1;
	}

	/*
	 * greaterThanOrEq
	 */

	// int
	public static int greaterequal_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int greaterequal_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int greaterequal_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int greaterequal_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int greaterequal_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).greaterEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}

	// greaterequal on other types
	public static int greaterequal_str_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
		stack[sp - 2] = c == 0 || c == 1;
		return sp - 1;
	}

	/*
	 * has
	 */


	/*
	 * implies
	 */

	public static int implies_bool_bool(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IBool) stack[sp - 2]).implies((IBool) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * intersection
	 */

	public static int intersection_list_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).intersect((IList) stack[sp - 1]);
		return sp - 1;
	}

	public static int intersection_set_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).intersect((ISet) stack[sp - 1]);
		return sp - 1;
	}

	public static int intersection_map_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).common((IMap) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * in
	 */

	public static int in_elm_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int in_elm_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int in_elm_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 1]).containsKey((IValue) stack[sp - 2]);
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
	 * intersection
	 * 
	 * infix Intersection "&" {
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
	public static int less_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int less_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int less_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int less_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int less_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).less((IRational) stack[sp - 1]);
		return sp - 1;
	}
	// less on other types
	public static int less_str_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
		stack[sp - 2] = c == -1;
		return sp - 1;
	}



	/*
	 * lessequal
	 */

	// int
	public static int lessequal_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int lessequal_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int lessequal_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int lessequal_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int lessequal_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).lessEqual((IRational) stack[sp - 1]);
		return sp - 1;
	}

	// lessequal on other types
	public static int lessequal_str_str(Object[] stack, int sp, int arity) {
		assert arity == 2;
		int c = ((IString) stack[sp - 2]).compare((IString) stack[sp - 1]);
		stack[sp - 2] = c == -1 || c == 0;
		return sp - 1;
	}


	/*
	 * make_list
	 */
	public static int make_list(Object[] stack, int sp, int arity) {
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
	 * make_...writer
	 */

	public static int make_listwriter(Object[] stack, int sp, int arity) {
		assert arity == 0;	// For now, later type can be added
		IListWriter writer = vf.listWriter();
		stack[sp] = writer;
		return sp + 1;
	}

	public static int make_setwriter(Object[] stack, int sp, int arity) {
		assert arity == 0;	// For now, later type can be added
		ISetWriter writer = vf.setWriter();
		stack[sp] = writer;
		return sp + 1;
	}
	
	public static int make_mapwriter(Object[] stack, int sp, int arity) {
		assert arity == 0;	// For now, later type can be added
		IMapWriter writer = vf.mapWriter();
		stack[sp] = writer;
		return sp + 1;
	}

	/*
	 * make_map
	 */
	public static int make_map(Object[] stack, int sp, int arity) {
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
	 * make_set
	 */
	public static int make_set(Object[] stack, int sp, int arity) {
		assert arity >= 0;
		ISetWriter writer = vf.setWriter();

		for (int i = arity - 1; i >= 0; i--) {
			writer.insert((IValue) stack[sp - 1 - i]);
		}
		sp = sp - arity + 1;
		stack[sp - 1] = writer.done();

		return sp;
	}

	public static int make_tuple(Object[] stack, int sp, int arity) {
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
	public static int notin_elm_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = !((IList) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int notin_elm_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = !((ISet) stack[sp - 1]).contains((IValue) stack[sp - 2]);
		return sp - 1;
	}

	public static int notin_elm_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = !((IMap) stack[sp - 1]).containsKey((IValue) stack[sp - 2]);
		return sp - 1;
	}


	/*
	 * size_list
	 */
	public static int size_list(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = vf.integer(((IList) stack[sp - 1]).length());
		return sp;
	}
	
	/*
	 * splice_to_...writer
	 */
	
	public static int splice_to_listwriter(Object[] stack, int sp, int arity) {
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
	
	public static int splice_to_setwriter(Object[] stack, int sp, int arity) {
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
	public static int product_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int product_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int product_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int product_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int product_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).multiply((IRational) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * remainder
	 */

	public static int remainder_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).remainder((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	
	/* 
	 * report_test_result
	 */
	
	public static int report_test_result(Object[] stack, int sp, int arity) {
		assert arity == 3;
		
		String fun = ((IString) stack[sp - 3]).getValue();
		ISourceLocation src = ((ISourceLocation) stack[sp - 2]);
		boolean passed = (Boolean) stack[sp - 1];
		System.err.println("Test " + fun + (passed ? ": succeeeded" : ": FAILED") + " at " + src);
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

	public static int negative(Object[] stack, int sp, int arity) {
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
		System.out.println(">>>>> " + stack[sp - 1]);
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
	
	public static int subscript_adt(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IConstructor) stack[sp - 2]).get(((IInteger) stack[sp - 1]).intValue());
		return sp - 1;
	}
	
	public static int subscript_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).get(((IInteger) stack[sp - 1]).intValue());
		return sp - 1;
	}

	public static Object subscript_map(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IMap) stack[sp - 2]).get((IValue) stack[sp - 1]);
		return sp - 1;
	}
	
	public static int subscript_tuple(Object[] stack, int sp, int arity) {
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
	public static int subtraction_int_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_int_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_int_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_int_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// num
	public static int subtraction_num_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_num_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_num_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_num_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((INumber) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// rat
	public static int subtraction_rat_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_rat_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_rat_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_rat_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IRational) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	// real
	public static int subtraction_real_num(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((INumber) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_real_int(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_real_real(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IReal) stack[sp - 1]);
		return sp - 1;
	}
	public static int subtraction_real_rat(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IReal) stack[sp - 2]).subtract((IRational) stack[sp - 1]);
		return sp - 1;
	}


	public static int subtraction_list_list(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((IList) stack[sp - 2]).subtract((IList) stack[sp - 1]);
		return sp - 1;
	}

	public static int subtraction_set_set(Object[] stack, int sp, int arity) {
		assert arity == 2;
		stack[sp - 2] = ((ISet) stack[sp - 2]).subtract((ISet) stack[sp - 1]);
		return sp - 1;
	}

	public static int subtraction_map_map(Object[] stack, int sp, int arity) {
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
	public static int transitive_closure_lrel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IListRelation) stack[sp - 1]).closure();
		return sp;
	}

	@SuppressWarnings("rawtypes")
	public static int transitive_closure_rel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IRelationalAlgebra) stack[sp - 1]).closure();
		return sp;
	}

	/*
	 * transitiveReflexiveClosure
	 */
	@SuppressWarnings("rawtypes")
	public static int transitive_reflexive_closure_lrel(Object[] stack, int sp, int arity) {
		assert arity == 1;
		stack[sp - 1] = ((IListRelation) stack[sp - 1]).closureStar();
		return sp;
	}

	@SuppressWarnings("rawtypes")
	public static int transitive_reflexive_closure_rel(Object[] stack, int sp, int arity) {
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
	
	public static int update_adt(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IConstructor cons = (IConstructor) stack[sp - 3];
		String field = ((IString) stack[sp - 2]).getValue();
		stack[sp - 3] = cons.set(field, (IValue) stack[sp - 1]);
		return sp - 2;
	}
	
	public static int update_list(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IList lst = (IList) stack[sp - 3];
		int n = ((IInteger) stack[sp - 2]).intValue();
		stack[sp - 3] = lst.put(n, (IValue) stack[sp - 1]);
		return sp - 2;
	}
	
	public static int update_map(Object[] stack, int sp, int arity) {
		assert arity == 3;
		IMap map = (IMap) stack[sp - 3];
		IValue key = (IValue) stack[sp - 2];
		stack[sp - 3] = map.put(key, (IValue) stack[sp - 1]);
		return sp - 2;
	}
	
	public static int update_tuple(Object[] stack, int sp, int arity) {
		assert arity == 3;
		ITuple tup = (ITuple) stack[sp - 3];
		int n = ((IInteger) stack[sp -2]).intValue();
		stack[sp - 3] = tup.set(n, (IValue) stack[sp - 1]);
		return sp - 2;
	}

	/*
	 * Run this class as a Java program to compare the list of enumeration constants with the implemented methods in this class.
	 */

	public static void main(String[] args) {
		init(ValueFactoryFactory.getValueFactory());
	}

}

