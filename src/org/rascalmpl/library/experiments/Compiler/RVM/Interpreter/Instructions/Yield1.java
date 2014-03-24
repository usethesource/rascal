package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Yield1 extends Instruction {
	// Entry point management for finding "NEXT" location
	private int hotEntryPoint = 0 ;
	final int arity;
	
	public Yield1(CodeBlock ins, int arity, int ep) {
		super(ins, Opcode.YIELD1);
		this.arity = arity;
		this.hotEntryPoint = ep ;
	}
	
	public String toString() { return "YIELD1 " + arity; }
	
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnYIELD1",arity);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}
}
