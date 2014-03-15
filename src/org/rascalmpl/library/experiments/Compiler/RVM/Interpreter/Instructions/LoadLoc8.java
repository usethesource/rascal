package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Generator;


public class LoadLoc8 extends Instruction {

	public LoadLoc8(CodeBlock ins){
		super(ins, Opcode.LOADLOC8);
	}
	public void generate(Generator codeEmittor){
		System.out.println("LOADLOC8");
		codeEmittor.emitCall("insnLOADLOC8");
		codeblock.addCode0(opcode.getOpcode());
	}
}
