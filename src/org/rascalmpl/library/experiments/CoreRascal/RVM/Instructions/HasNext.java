package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class HasNext extends Instruction {

	public HasNext(CodeBlock ins) {
		super(ins, Opcode.HASNEXT);
	}
	
}
