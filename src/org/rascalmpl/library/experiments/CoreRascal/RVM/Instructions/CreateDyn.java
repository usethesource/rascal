package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class CreateDyn extends Instruction {
	
	public CreateDyn(CodeBlock ins) {
		super(ins, Opcode.CREATEDYN);
	}

}
