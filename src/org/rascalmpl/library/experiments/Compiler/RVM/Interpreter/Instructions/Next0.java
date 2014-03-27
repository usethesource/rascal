package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Next0 extends Instruction {

	public Next0(CodeBlock ins) {
		super(ins, Opcode.NEXT0);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("jvmNEXT0");
		codeblock.addCode0(opcode.getOpcode());
	}
}
