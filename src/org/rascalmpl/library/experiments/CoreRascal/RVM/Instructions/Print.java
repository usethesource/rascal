package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Print extends Instruction {

	int constant;
	
	public Print(CodeBlock cb, int constant){
		super(cb, Opcode.PRINT);
		this.constant = constant;
	}
	
	public String toString() { return "PRINT " + constant + "[" + codeblock.getConstantValue(constant) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(constant);
	}

}
