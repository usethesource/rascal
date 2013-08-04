package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class JmpFalse extends Instruction {

	String label;
	
	public JmpFalse(CodeBlock ins, String label){
		super(ins, Opcode.JMPFALSE);
		this.label = label;
	}
	
	public String toString() { return "JMPFALSE " + label + " [" + ins.labels.get(label) + "]"; }
	
	public void generate(){
		ins.addCode(opcode.getOpcode());
		ins.addCode(ins.labels.get(label));
	}
}
