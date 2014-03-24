package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class SubtractInt extends Instruction {
	
	public SubtractInt(CodeBlock ins) {
		super(ins, Opcode.SUBTRACTINT);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnSUBTRACTINT");
		codeblock.addCode0(opcode.getOpcode());
	}
}
