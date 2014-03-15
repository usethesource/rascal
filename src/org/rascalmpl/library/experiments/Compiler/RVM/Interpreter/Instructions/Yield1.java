package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Yield1 extends Instruction {
	
	final int arity;
	
	public Yield1(CodeBlock ins, int arity) {
		super(ins, Opcode.YIELD1);
		this.arity = arity;
	}
	
	public String toString() { return "YIELD1 " + arity; }
	
	public void generate(Generator codeEmittor){
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
}
