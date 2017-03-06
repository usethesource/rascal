package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import io.usethesource.vallang.IValue;

public class LoadCon extends Instruction {

	int constant;

	public LoadCon(CodeBlock cb, int constant) {
		super(cb, Opcode.LOADCON);
		this.constant = constant;
	}

	public String toString() {
		return "LOADCON " + constant + "[" + codeblock.getConstantValue(constant) + "]";
	}

	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), constant);
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if (debug) {
			codeEmittor.emitDebugCall2(opcode.name(), codeblock.getConstantValue(constant).toString(), constant);
		}

		IValue val = codeblock.getConstantValue(constant);
		codeEmittor.emitInlineLoadConOrType(constant,true);
	}
}
