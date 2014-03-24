package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class SubscriptList extends Instruction {
	
	public SubscriptList(CodeBlock ins) {
		super(ins, Opcode.SUBSCRIPTLIST);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnSUBSCRIPTLIST");
		codeblock.addCode0(opcode.getOpcode());
	}
}
