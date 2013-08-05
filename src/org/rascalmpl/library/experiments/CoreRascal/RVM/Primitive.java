package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

/*
 * The primitives that can be called from the RVM interpreter loop.
 * Each primitive with name P (e.g. addition_int_int) is defined by:
 * - a constant P below
 * - a static method int P(Object[] stack, int sp)
 * 
 * Each primitive implementation gets the current stack and stack pointer as argument
 * and returns a new stack pointer. It may make mdifications to the stack.
 */

public enum Primitive {
	addition_int_int,
	appendAfter,
	addition_list_list,
	equal_int_int,
	greater_int_int,
	make_list,
	make_map,
	make_set,
	multiplication_int_int,
	substraction_int_int,
	subscript_list_int, 
	subscript_map;
	
	private static Primitive[] values = Primitive.values();

	public static Primitive fromInteger(int prim){
		return values[prim];
	}
	
	
	
	private static IValueFactory vf;
	private static IBool TRUE;
	private static IBool FALSE;
	static Method [] methods;

	/**
	 * Initialize the primitive methods.
	 * @param fact value factory to be used
	 */
	public static void init(IValueFactory fact) {
		vf = fact;
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		Method [] methods1 = Primitive.class.getDeclaredMethods();
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
	 * @return		new sstack pointer and modified stack contents
	 */
	int invoke(Object[] stack, int sp) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return (int) methods[ordinal()].invoke(null, stack,  sp);
	}
	
	/***************************************************************
	 * 				IMPLEMENTATION OF PRIMITIVES                   *
	 ***************************************************************/
	
	/*
	 * addition
	 */

	public static int addition_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2])
				.add((IInteger) stack[sp - 1]);
		return sp - 1;
	}

	public static int addition_list_list(Object[] stack, int sp) {
		stack[sp - 2] = ((IList) stack[sp - 2]).concat((IList) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * appendAfter
	 */
	public static int appendAfter(Object[] stack, int sp) {
		stack[sp - 2] = ((IList) stack[sp - 2]).append((IValue) stack[sp - 1]);
		return sp - 1;
	}
	
	/*
	 * asType
	 */
	/*
	 * composition
	 */
	/*
	 * division
	 */
	
	/*
	 * equals
	 */

	public static int equal_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2])
				.equal((IInteger) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * fieldAccess
	 */
	/*
	 * fieldUpdate
	 */
	/*
	 * fieldProject
	 */
	/*
	 * getAnnotation
	 */
	/*
	 * greater
	 */
	public static int greater_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater(
				(IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
		return sp - 1;
	}

	/*
	 * greaterThan
	 */
	/*
	 * greaterThanOrEq
	 */
	/*
	 * has
	 */
	/*
	 * insertBefore
	 */
	/*
	 * intersection
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
	/*
	 * lessThanOrEq
	 */
	
	/*
	 * make_list
	 */
	public static int make_list(Object[] stack, int sp) {
		int len = ((IInteger) stack[sp - 1]).intValue();
		IListWriter writer = vf.listWriter();

		for (int i = len - 1; i >= 0; i--) {
			writer.append((IValue) stack[sp - 2 - i]);
		}
		sp = sp - len;
		stack[sp - 1] = writer.done();

		return sp;
	}
	
	/*
	 * make_map
	 */
	public static int make_map(Object[] stack, int sp) {
		int len = ((IInteger) stack[sp - 1]).intValue();
		IMapWriter writer = vf.mapWriter();

		for (int i = 2 * (len - 1); i >= 0; i -= 2) {
			writer.insert((IValue) stack[sp - 2 - i], (IValue) stack[sp - 2 - i + 1]);
		}
		sp = sp - 2 * len;
		stack[sp - 1] = writer.done();

		return sp;
	}
	
	/*
	 * make_set
	 */
	public static int make_set(Object[] stack, int sp) {
		int len = ((IInteger) stack[sp - 1]).intValue();
		ISetWriter writer = vf.setWriter();

		for (int i = len - 1; i >= 0; i--) {
			writer.insert((IValue) stack[sp - 2 - i]);
		}
		sp = sp - len;
		stack[sp - 1] = writer.done();

		return sp;
	}

	/*
	 * mod
	 */
	
	/*
	 * multiplication
	 */
	public static int multiplication_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2])
				.multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * negation
	 */
	
	/*
	 * negative
	 */
	
	/*
	 * nonEquals
	 */
	
	/*
	 * product
	 */
	
	/*
	 * remainder /* slice
	 */
	
	/*
	 * splice
	 */
	
	/*
	 * setAnnotation
	 */
	
	/*
	 * subscript
	 */
	public static int subscript_list_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IList) stack[sp - 2]).get(((IInteger) stack[sp - 1])
				.intValue());
		return sp - 1;
	}

	public static Object subscript_map(Object[] stack, int sp) {
		stack[sp - 2] = ((IMap) stack[sp - 2]).get((IValue) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * substraction
	 */
	public static int substraction_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2])
				.subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}

	/*
	 * transitiveClosure
	 */

	/*
	 * transitiveReflexiveClosure
	 */


}

