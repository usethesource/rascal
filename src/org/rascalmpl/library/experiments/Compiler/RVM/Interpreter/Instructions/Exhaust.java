package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Exhaust extends Instruction {
	
	public Exhaust(CodeBlock ins) {
		super(ins, Opcode.EXHAUST);
	}
	
}
