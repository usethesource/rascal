 package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class PushCon extends Instruction {

	int constant;

	public PushCon(CodeBlock cb, int constant) {
		super(cb, Opcode.PUSHCON);
		this.constant = constant;
	}

	public String toString() {
		return "PUSHCON " + constant + "[" + codeblock.getConstantValue(constant) + "]";
	}

	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), constant);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if (debug) {
			String val = codeblock.getConstantValue(constant).toString();
			codeEmittor.emitDebugCall2(opcode.name(), val, constant);
		}

		codeEmittor.emitInlinePushConOrType(constant,true);
	}
}
