package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.primitives;

import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class UseMuPrimitiveX {
	
	
	public static void main(String[] args) {
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();

		// Some shorthands
		MuPrimitiveX mup = new MuPrimitiveX();
		MuPrimitiveX1 muPrimitives1[] = mup.muPrimitives1;
		MuPrimitiveX2 muPrimitives2[] = mup.muPrimitives2;
		
		// size_str("abc")
		int size_str = 0;
		Object tos = vf.string("abc");
		tos = muPrimitives1[size_str].execute1(tos);
		System.out.println(tos);
		
		// addition_mint_mint(10, 20)
		int addition_mint_mint = 0;
		Object[] stack = {10};
		int sp = 1;
		tos = 20;
		
		tos = muPrimitives2[addition_mint_mint].execute2(stack[--sp], tos);
		
		System.out.println(tos);
		
		// method pointer to size_str in code array
		Object [] code = { muPrimitives1[size_str] };
		
		// size_str("abc")
		tos = vf.string("abc");
		tos = ((MuPrimitiveX1)code[0]).execute1(tos);
		System.out.println(tos);
	}
}
