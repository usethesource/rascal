package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Throw extends Instruction {
	
	public Throw(CodeBlock ins) {
		super(ins, Opcode.THROW);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnTHROW");
		codeblock.addCode0(opcode.getOpcode());
	}
}
