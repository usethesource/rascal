package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class StoreLoc extends  Instruction {

	int pos;
	
	StoreLoc(Instructions ins, int pos){
		super(ins, Opcode.STORELOC);
		this.ins = ins;
		this.pos = pos;
	}
	
	public String toString() { return "STORELOC " + pos; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(pos);
	}
}
