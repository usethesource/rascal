package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;


public class Halt extends Instruction {

	public Halt(CodeBlock ins){
		super(ins, Opcode.HALT);
	}

}
