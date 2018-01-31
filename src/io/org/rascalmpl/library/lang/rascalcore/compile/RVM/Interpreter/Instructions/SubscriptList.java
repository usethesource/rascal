package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class SubscriptList extends Instruction {
	
	public SubscriptList(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTLIST);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		//codeEmittor.emitCallWithArgsPA_A("insnSUBSCRIPTLIST");
		
		codeEmittor.emitInlineSubscriptList();
	}
}
