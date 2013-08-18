package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Create extends Instruction {
	
	final String function;
	final int arity;
	
	public Create(CodeBlock ins, String function, int arity) {
		super(ins, Opcode.CREATE);
		this.function = function;
		this.arity = arity;
	}
	
	public String toString() { return "CREATE " + function + "[" + codeblock.getFunctionIndex(function) + ", " + arity + "]"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(codeblock.getFunctionIndex(function));
		codeblock.addCode(arity);
	}

}
