package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Label extends Instruction {

	String label;
	
	public Label(CodeBlock ins, String label){
		super(ins, Opcode.LABEL);
		this.label = label;
		ins.defLabel(label, this);
	}
	
	public String toString() { return "LABEL " + label + " [" +  "]"; }
	
	public void generate(){
		System.out.println("LABEL " + label + " [" +  "]");
	}
}
