package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class TypeOf extends Instruction {
	
	public TypeOf(CodeBlock ins) {
		super(ins, Opcode.TYPEOF);
	}
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (!dcode)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitVoidCallWithArgsSS("insnTYPEOF",dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
