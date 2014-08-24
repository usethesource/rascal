package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class CheckArgType extends Instruction {
	
	public CheckArgType(CodeBlock ins) {
		super(ins, Opcode.CHECKARGTYPE);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		codeEmittor.emitCallWithArgs("insnCHECKARGTYPE");
		codeblock.addCode0(opcode.getOpcode());
	}
}
