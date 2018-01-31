package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

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
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), arity);
		
		codeEmittor.emitInlineYield(arity, hotEntryPoint) ;
	}
}
