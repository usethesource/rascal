package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public abstract class Instruction {
	
	protected Opcode opcode;
	protected Instructions ins;

	Instruction(Instructions ins, Opcode opc){
		this.opcode = opc;
		this.ins = ins;
	}
	
	public int pcIncrement() {
		return opcode.getIncrement();
	}
	
	public String toString(){
		return opcode.toString();
	}
	
	public void generate(){
		 ins.addCode(opcode.getOpcode());
	}
   
}
