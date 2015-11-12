package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.primitives;

public class MuPrimitive2 {

	public Object execute2(Object arg_2, Object arg_1) { return null; }
	
	class addition_mint_mint extends MuPrimitive2 {
		/**
		 * mint3 = mint1 + mint2
		 * 
		 * [ ..., mint1, mint2 ] => [ ..., mint3 ]
		 *
		 */
		@Override
		public Object execute2(Object arg_2, Object arg_1) {
			return ((int) arg_2) + ((int) arg_1);
		};
	}

}
