package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Shift extends Instruction {
	
	public Shift(CodeBlock ins) {
		super(ins, Opcode.SHIFT);
	}

}
