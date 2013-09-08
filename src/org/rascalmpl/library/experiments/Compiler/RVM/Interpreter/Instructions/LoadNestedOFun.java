package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadNestedOFun extends Instruction {
	
	final String fuid;
	final String scopeIn;
	
	public LoadNestedOFun(CodeBlock ins, String fuid, String scopeIn) {
		super(ins, Opcode.LOAD_NESTED_OFUN);
		this.fuid = fuid;
		this.scopeIn = scopeIn;
	}
	
	public String toString() { return "LOAD_NESTED_)FUN " + fuid + ", " + scopeIn + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + ", " + codeblock.getFunctionIndex(scopeIn) + " ]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getOverloadedFunctionIndex(fuid));
		codeblock.addCode(codeblock.getFunctionIndex(scopeIn));
	}

}
