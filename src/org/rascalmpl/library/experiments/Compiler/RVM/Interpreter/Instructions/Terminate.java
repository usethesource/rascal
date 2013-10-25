package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Terminate extends Instruction {
	
	public Terminate(CodeBlock ins) {
		super(ins, Opcode.TERMINATE);
	}
	
}
