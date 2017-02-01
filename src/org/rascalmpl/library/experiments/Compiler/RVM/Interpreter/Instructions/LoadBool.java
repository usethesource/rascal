package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class LoadBool extends Instruction {
	
	final boolean bool;
	
	public LoadBool(CodeBlock ins, boolean bool) {
		super(ins, Opcode.LOADBOOL);
		this.bool = bool;
	}
	
	public String toString() { return "LOADBOOL " + bool; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), bool ? 1 : 0);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), bool ? 1 : 0);
		
		codeEmittor.emitInlineLoadBool(bool) ;
	}
}
