package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class SubType extends Instruction {
	
	public SubType(CodeBlock ins) {
		super(ins, Opcode.SUBTYPE);
	}

}
