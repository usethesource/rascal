package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class FailReturn extends Instruction {
	
	public FailReturn(CodeBlock ins) {
		super(ins, Opcode.FAILRETURN);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		// TODO version 1 done
		// Implement return system.
		// System.out.println("FAILRETURN");
		codeEmittor.emitFailreturn();
		codeblock.addCode0(opcode.getOpcode());
	}
}
