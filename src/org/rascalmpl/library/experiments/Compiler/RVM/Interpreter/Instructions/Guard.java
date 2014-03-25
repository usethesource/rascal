package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Guard extends Instruction {
	private int continuationPoint ;
	
	public Guard(CodeBlock ins, int continuationPoint) {
		super(ins, Opcode.GUARD);
		this.continuationPoint = continuationPoint ;
	}
	public void generate(Generator codeEmittor, boolean dcode){

		codeEmittor.emitInlineGuard(continuationPoint,dcode) ;
		codeblock.addCode0(opcode.getOpcode());
	}
}
