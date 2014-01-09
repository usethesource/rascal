package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Guard extends Instruction {
	
	public Guard(CodeBlock ins) {
		super(ins, Opcode.GUARD);
	}

}
