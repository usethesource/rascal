package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LoadLoc7 extends Instruction {

	public LoadLoc7(CodeBlock ins) {
		super(ins, Opcode.LOADLOC7);
	}

	public void generate(Generator codeEmittor, boolean debug) {
		codeEmittor.emitCall("insnLOADLOC7");
		codeblock.addCode0(opcode.getOpcode());
	}
}
