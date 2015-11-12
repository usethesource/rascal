package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.primitives;

import org.rascalmpl.value.IString;

public class MuPrimitive1 {
	
	public Object execute1(Object arg_1) { return null; }

	class size_str extends MuPrimitive1 {
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
