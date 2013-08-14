package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CreateDyn extends Instruction {
	
	public CreateDyn(CodeBlock ins) {
		super(ins, Opcode.CREATEDYN);
	}

}
