package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

public class Label extends Instruction {

	String label;
	
	Label(Instructions ins, String label){
		super(ins, Opcode.LABEL);
		this.label = label;
		ins.defLabel(label);
	}
	
	public String toString() { return "LABEL " + label + " [" +  "]"; }
	
	public void generate(){
	}
}
