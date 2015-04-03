package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadType extends Instruction {
	
	final int type;
	
	public LoadType(CodeBlock ins, int type) {
		super(ins, Opcode.LOADTYPE);
		this.type = type;
	}
	
	public String toString() { return "LOADTYPE " + type + "[" + codeblock.getConstantType(type) + "]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (!dcode)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineLoadConOrType(type, false,  dcode); 
		codeblock.addCode1(opcode.getOpcode(), type);
	}
}
