package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.primitives;

public class MuPrimitiveX {
	
	MuPrimitiveX1 mup1 = new MuPrimitiveX1();
	MuPrimitiveX2 mup2 = new MuPrimitiveX2();
	
	MuPrimitiveX1[] muPrimitives1 = {
			mup1.new size_str()
	};
	
	MuPrimitiveX2[] muPrimitives2 = {
			mup2.new addition_mint_mint()
	};
	

}
