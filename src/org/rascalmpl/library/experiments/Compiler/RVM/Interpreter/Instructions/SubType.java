package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class SubType extends Instruction {
	
	public SubType(CodeBlock ins) {
		super(ins, Opcode.SUBTYPE);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnSUBTYPE");
		codeblock.addCode0(opcode.getOpcode());
	}
}
