package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Return1 extends Instruction {

	public Return1(CodeBlock ins){
		super(ins, Opcode.RETURN1);
	}

}
