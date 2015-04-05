package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class FilterReturn extends Instruction {
	
	public FilterReturn(CodeBlock ins) {
		super(ins, Opcode.FILTERRETURN);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean dcode) {
		if ( dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		// TODO implement.
		codeEmittor.emitDebugCall(opcode.name());
	}
}
