package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class SubscriptList extends Instruction {
	
	public SubscriptList(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTLIST);
	}

}
