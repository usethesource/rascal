package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public enum Primitive {
	addition_int_int,
	multiplication_int_int,
	substraction_int_int,
	equal_int_int,
	greater_int_int,
	subscript_list_int, addition_list_list, subscript_map, subscript2, make_list;
	
	private static Primitive[] values = Primitive.values();

	public static Primitive fromInteger(int prim){
		return values[prim];
	}
	
	static Method [] methods;
	
	static void bindMethods(){
		Method [] methods1 = Primitives.class.getDeclaredMethods();
		methods = new Method[methods1.length];
		for(int i = 0; i < methods1.length; i++){
			Method m = methods1[i];
			System.err.println(m.getName());
			if(m.getName() != "init"){
				methods[valueOf(m.getName()).ordinal()] = m;
			}
		}
	}
	
	int invoke(Object[] stack, int sp) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		return (int) methods[ordinal()].invoke(null, stack,  sp);
	}
}

