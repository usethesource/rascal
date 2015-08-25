package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class CheckMemo extends Instruction {
	
	public CheckMemo(CodeBlock ins){
		super(ins, Opcode.CHECKMEMO);
		
	}

	public String toString() { return "CHECKMEMO"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
	}

	// TODO
//	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
//		if ( debug ) 
//			codeEmittor.emitDebugCall(opcode.name());
//		
//		codeEmittor.emitInlineReturn(1,debug);
//	}
}
