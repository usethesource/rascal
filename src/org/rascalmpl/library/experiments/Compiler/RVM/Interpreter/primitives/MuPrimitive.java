package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.primitives;

public class MuPrimitive {
	MuPrimitive1 mup1 = new MuPrimitive1();
	MuPrimitive2 mup2 = new MuPrimitive2();
	
	MuPrimitive1[] muPrimitives1 = {
			mup1.new size_str()
	};
	
	MuPrimitive2[] muPrimitives2 = {
			mup2.new addition_mint_mint()
	};
	

}
