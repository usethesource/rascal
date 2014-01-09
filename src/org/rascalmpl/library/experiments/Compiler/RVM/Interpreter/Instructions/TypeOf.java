package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class TypeOf extends Instruction {
	
	public TypeOf(CodeBlock ins) {
		super(ins, Opcode.TYPEOF);
	}

}
