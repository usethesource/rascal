package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Create extends Instruction {
	
	String function;
	
	public Create(CodeBlock ins, String function) {
		super(ins, Opcode.CREATE);
		this.function = function;
	}
	
	public String toString() { return "CREATE " + function + "[" + codeblock.getFunctionIndex(function) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(function));
	}


}
