package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class FilterReturn extends Instruction {
	
	public FilterReturn(CodeBlock ins) {
		super(ins, Opcode.FILTERRETURN);
	}

}
