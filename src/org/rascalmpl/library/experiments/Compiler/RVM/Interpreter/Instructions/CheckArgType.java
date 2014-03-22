package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class CheckArgType extends Instruction {
	
	public CheckArgType(CodeBlock ins) {
		super(ins, Opcode.CHECKARGTYPE);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnCHECKARGTYPE");
		codeblock.addCode0(opcode.getOpcode());
	}
}
