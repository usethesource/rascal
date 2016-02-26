package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

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
