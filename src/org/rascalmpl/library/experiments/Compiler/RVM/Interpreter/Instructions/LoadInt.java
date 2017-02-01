package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadInt extends Instruction {
	
	final int nval;
	
	public LoadInt(CodeBlock ins, int nval) {
		super(ins, Opcode.LOADINT);
		this.nval = nval;
	}
	
	public String toString() { return "LOADINT " + nval; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), nval);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), nval);
		
		codeEmittor.emitInlineLoadInt(nval);
	}
}
