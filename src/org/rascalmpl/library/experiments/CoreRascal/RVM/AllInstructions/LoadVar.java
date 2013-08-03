package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class LoadVar extends Instruction {

	int pos;
	int scope;
	
	LoadVar(Instructions ins, int scope, int pos){
		super(ins, Opcode.LOADVAR);
		this.scope = scope;
		this.pos = pos;
	}
	
	public String toString() { return "LOADVar " + scope + ", " + pos; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(scope);
		ins.addCode(pos);
	}
}
