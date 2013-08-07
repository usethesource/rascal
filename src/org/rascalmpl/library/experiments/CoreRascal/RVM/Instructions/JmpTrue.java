package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class JmpTrue extends Instruction {

	String label;
	
	public JmpTrue(CodeBlock ins, String label){
		super(ins, Opcode.JMPTRUE);
		this.label = label;
	}
	
	public String toString() { return "JMPTRUE " + label + " [" + ins.labels.get(label) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		if(ins.labels.get(label) == null){
			throw new RuntimeException("PANIC: undefined label " + label);
		}
		ins.addCode(ins.labels.get(label));
	}
}
