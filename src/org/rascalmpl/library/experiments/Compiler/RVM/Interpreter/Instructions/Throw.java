package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

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
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineThrow(debug);
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstantIndex(src));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineThrow(debug);
	}
}
