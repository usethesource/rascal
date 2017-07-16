package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;

public class PushCallMuPrim3 extends Instruction {

	MuPrimitive muprim;

	public PushCallMuPrim3(CodeBlock ins, MuPrimitive muprim) {
		super(ins, Opcode.PUSHCALLMUPRIM3);
		this.muprim = muprim;
	}

	public String toString() {
		return "PUSHCALLMUPRIM3 " + muprim;
	}

	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), muprim.ordinal());
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), muprim.ordinal());
		
		codeEmittor.emitInlinePushCallMuPrim3(muprim);
	}
}
