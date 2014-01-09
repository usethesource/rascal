package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class SubscriptArray extends Instruction {
	
	public SubscriptArray(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTARRAY);
	}

}
