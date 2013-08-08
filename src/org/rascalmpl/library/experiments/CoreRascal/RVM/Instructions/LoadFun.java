package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadFun extends Instruction {

	String function;
	
	public LoadFun(CodeBlock ins, String function){
		super(ins, Opcode.LOADFUN);
		this.function = function;
	}
	
	public String toString() { return "LOADFUN " + function + "[" + codeblock.getFunctionIndex(function) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(function));
	}

}
