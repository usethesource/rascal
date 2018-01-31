package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;

public class PushCallMuPrim2 extends Instruction {

	MuPrimitive muprim;

	public PushCallMuPrim2(CodeBlock ins, MuPrimitive muprim) {
		super(ins, Opcode.PUSHCALLMUPRIM2);
		this.muprim = muprim;
	}

	public String toString() {
		return "PUSHCALLMUPRIM2 " + muprim;
	}

	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), muprim.ordinal());
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(), muprim.ordinal());
		
		codeEmittor.emitInlinePushCallMuPrim2(muprim);
	}
}
