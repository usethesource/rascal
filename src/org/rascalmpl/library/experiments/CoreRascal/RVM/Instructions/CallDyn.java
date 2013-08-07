package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class CallDyn extends Instruction {

	public CallDyn(CodeBlock ins){
		super(ins, Opcode.CALLDYN);
	}

}
