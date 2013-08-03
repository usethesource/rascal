package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class StoreVar extends Instruction {

	int pos;
	int scope;
	
	StoreVar(Instructions ins, int scope, int pos){
		super(ins, Opcode.STOREVAR);
		this.scope = scope;
		this.pos = pos;
	}
	
	public String toString() { return "STOREVAR " + scope + ", " + pos; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(scope);
		ins.addCode(pos);
	}
}
