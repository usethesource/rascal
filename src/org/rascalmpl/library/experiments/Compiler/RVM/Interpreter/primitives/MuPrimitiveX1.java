package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.primitives;

import org.rascalmpl.value.IString;

public class MuPrimitiveX1 {

	public Object execute1(Object arg_1) { return null; }

	class size_str extends MuPrimitiveX1 {
		/**
		 * Size of IString
		 * 
		 * [ ..., IString ] => [ ..., mint ]
		 */
		@Override
		public Object execute1(Object arg_1) {
			return ((IString) arg_1).length();
		};
	}
}
