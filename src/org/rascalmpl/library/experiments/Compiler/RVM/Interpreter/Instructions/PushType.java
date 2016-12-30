package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushType extends Instruction {
	
	final int type;
	
	public PushType(CodeBlock ins, int type) {
		super(ins, Opcode.PUSHTYPE);
		this.type = type;
	}
	
	public String toString() { return "PUSHTYPE " + type + "[" + codeblock.getConstantType(type) + "]"; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), type);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), type);
		
		codeEmittor.emitInlinePushConOrType(type, false); 
	}
}
