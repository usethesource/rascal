package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class AddInt extends Instruction {
	
	public AddInt(CodeBlock ins) {
		super(ins, Opcode.ADDINT);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnADDINT");
		codeblock.addCode0(opcode.getOpcode());
	}

}
