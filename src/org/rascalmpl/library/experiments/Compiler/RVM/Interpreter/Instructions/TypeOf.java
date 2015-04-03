package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class TypeOf extends Instruction {
	
	public TypeOf(CodeBlock ins) {
		super(ins, Opcode.TYPEOF);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitVoidCallWithArgsSS("insnTYPEOF",debug);
		codeblock.addCode0(opcode.getOpcode());
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitVoidCallWithArgsSS("insnTYPEOF",debug);
	}
}
