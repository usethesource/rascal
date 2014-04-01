package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class Yield0 extends Instruction {
	// Entry point management for finding "NEXT" location
	private int hotEntryPoint = 0 ;
	
	public Yield0(CodeBlock ins, int ep) {
		super(ins, Opcode.YIELD0);
		this.hotEntryPoint = ep ;
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitInlineYield0(hotEntryPoint,dcode) ;
		codeblock.addCode0(opcode.getOpcode());
	}
}
