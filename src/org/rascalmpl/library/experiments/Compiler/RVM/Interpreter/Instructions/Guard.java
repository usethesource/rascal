package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Guard extends Instruction {
	
	public Guard(CodeBlock ins) {
		super(ins, Opcode.GUARD);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnGUARD");
		codeblock.addCode0(opcode.getOpcode());
	}
}
