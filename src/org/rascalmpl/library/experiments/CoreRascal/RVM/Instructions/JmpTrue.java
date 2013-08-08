package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class JmpTrue extends Instruction {

	String label;
	
	public JmpTrue(CodeBlock ins, String label){
		super(ins, Opcode.JMPTRUE);
		this.label = label;
	}
	
	public String toString() { return "JMPTRUE " + label + " [" + codeblock.getLabelIndex(label) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getLabelIndex(label));
	}
}
