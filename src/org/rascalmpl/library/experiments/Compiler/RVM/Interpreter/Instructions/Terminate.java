package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Terminate extends Instruction {
	
	final int arity;
	
	public Terminate(CodeBlock ins, int arity) {
		super(ins, Opcode.TERMINATE);
		this.arity = arity;
	}
	
	public String toString() { return "TERMINATE " + arity; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
		codeblock.addCode(arity);
	}


}
