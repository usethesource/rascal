package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushConstr extends Instruction {
	
	final String fuid;
	
	public PushConstr(CodeBlock ins, String fuid) {
		super(ins, Opcode.PUSHCONSTR);
		this.fuid = fuid;
	}
	
	public String toString() { return "PUSHCONSTR " + fuid + "[" + codeblock.getConstructorIndex(fuid) + "]"; }
	
	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), codeblock.getConstructorIndex(fuid));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), codeblock.getConstructorIndex(fuid));
		
		codeEmittor.emitCallWithArgsSSI_S("insnPUSHCONSTR", codeblock.getConstructorIndex(fuid));
	}
}
