package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Exhaust extends Instruction {

	public Exhaust(CodeBlock ins) {
		super(ins, Opcode.EXHAUST);
	}

	public void generate(BytecodeGenerator codeEmittor, boolean debug) {
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		

		codeEmittor.emitInlineExhaust(debug);
		codeblock.addCode0(opcode.getOpcode());
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineExhaust(debug);
	}
}
