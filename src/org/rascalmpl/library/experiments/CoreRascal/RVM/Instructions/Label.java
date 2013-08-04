package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Label extends Instruction {

	String label;
	
	public Label(CodeBlock ins, String label){
		super(ins, Opcode.LABEL);
		this.label = label;
		ins.defLabel(label);
	}
	
	public String toString() { return "LABEL " + label + " [" +  "]"; }
	
	public void generate(){
	}
}
