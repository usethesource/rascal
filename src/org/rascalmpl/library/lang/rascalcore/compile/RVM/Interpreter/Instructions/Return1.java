package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Return1 extends Instruction {
	
	public Return1(CodeBlock ins){
		super(ins, Opcode.RETURN1);
	}

	public String toString() { return "RETURN1"; }
	
	public void generate(){
		codeblock.addCode(opcode.getOpcode());
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineReturn(1);
	}
}
