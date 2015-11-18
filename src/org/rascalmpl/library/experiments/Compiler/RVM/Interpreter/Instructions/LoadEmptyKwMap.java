package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadEmptyKwMap extends Instruction {
	
	public LoadEmptyKwMap(CodeBlock cb) {
		super(cb, Opcode.LOADEMPTYKWMAP);
	}

	public String toString() { return "LOADEMPTYKWMAP"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineLoadEmptyKwMap(debug);
	}
}
