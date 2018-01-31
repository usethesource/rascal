package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class CreateDyn extends Instruction {

	final int arity;
	
	public CreateDyn(CodeBlock ins, int arity) {
		super(ins, Opcode.CREATEDYN);
		this.arity = arity;
	}
	
	public String toString() { return "CREATEDYN " + arity; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), arity);
		
		codeEmittor.emitInlineCreateDyn(arity);
	}
}
