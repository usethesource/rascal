package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;

public class CallMuPrim1 extends Instruction {

	MuPrimitive muprim;

	public CallMuPrim1(CodeBlock ins, MuPrimitive muprim) {
		super(ins, Opcode.CALLMUPRIM1);
		this.muprim = muprim;
	}

	public String toString() {
		return "CALLMUPRIM1 " + muprim ;
	}

	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), muprim.ordinal());
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall2(opcode.name(), muprim.name(), 1);
		
		codeEmittor.emitInlineCallMuPrim1(muprim);
	}
}
