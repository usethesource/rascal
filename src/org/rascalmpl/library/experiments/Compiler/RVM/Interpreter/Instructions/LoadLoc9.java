package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;

public class LoadLoc9 extends Instruction {

	public LoadLoc9(CodeBlock ins){
		super(ins, Opcode.LOADLOC9);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC9");
		codeblock.addCode0(opcode.getOpcode());
	}
}
