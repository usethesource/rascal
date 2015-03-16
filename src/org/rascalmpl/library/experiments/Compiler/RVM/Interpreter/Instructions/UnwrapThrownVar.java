package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class UnwrapThrownVar extends Instruction {
	
	final String fuid;
	final int pos;
	
	public UnwrapThrownVar(CodeBlock ins, String fuid, int pos) {
		super(ins, Opcode.UNWRAPTHROWNVAR);
		this.fuid = fuid;
		this.pos = pos;
	}
	
	public String toString() { 
		return "UNWRAPTHROWNVAR " + fuid + ", " + pos + " [" + codeblock.getFunctionIndex(fuid) + ", " + pos + "]";
	}
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if (!dcode)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSFIIZ("insnUNWRAPTHROWNVAR", codeblock.getFunctionIndex(fuid), pos, pos == -1,dcode); 
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), pos);
	}
}
