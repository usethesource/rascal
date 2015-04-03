package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Apply extends Instruction {
	
	final String fuid;
	final int arity;
	
	public Apply(CodeBlock ins, String fuid, int arity) {
		super(ins, Opcode.APPLY);
		this.fuid = fuid;
		this.arity = arity;
	}
	
	public String toString() { return "APPLY " + fuid + ", " + arity + " [ " + codeblock.getFunctionIndex(fuid) + " ]"; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSII("insnAPPLY", codeblock.getFunctionIndex(fuid), arity,dcode);
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), arity);
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSII("insnAPPLY", codeblock.getFunctionIndex(fuid), arity,debug);
	}
}
