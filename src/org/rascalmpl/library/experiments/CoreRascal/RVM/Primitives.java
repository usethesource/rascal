package org.rascalmpl.library.experiments.CoreRascal.RVM;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Primitives {
	
	private static IValueFactory vf;
	private static IBool TRUE;
	private static IBool FALSE;
	
	public static void init(IValueFactory fact){
		vf = fact;
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		Primitive.bindMethods();
	}
	
	// addition 

// appendAfter 
// asType 
// composition 
// division 
// equals 

// fieldAccess 
// fieldUpdate 
// fieldProject 
// getAnnotation 
// greater 

// greaterThan 
// greaterThanOrEq 
// has 
// insertBefore 
// intersection 
// in 
// is 
// isDefined 
// join 
// lessThan 
// lessThanOrEq 
// makeList 
		
// mod 
// multiplication 
// negation 
// negative 
// nonEquals 
// product 
// remainder 
// slice 
// splice 
// setAnnotation 
// subscript 
// substraction 	
// transitiveClosure 
// transitiveRefleixiveClosure 
	
	public static int addition_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	
	public static int equal_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	
	public static int greater_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
		return sp - 1;
	}
	
	public static int multiplication_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
		return sp - 1;
	}
	
	public static int substraction_int_int(Object[] stack, int sp) {
		stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
		return sp - 1;
	}

	public static int subscript_list_int(Object[] stack, int sp){
		stack[sp - 2] = ((IList) stack[sp - 2]).get(((IInteger) stack[sp - 1]).intValue());
		return sp - 1;
	}

	public static int addition_list_list(Object[] stack, int sp) {
		stack[sp - 2] = ((IList) stack[sp - 2]).concat((IList) stack[sp - 1]);
		return sp - 1;
	}

	public static Object subscript_map(Object[] stack, int sp) {
		stack[sp - 2] = ((IMap) stack[sp - 2]).get((IValue) stack[sp - 1]);
		return sp - 1;
	}

//	public static Object subscript2(IValue iValue, IValue idx1, IValue idx2) {
//		return null;
//	}

	public static int make_list(Object[] stack, int sp) {
		int len = ((IInteger) stack[sp - 1]).intValue();
		IListWriter writer = vf.listWriter();
		
		for(int i = len - 1; i >= 0; i--){
			writer.append((IValue)stack[sp - 2 - i]);
		}
		sp = sp - len;
		stack[sp - 1] = writer.done();
		
		return sp;
	}

}
