package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Next1 extends Instruction {
	
	public Next1(CodeBlock ins) {
		super(ins, Opcode.NEXT1);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		// TODO: implement!
		codeEmittor.emitDebugCall(opcode.name());
	}
}
