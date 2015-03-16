package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class LoadLoc3 extends Instruction {

	public LoadLoc3(CodeBlock ins) {
		super(ins, Opcode.LOADLOC3);
	}

	public void generate(BytecodeGenerator codeEmittor, boolean debug) {
		if (!debug)
			codeEmittor.emitDebugCall(opcode.name());
		codeEmittor.emitInlineLoadLocN(3, debug);
		codeblock.addCode0(opcode.getOpcode());
	}
}
