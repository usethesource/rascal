package org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions;

import org.rascalmpl.library.experiments.CoreRascal.RVM.CodeBlock;

public class LoadNestedFun extends Instruction {

	final String function;
	final int scope;
	
	public LoadNestedFun(CodeBlock ins, String function, int scope) {
		super(ins, Opcode.LOAD_NESTED_FUN);
		this.function = function;
		this.scope = scope;
	}
	
	public String toString() { return "LOAD_NESTED_FUN " + function + ", " + scope; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(function));
		codeblock.addCode(scope);
	}

}
