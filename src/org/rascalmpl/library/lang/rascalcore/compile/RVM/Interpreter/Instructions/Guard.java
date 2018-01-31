package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Guard extends Instruction {
	private int continuationPoint ;
	
	public Guard(CodeBlock ins, int continuationPoint) {
		super(ins, Opcode.GUARD);
		this.continuationPoint = continuationPoint ;
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineGuard(continuationPoint) ;
	}
}
