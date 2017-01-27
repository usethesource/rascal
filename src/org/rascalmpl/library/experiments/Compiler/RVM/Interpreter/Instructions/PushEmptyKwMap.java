package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushEmptyKwMap extends Instruction {
	
	public PushEmptyKwMap(CodeBlock cb) {
		super(cb, Opcode.PUSHEMPTYKWMAP);
	}

	public String toString() { return "PUSHEMPTYKWMAP"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlinePushEmptyKwMap();
	}
}
