package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Pop extends Instruction {

	public Pop(CodeBlock ins){
		super(ins, Opcode.POP);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		 codeEmittor.emitInlinePop(dcode);
		 codeblock.addCode0(opcode.getOpcode());
	}
}
