package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class AddInt extends Instruction {
	
	public AddInt(CodeBlock ins) {
		super(ins, Opcode.ADDINT);
	}

}
