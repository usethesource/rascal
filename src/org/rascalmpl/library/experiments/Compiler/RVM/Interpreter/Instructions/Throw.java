package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Throw extends Instruction {
	
	public Throw(CodeBlock ins) {
		super(ins, Opcode.THROW);
	}

}
