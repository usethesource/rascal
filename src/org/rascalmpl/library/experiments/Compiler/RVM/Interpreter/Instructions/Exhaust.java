package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.ToJVM.BytecodeGenerator;

public class Exhaust extends Instruction {

	public Exhaust(CodeBlock ins) {
		super(ins, Opcode.EXHAUST);
	}

	public void generate(BytecodeGenerator codeEmittor, boolean dcode) {
		if (dcode)
			codeEmittor.emitCall("dinsnEXHAUST");

		codeEmittor.emitInlineExhaust(dcode);
		codeblock.addCode0(opcode.getOpcode());
	}
}
