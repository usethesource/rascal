package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushLocRef extends Instruction {
	
	final int pos;
	
	public PushLocRef(CodeBlock ins, int pos) {
		super(ins, Opcode.PUSHLOCREF);
		this.pos = pos;
	}

	public String toString() { return "PUSHLOCREF " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), pos);
		
		//codeEmittor.emitCallWithArgsSSI_S("insnPUSHLOCREF", pos,debug) ;
		codeEmittor.emitInlinePushLocRef(pos);
	}
}
