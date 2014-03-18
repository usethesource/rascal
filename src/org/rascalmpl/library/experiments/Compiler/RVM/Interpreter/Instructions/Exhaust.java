package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Exhaust extends Instruction {
	
	public Exhaust(CodeBlock ins) {
		super(ins, Opcode.EXHAUST);
	}
	public void generate(Generator codeEmittor){
		codeEmittor.emitCall("insnEXHAUST");
		codeblock.addCode0(opcode.getOpcode());
	}
}
