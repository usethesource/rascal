package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Init extends Instruction {

	final int arity;
	
	public Init(CodeBlock ins, int arity) {
		super(ins, Opcode.INIT);
		this.arity = arity;
	}
	
	public String toString() { return "INIT " + arity; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
		
}
