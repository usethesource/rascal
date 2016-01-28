package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

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
			codeEmittor.emitDebugCall(opcode.name());
		
		codeEmittor.emitCallWithArgsSSI("insnPUSHLOCREF", pos,debug) ;
	}
}
