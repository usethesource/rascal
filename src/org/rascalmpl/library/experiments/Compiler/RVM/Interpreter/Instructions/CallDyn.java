package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallDyn extends Instruction {

	public CallDyn(CodeBlock ins){
		super(ins, Opcode.CALLDYN);
	}

}
