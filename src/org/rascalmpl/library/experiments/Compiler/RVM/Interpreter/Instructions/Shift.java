package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Shift extends Instruction {
	
	public Shift(CodeBlock ins) {
		super(ins, Opcode.SHIFT);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		codeEmittor.emitDebugCall(opcode.name());
		
		codeblock.addCode0(opcode.getOpcode());
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());
		codeEmittor.emitDebugCall(opcode.name());
	}
}
