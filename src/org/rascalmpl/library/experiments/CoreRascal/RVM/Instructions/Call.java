package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class Call extends Instruction {

	String fun;
	
	public Call(CodeBlock ins, String fun){
		super(ins, Opcode.CALL);
		this.fun = fun;
	}
	
	public String toString() { return "CALL " + fun + "[" + codeblock.getFunctionIndex(fun) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(fun));
	}

}
