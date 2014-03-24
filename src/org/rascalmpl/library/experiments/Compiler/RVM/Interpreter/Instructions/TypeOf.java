package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class TypeOf extends Instruction {
	
	public TypeOf(CodeBlock ins) {
		super(ins, Opcode.TYPEOF);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnTYPEOF");
		codeblock.addCode0(opcode.getOpcode());
	}
}
