package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadEmptyKwMap extends Instruction {
	
	public LoadEmptyKwMap(CodeBlock cb) {
		super(cb, Opcode.LOADEMPTYKWMAP);
	}

	public String toString() { return "LOADEMPTYKWMAP"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
	}

	// TODO
//	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
//		if ( debug ) 
//			codeEmittor.emitDebugCall(opcode.name());
//		
//		codeEmittor.emitInlineReturn(1,debug);
//	}
}
