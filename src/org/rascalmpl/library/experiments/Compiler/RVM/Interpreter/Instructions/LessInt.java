package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LessInt extends Instruction {
	
	public LessInt(CodeBlock ins) {
		super(ins, Opcode.LESSINT);
	}
	public void generate(Generator codeEmittor){
		 codeEmittor.emitCall("insnLESSINT");
		 codeblock.addCode0(opcode.getOpcode());
	}
}
