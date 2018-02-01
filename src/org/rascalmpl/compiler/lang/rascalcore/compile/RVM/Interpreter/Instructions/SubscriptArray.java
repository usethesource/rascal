package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class SubscriptArray extends Instruction {
	
	public SubscriptArray(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTARRAY);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug) 
			codeEmittor.emitDebugCall(opcode.name());

		//codeEmittor.emitCallWithArgsPA_A("insnSUBSCRIPTARRAY");
		
		codeEmittor.emitInlineSubscriptArray();
	}
}
