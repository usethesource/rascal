package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.primitives;

import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class UseMuPrimitives {
	
	
	public static void main(String[] args) {
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();

		MuPrimitive mup = new MuPrimitive();
		MuPrimitive1 muPrimitives1[] = mup.muPrimitives1;
		MuPrimitive2 muPrimitives2[] = mup.muPrimitives2;
		
		int size_str = 0;
		Object tos = vf.string("abc");
		tos = muPrimitives1[size_str].execute1(tos);
		System.out.println(tos);
		
		int addition_mint_mint = 0;
		Object[] stack = {10};
		int sp = 1;
		tos = 20;
		
		tos = muPrimitives2[addition_mint_mint].execute2(stack[--sp], tos);
		
		System.out.println(tos);
	}
}
