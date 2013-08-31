package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class FailReturn extends Instruction {
	
	public FailReturn(CodeBlock ins) {
		super(ins, Opcode.FAILRETURN);
	}

}
