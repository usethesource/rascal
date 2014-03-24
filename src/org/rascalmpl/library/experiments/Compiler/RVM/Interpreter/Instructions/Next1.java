package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Next1 extends Instruction {
	
	public Next1(CodeBlock ins) {
		super(ins, Opcode.NEXT1);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnNEXT1");
		codeblock.addCode0(opcode.getOpcode());
	}
}
