package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class LoadLoc extends Instruction {

	int pos;
	
	LoadLoc(Instructions ins, int pos){
		super(ins, Opcode.LOADLOC);
		this.pos = pos;
	}
	
	public String toString() { return "LOADLOC " + pos; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(pos);
	}
}
