package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class SubscriptArray extends Instruction {
	
	public SubscriptArray(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTARRAY);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if ( !debug) 
			codeEmittor.emitDebugCall(opcode.name());

		codeEmittor.emitCallWithArgsSS("insnSUBSCRIPTARRAY");
		codeblock.addCode0(opcode.getOpcode());
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug) 
			codeEmittor.emitDebugCall(opcode.name());

		codeEmittor.emitCallWithArgsSS("insnSUBSCRIPTARRAY");
	}
}
