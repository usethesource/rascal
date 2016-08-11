package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushLocDeref extends Instruction {

	int pos;
	
	public PushLocDeref(CodeBlock ins, int pos) {
		super(ins, Opcode.PUSHLOCDEREF);
		this.pos = pos;
	}
	
	public String toString() { return "PUSHLOCDEREF " + pos; }
	
	public void generate(){
		codeblock.addCode1(opcode.getOpcode(), pos);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug){
		if (debug)
			codeEmittor.emitDebugCall1(opcode.name(), pos);
		
		//codeEmittor.emitCallWithArgsSSI_S("insnPUSHLOCDEREF", pos,debug);
		codeEmittor.emitInlinePushLocDeref(pos);
	}
}
