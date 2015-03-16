package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class SubscriptList extends Instruction {
	
	public SubscriptList(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTLIST);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (!dcode)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSS("insnSUBSCRIPTLIST");
		codeblock.addCode0(opcode.getOpcode());
	}
}
