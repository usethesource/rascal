package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;


public class LoadLoc0 extends Instruction {

	public LoadLoc0(CodeBlock ins){
		super(ins, Opcode.LOADLOC0);
	}
	public void generate(){
		System.out.println("LOADLOC0");
		codeblock.addCode0(opcode.getOpcode());
	}
}
