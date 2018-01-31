package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.BytecodeGenerator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;

public class Jmp extends Instruction {

	String label;

	public Jmp(CodeBlock ins, String label) {
		super(ins, Opcode.JMP);
		this.label = label;
	}

	public String toString() {
		return "JMP " + label + " [" + codeblock.getLabelPC(label) + "]";
	}

	public void generate() {
		codeblock.addCode1(opcode.getOpcode(), codeblock.getLabelPC(label));
	}

	public void generateByteCode(BytecodeGenerator codeEmittor, boolean debug) {
		if ( debug ) 
			codeEmittor.emitDebugCall1(opcode.name(),codeblock.getLabelPC(label) );

		codeEmittor.emitJMP(label);
	}
}
