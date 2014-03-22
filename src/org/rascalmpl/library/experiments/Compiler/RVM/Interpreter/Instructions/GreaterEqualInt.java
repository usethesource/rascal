package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class GreaterEqualInt extends Instruction {
	
	public GreaterEqualInt(CodeBlock ins) {
		super(ins, Opcode.GREATEREQUALINT);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnGREATEREQUALINT");
		codeblock.addCode0(opcode.getOpcode());
	}

}
