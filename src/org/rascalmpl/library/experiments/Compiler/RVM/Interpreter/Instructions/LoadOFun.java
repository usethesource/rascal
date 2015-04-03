package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadOFun extends Instruction {
	
	final String fuid;
	
	public LoadOFun(CodeBlock ins, String fuid) {
		super(ins, Opcode.LOADOFUN);
		this.fuid = fuid;
	}
	
	public String toString() { return "LOADOFUN " + fuid + " [ " + codeblock.getOverloadedFunctionIndex(fuid) + " ]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSFI("insnLOADOFUN", codeblock.getOverloadedFunctionIndex(fuid),debug);
		codeblock.addCode1(opcode.getOpcode(), codeblock.getOverloadedFunctionIndex(fuid));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSFI("insnLOADOFUN", codeblock.getOverloadedFunctionIndex(fuid),debug);
	}
}
