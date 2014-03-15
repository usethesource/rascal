package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;


public class LoadLoc3 extends Instruction {

	public LoadLoc3(CodeBlock ins){
		super(ins, Opcode.LOADLOC3);
	}
	public void generate(){
		System.out.println("LOADLOC3");
		codeblock.addCode0(opcode.getOpcode());
	}
}
