package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class FilterReturn extends Instruction {
	
	public FilterReturn(CodeBlock ins) {
		super(ins, Opcode.FILTERRETURN);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode) {
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		 codeblock.addCode0(opcode.getOpcode());
	}
}
