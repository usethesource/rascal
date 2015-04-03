package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Return1 extends Instruction {
	
	final int arity;
	
	public Return1(CodeBlock ins, int arity){
		super(ins, Opcode.RETURN1);
		this.arity = arity;
	}

	public String toString() { return "RETURN1 " + arity; }
	
	public void generate(BytecodeGenerator codeEmittor, boolean debug){
		if ( !debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineReturn(1,debug);
		codeblock.addCode1(opcode.getOpcode(), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineReturn(1,debug);
	}
}
