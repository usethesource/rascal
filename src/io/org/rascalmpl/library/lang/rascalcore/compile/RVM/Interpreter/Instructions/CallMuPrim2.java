package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;

public class CallMuPrim2 extends Instruction {

	MuPrimitive muprim;

	public CallMuPrim2(CodeBlock ins, MuPrimitive muprim) {
		super(ins, Opcode.CALLMUPRIM2);
		this.muprim = muprim;
	}

	public String toString() {
		return "CALLMUPRIM2 " + muprim;
	}

	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), muprim.ordinal());
	}
	
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall2(opcode.name(), muprim.name(), 2);
		
		codeEmittor.emitInlineCallMuPrim2(muprim);
	}
}
