package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Note extends Instruction {
	
	int constant;
	
	public Note(CodeBlock cb, int txt){
		super(cb, Opcode.NOTE);
		this.constant = txt;
	}
	
	public String toString() { return "NOTE " + constant + "[" + codeblock.getConstantValue(constant) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(constant);
	}
}
