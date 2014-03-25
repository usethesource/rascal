package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Reset extends Instruction {
	
	public Reset(CodeBlock ins) {
		super(ins, Opcode.RESET);
	}

}
