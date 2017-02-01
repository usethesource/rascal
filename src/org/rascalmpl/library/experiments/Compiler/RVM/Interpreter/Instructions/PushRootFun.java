package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushRootFun extends Instruction {

	final String fuid;
	
	public PushRootFun(CodeBlock ins, String fuid){
		super(ins, Opcode.PUSH_ROOT_FUN);
		this.fuid = fuid;
	}
	
	public String toString() { return "PUSH_ROOT_FUN " + fuid + "[" + codeblock.getFunctionIndex(fuid) + "]"; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), codeblock.getFunctionIndex(fuid));
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall2(opcode.name(), fuid, codeblock.getFunctionIndex(fuid));
	
		codeEmittor.emitCallWithArgsSSI_S("insnPUSH_ROOT_FUN", codeblock.getFunctionIndex(fuid));
	}
}
