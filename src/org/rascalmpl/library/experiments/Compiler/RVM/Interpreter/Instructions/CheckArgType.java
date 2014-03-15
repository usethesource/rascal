package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CheckArgType extends Instruction {
	
	public CheckArgType(CodeBlock ins) {
		super(ins, Opcode.CHECKARGTYPE);
	}
	public void generate(){
		System.out.println("\tCHECKARGTYPE");
		codeblock.addCode0(opcode.getOpcode());
	}
}
