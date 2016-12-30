package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Yield0 extends Instruction {
	// Entry point management for finding "NEXT" location
	private int hotEntryPoint = 0 ;
	
	public Yield0(CodeBlock ins, int ep) {
		super(ins, Opcode.YIELD0);
		this.hotEntryPoint = ep ;
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineYield(0,hotEntryPoint) ;
	}
}
