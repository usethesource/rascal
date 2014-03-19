package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;


public class LoadLoc5 extends Instruction {

	public LoadLoc5(CodeBlock ins){
		super(ins, Opcode.LOADLOC5);
	}
	public void generate(Generator codeEmittor){
		codeEmittor.emitCall("insnLOADLOC5");
		codeblock.addCode0(opcode.getOpcode());
	}
}
