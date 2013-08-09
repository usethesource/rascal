package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Println extends Instruction {

	int constant;
	
	public Println(CodeBlock cb, int constant){
		super(cb, Opcode.PRINTLN);
		this.constant = constant;
	}
	
	public String toString() { return "PRINTLN " + constant + "[" + codeblock.getConstantValue(constant) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(constant);
	}

}
