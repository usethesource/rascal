package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import io.usethesource.vallang.ISourceLocation;

public class Throw extends Instruction {
	
	private final ISourceLocation src;
	
	public Throw(CodeBlock ins, ISourceLocation src) {
		super(ins, Opcode.THROW);
		this.src = src;
	}
	
	public String toString() { return "THROW " + src; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(src));
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), codeblock.getConstantIndex(src));
		
		// TODO add source lines.
		codeEmittor.emitInlineThrow();
	}
}
