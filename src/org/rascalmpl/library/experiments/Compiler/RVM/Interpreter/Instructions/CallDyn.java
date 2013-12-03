package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CallDyn extends Instruction {
	
	final int arity;

	public CallDyn(CodeBlock ins, int arity){
		super(ins, Opcode.CALLDYN);
		this.arity = arity;
	}
	
	public String toString() { return "CALLDYN " + arity; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), arity);
	}

}
