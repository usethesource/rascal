package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LessInt extends Instruction {
	
	public LessInt(CodeBlock ins) {
		super(ins, Opcode.LESSINT);
	}

}
