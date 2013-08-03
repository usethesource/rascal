package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class Jmp extends Instruction {

	String label;

	Jmp(Instructions ins, String label){
		super(ins, Opcode.JMP);
		this.label = label;
	}
	
	public String toString() { return "JMP " + label + " [" + ins.labels.get(label) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(ins.labels.get(label));
	}
}
