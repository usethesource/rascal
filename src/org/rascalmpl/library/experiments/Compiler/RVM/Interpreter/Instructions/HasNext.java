package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class HasNext extends Instruction {

	public HasNext(CodeBlock ins) {
		super(ins, Opcode.HASNEXT);
	}
	
}
