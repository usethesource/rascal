package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class ApplyDyn extends Instruction {
	
	final int arity;
	
	public ApplyDyn(CodeBlock ins, int arity) {
		super(ins, Opcode.APPLYDYN);
		this.arity = arity;
	}
	
	public String toString() { return "APPLYDYN " + arity; }
	
	public void generate(){
			codeblock.addCode1(opcode.getOpcode(), arity);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean dcode){
		if ( dcode ) 
			codeEmittor.emitDebugCall1(opcode.name(), arity);
		
		codeEmittor.emitCallWithArgsSSI_S("insnAPPLYDYN", arity);
	}
}	
