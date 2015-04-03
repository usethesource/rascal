package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;


public class Halt extends Instruction {

	public Halt(CodeBlock ins){
		super(ins, Opcode.HALT);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeblock.addCode0(opcode.getOpcode());
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean dcode){
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		// TODO : implement ?
		codeEmittor.emitDebugCall(opcode.name());	
	}
}
