package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class SubtractInt extends Instruction {
	
	public SubtractInt(CodeBlock ins) {
		super(ins, Opcode.SUBTRACTINT);
	}
}
