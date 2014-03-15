package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Return1 extends Instruction {
	
	final int arity;
	
	public Return1(CodeBlock ins, int arity){
		super(ins, Opcode.RETURN1);
		this.arity = arity;
	}

	public String toString() { return "RETURN1 " + arity; }
	
	public void generate(){
		System.out.println("\tRETURN1 " + arity);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
}
