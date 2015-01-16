package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Shift extends Instruction {
	
	public Shift(CodeBlock ins) {
		super(ins, Opcode.SHIFT);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnSHIFT");
		codeblock.addCode0(opcode.getOpcode());
	}
}
