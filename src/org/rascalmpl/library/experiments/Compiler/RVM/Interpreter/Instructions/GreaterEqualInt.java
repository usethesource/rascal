package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class GreaterEqualInt extends Instruction {
	
	public GreaterEqualInt(CodeBlock ins) {
		super(ins, Opcode.GREATEREQUALINT);
	}

}
