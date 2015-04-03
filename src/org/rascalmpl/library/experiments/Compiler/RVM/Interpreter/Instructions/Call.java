package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Call extends Instruction {

	final String fuid;
	final int arity;
	int continuationPoint ; 

	public Call(CodeBlock ins, String fuid, int arity, int continuationPoint) {
		super(ins, Opcode.CALL);
		this.fuid = fuid;
		this.arity = arity;
		this.continuationPoint = continuationPoint ;
	}

	public String toString() {
		return "CALL " + fuid + ", " + arity + " [ " + codeblock.getFunctionIndex(fuid) + " ]";
	}

	public void generate(BytecodeGenerator codeEmittor, boolean debug) {
		
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineCall(codeblock.getFunctionIndex(fuid), arity, continuationPoint,debug) ;
		codeblock.addCode2(opcode.getOpcode(), codeblock.getFunctionIndex(fuid), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineCall(codeblock.getFunctionIndex(fuid), arity, continuationPoint,debug) ;
	}
}
