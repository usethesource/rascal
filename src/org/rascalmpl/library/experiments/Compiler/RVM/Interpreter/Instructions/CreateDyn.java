package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class CreateDyn extends Instruction {

	final int arity;
	
	public CreateDyn(CodeBlock ins, int arity) {
		super(ins, Opcode.CREATEDYN);
		this.arity = arity;
	}
	
	public String toString() { return "CREATEDYN " + arity; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSFI("jvmCREATEDYN", arity, debug);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSFI("jvmCREATEDYN", arity, debug);
	}
}
