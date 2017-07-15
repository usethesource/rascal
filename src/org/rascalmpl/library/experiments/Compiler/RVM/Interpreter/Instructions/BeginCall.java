package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class BeginCall extends Instruction {
	int arity;
	public BeginCall(CodeBlock ins, int arity) {
		super(ins, Opcode.BEGINCALL);
		this.arity = arity;
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		//codeEmittor.emitCallWithArgsPA_A("insnADDINT");
		codeEmittor.emitInlineBeginCall(arity);
	}
}
