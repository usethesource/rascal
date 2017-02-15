package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushOFun extends Instruction {
	
	final String fuid;
	
	public PushOFun(CodeBlock ins, String fuid) {
		super(ins, Opcode.PUSHOFUN);
		this.fuid = fuid;
	}
	
	public String toString() { return "PUSHOFUN " + fuid + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]"; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), codeblock.getOverloadedFunctionIndex(fuid));
		
		codeEmittor.emitCallWithArgsSSFI_S("insnPUSHOFUN", codeblock.getOverloadedFunctionIndex(fuid));
	}
}
