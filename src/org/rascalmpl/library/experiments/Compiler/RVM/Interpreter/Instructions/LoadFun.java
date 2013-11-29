package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadFun extends Instruction {

	final String fuid;
	
	public LoadFun(CodeBlock ins, String fuid){
		super(ins, Opcode.LOADFUN);
		this.fuid = fuid;
	}
	
	public String toString() { return "LOADFUN " + fuid + "[" + codeblock.getFunctionIndex(fuid) + "]"; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getFunctionIndex(fuid));
	}

}
