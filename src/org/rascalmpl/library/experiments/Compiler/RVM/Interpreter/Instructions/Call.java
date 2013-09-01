package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Call extends Instruction {

	final String fun;
	final int arity;
	
	public Call(CodeBlock ins, String fun, int arity){
		super(ins, Opcode.CALL);
		this.fun = fun;
		this.arity = arity;
	}
	
	public int spIncrement() {
		return arity + 1;
	}
	
	public String toString() { return "CALL " + fun + ", " + arity + "[" + codeblock.getFunctionIndex(fun) + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(fun));
		codeblock.addCode(arity);
	}

}
