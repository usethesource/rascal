package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Pop extends Instruction {

	public Pop(CodeBlock ins){
		super(ins, Opcode.POP);
	}

}
