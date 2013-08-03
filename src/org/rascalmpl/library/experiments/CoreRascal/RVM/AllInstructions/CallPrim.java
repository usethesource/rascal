package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class CallPrim extends Instruction {

	int fun;
	
	CallPrim(Instructions ins, int fun){
		super(ins, Opcode.CALLPRIM);
		this.fun = fun;
	}
	
	public String toString() { return "CALLPRIM " + fun; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(fun);
	}
}
