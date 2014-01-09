package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class UnwrapThrown extends Instruction {
	
	final int pos;
	
	public UnwrapThrown(CodeBlock ins, int pos) {
		super(ins, Opcode.UNWRAPTHROWN);
		this.pos = pos;
	}
	
	public String toString() { return "UNWRAPTHROWN " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
	
}
