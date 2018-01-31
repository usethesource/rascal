package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;

public class PushCallMuPrimN extends Instruction {

	MuPrimitive muprim;
	int arity;

	public PushCallMuPrimN(CodeBlock ins, MuPrimitive muprim, int arity) {
		super(ins, Opcode.PUSHCALLMUPRIMN);
		this.muprim = muprim;
		this.arity = arity;
	}

	public String toString() {
		return "PUSHCALLMUPRIMN " + muprim + ", " + arity;
	}

	public void generate() {
		codeblock.addCode2(opcode.getOpcode(), muprim.ordinal(), arity);
	}
	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall2(opcode.name(), muprim.name(), arity);
		
		codeEmittor.emitInlinePushCallMuPrimN(muprim, arity);
	}
}
