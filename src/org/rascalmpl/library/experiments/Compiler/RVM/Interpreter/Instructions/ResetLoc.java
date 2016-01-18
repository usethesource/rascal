package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class ResetLoc extends Instruction {

	int pos;
	
	public ResetLoc(CodeBlock ins, int pos){
		super(ins, Opcode.RESETLOC);
		this.pos = pos;
	}
	
	public String toString() { return "RESETLOC " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineResetLoc(pos,debug);
	}
}
