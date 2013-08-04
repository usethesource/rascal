package org.rascalmpl.library.experiments.CoreRascal.RVM;

public enum Primitive {
	addition_int_int,
	multiplication_int_int,
	substraction_int_int,
	equal_int_int,
	greater_int_int;
	
	private static Primitive[] values = Primitive.values();

	public static Primitive fromInteger(int prim){
		return values[prim];
	}
}

