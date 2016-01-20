package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class PushLoc extends Instruction {

	int pos;
	
	public PushLoc(CodeBlock ins, int pos){
		super(ins, Opcode.PUSHLOC);
		this.pos = pos;
	}
	
	public String toString() { return "PUSHLOC " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlinePushLoc(pos,debug);
	}
}
