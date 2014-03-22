package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;


public class LoadLoc1 extends Instruction {

	public LoadLoc1(CodeBlock ins){
		super(ins, Opcode.LOADLOC1);
	}
	public void generate(Generator codeEmittor, boolean dcode){
		codeEmittor.emitCall("insnLOADLOC1");
		codeblock.addCode0(opcode.getOpcode());
	}
}
