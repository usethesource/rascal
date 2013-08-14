package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Init extends Instruction {

	public Init(CodeBlock ins) {
		super(ins, Opcode.INIT);
	}
		
}
