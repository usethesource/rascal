package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LessInt extends Instruction {
	
	public LessInt(CodeBlock ins) {
		super(ins, Opcode.LESSINT);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		 codeEmittor.emitCallWithArgsSS("insnLESSINT");
		 codeblock.addCode0(opcode.getOpcode());
	}
}
