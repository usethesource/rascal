package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadCont extends Instruction {
	
	public LoadCont(CodeBlock ins) {
		super(ins, Opcode.LOADCONT);
	}

}
