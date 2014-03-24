package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class SubscriptArray extends Instruction {
	
	public SubscriptArray(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTARRAY);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnSUBSCRIPTARRAY");
		codeblock.addCode0(opcode.getOpcode());
	}
}
