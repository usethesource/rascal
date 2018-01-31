package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class GreaterEqualInt extends Instruction {
	
	public GreaterEqualInt(CodeBlock ins) {
		super(ins, Opcode.GREATEREQUALINT);
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		//codeEmittor.emitCallWithArgsPA_A("insnGREATEREQUALINT");
		
		codeEmittor.emitInlineGreaterEqualInt();
		
	}
}
