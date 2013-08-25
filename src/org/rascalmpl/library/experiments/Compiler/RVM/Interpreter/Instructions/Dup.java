package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Dup extends Instruction {

	public Dup(CodeBlock ins) {
		super(ins, Opcode.DUP);
	}
	
}
