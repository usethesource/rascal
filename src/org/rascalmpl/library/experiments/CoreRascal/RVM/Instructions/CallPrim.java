package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class CallPrim extends Instruction {

	int fun;
	
	public CallPrim(CodeBlock ins, int fun){
		super(ins, Opcode.CALLPRIM);
		this.fun = fun;
	}
	
	public String toString() { return "CALLPRIM " + fun; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(fun);
	}
}
