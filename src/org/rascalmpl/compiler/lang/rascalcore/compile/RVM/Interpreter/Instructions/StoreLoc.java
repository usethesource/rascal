package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class StoreLoc extends  Instruction {

	int pos;
	
	public StoreLoc(CodeBlock ins, int pos){
		super(ins, Opcode.STORELOC);
		this.codeblock = ins;
		this.pos = pos;
	}
	
	public String toString() { return "STORELOC " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), pos);
		
		codeEmittor.emitInlineStoreLoc(pos);
	}
}
