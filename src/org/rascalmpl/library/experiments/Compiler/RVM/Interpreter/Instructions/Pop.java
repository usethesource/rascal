package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Pop extends Instruction {

	public Pop(CodeBlock ins){
		super(ins, Opcode.POP);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		//codeEmittor.emitCall("insnPOP"); 
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		codeEmittor.emitInlinePop(debug);
		codeblock.addCode0(opcode.getOpcode());
	}
}
