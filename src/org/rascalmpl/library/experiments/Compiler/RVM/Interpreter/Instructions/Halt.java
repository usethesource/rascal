package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;


public class Halt extends Instruction {

	public Halt(CodeBlock ins){
		super(ins, Opcode.HALT);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		if ( dcode ) 
			codeEmittor.emitCall("dinsnHALT");
		codeblock.addCode0(opcode.getOpcode());
	}
}
