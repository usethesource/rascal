package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Pop extends Instruction {

	public Pop(CodeBlock ins){
		super(ins, Opcode.POP);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		//codeEmittor.emitInlinePop(dcode);
		codeEmittor.emitCall("insnPOP"); 
		codeblock.addCode0(opcode.getOpcode());
	}
}
