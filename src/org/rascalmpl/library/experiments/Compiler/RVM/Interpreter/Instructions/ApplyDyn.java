package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class ApplyDyn extends Instruction {
	
	final int arity;
	
	public ApplyDyn(CodeBlock ins, int arity) {
		super(ins, Opcode.APPLYDYN);
		this.arity = arity;
	}
	
	public String toString() { return "APPLYDYN " + arity; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean dcode){
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSI("insnAPPLYDYN", arity, dcode);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean dcode){
		if ( !dcode ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSI("insnAPPLYDYN", arity, dcode);
	}
}	
