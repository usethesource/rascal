package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class AndBool extends Instruction {
	
	public AndBool(CodeBlock ins) {
		super(ins, Opcode.ANDBOOL);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		//codeEmittor.emitCallWithArgsPA_A("insnANDBOOL");
		
		codeEmittor.emitInlineAndBool();
	}
}
