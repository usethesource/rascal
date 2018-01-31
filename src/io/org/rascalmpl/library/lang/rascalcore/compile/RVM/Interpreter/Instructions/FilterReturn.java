package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class FilterReturn extends Instruction {
	
	public FilterReturn(CodeBlock ins) {
		super(ins, Opcode.FILTERRETURN);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		// TODO implement.
		codeEmittor.emitDebugCall(opcode.name());
	}
}
