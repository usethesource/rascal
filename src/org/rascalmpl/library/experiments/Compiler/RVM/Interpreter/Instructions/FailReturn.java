package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class FailReturn extends Instruction {
	
	public FailReturn(CodeBlock ins) {
		super(ins, Opcode.FAILRETURN);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		// TODO version 1 done
		// Implement return system.
		// System.out.println("FAILRETURN");
		if (dcode)
			codeEmittor.emitCall("dinsnFAILRETURN");

		codeEmittor.emitInlineFailreturn();
		codeblock.addCode0(opcode.getOpcode());
	}
}
