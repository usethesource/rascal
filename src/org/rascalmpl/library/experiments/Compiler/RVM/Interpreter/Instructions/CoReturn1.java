package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class CoReturn1 extends Instruction {
	
	final int arity;
	
	public CoReturn1(CodeBlock ins, int arity){
		super(ins, Opcode.CORETURN1);
		this.arity = arity;
	}

	public String toString() { return "CORETURN1 " + arity; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitInlineCoReturn(arity,debug);
	}
}
