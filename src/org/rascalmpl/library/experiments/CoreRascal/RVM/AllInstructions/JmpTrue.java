package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class JmpTrue extends Instruction {

	String label;
	
	JmpTrue(Instructions ins, String label){
		super(ins, Opcode.JMPTRUE);
		this.label = label;
	}
	
	public String toString() { return "JMPTRUE " + label + " [" + ins.labels.get(label) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(ins.labels.get(label));
	}
}
