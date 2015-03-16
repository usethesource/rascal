package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadCont extends Instruction {
	
	final String fuid;
	
	public LoadCont(CodeBlock ins, String fuid) {
		super(ins, Opcode.LOADCONT);
		this.fuid = fuid;
	}
	
	public String toString() { return "LOADCONT " + fuid + "[" + codeblock.getFunctionIndex(fuid) + "]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCall("insnLOADCONT", codeblock.getFunctionIndex(fuid));
		codeblock.addCode1(opcode.getOpcode(), codeblock.getFunctionIndex(fuid));
	}
}
