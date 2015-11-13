package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class CallMuPrimN extends Instruction {

	MuPrimitive muprim;
	int arity;

	public CallMuPrimN(CodeBlock ins, MuPrimitive muprim, int arity) {
		super(ins, Opcode.CALLMUPRIMN);
		this.muprim = muprim;
		this.arity = arity;
	}

	public String toString() {
		return "CALLMUPRIMN " + muprim + ", " + arity;
	}

	public void generate() {
		codeblock.addCode2(opcode.getOpcode(), muprim.ordinal(), arity);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall(opcode.name());
		codeEmittor.emitInlineCallMuPrime(muprim, arity, debug);
	}
}
