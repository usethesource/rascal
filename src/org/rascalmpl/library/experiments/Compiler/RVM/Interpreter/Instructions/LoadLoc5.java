package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.CodeBlock;


public class LoadLoc5 extends Instruction {

	public LoadLoc5(CodeBlock ins){
		super(ins, Opcode.LOADLOC5);
	}
	public void generate(){
		System.out.println("LOADLOC5");
		codeblock.addCode0(opcode.getOpcode());
	}
}
